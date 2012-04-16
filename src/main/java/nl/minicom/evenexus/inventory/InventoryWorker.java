package nl.minicom.evenexus.inventory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.Item;
import nl.minicom.evenexus.persistence.dao.TransactionMatch;
import nl.minicom.evenexus.persistence.dao.TransactionMatchIdentifier;
import nl.minicom.evenexus.persistence.dao.WalletTransaction;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryWorker implements Callable<String> {

	private static final Logger LOG = LoggerFactory.getLogger(InventoryWorker.class);
	
	private final Database database;
	
	private long typeId = -1;
	
	@Inject
	public InventoryWorker(Database database) {
		this.database = database;
	}
	
	public void initialize(long typeId) {
		this.typeId = typeId;
	}

	@Override
	public String call() {
		if (typeId < 0) {
			throw new IllegalStateException("InventoryWorker not yet initialized!");
		}
		
		// Drop transactionMatches of the past days for this typeId, and reset remaining flag for wallet transactions.
		revertMostRecentTransactionsIfRequired();
		
		final Queue<WalletTransaction> buyTransactions = queryRemainingBuyTransactions();
		final Queue<WalletTransaction> sellTransactions = queryRemainingSellTransactions();
		
		if (!sellTransactions.isEmpty()) {
			LOG.info("Matching " + sellTransactions.size() + " unprocessed transactions for type: " + typeId);
		}
		
		while (!sellTransactions.isEmpty()) {
			if (match(buyTransactions, sellTransactions)) {
				break;
			}
		}
		
		return getName();
	}
	
	@Transactional
	protected String getName() {
		Session currentSession = database.getCurrentSession();
		Item item = (Item) currentSession.createCriteria(Item.class)
				.add(Restrictions.eq(Item.TYPE_ID, typeId))
				.setMaxResults(1)
				.uniqueResult();
		
		if (item == null) {
			return "Unknown";
		}
		
		return item.getTypeName();
	}

	private void revertMostRecentTransactionsIfRequired() {
		Timestamp timestamp = getEarliestMatchErrorTimestamp();
		if (timestamp != null) {
			List<TransactionMatch> matches = listInvalidMatches(timestamp);
			if (!matches.isEmpty()) {
				LOG.info("Found " + matches.size() + " invalid matches for type: " + typeId + ", now de-matching...");
				for (TransactionMatch match : matches) {
					revertMatch(match);
				}
				LOG.info("Finished de-matching " + matches.size() + " invalid matches for type: " + typeId + ".");
			}
		}
	}

	@Transactional
	void revertMatch(TransactionMatch match) {
		Session session = database.getCurrentSession();
		WalletTransaction buy = (WalletTransaction) session.get(WalletTransaction.class, match.getBuyTransactionId());
		WalletTransaction sell = (WalletTransaction) session.get(WalletTransaction.class, match.getSellTransactionId());
		
		buy.setRemaining(Math.min(buy.getQuantity(), buy.getRemaining() + match.getQuantity()));
		sell.setRemaining(Math.min(sell.getQuantity(), sell.getRemaining() + match.getQuantity()));
		
		session.update(buy);
		session.update(sell);
		session.delete(match);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	List<TransactionMatch> listInvalidMatches(Timestamp timestamp) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append("	m ");
		query.append("FROM ");
		query.append("	TransactionMatch AS m, ");
		query.append("	WalletTransaction AS s, ");
		query.append("	WalletTransaction AS b ");
		query.append("WHERE ");
		query.append("	(s.transactionId = m.key.sellTransactionId AND b.transactionId = m.key.buyTransactionId) AND ");
		query.append("	(s.transactionDateTime >= ? OR b.transactionDateTime >= ?) ");
		query.append("ORDER BY ");
		query.append("	s.transactionDateTime DESC, s.transactionId DESC");
		
		Session session = database.getCurrentSession();
		Query q = session.createQuery(query.toString());
		q.setTimestamp(0, timestamp);
		q.setTimestamp(1, timestamp);
		
		return (List<TransactionMatch>) q.list();
	}

	private Timestamp getEarliestMatchErrorTimestamp() {
		State previousBuyState = null;
		Timestamp buyMismatch = null;
		
		State previousSellState = null;
		Timestamp sellMismatch = null;
		
		List<WalletTransaction> transactions = listTransactionsInAscendingOrder();
		for (WalletTransaction transaction : transactions) {
			long remaining = transaction.getRemaining();
			long quantity = transaction.getQuantity();
			if (transaction.isBuy()) {
				if (previousBuyState == null || buyMismatch != null) {
					continue;
				}
				else if (remaining == quantity) { // UNTOUCHED
					if (previousBuyState != State.UNTOUCHED) {
						buyMismatch = transaction.getTransactionDateTime();
					}
					previousBuyState = State.UNTOUCHED;
				}
				else if (remaining > 0) { // PARTIAL
					if (previousBuyState == State.PARTIAL || previousBuyState == State.UNTOUCHED) {
						buyMismatch = transaction.getTransactionDateTime();
					}
					previousBuyState = State.PARTIAL;
				}
				else { // EXHAUSTED
					previousBuyState = State.EXHAUSTED;
				}
			} 
			else {
				if (previousSellState == null || sellMismatch != null) {
					continue;
				}
				else if (remaining == quantity) { // UNTOUCHED
					if (previousSellState != State.UNTOUCHED) {
						sellMismatch = transaction.getTransactionDateTime();
					}
					previousSellState = State.UNTOUCHED;
				}
				else if (remaining > 0) { // PARTIAL
					if (previousSellState == State.PARTIAL || previousSellState == State.UNTOUCHED) {
						sellMismatch = transaction.getTransactionDateTime();
					}
					previousSellState = State.PARTIAL;
				}
				else { // EXHAUSTED
					previousSellState = State.EXHAUSTED;
				}
			} 
		}
		
		return getEarliest(buyMismatch, sellMismatch);
	}
	
	private Timestamp getEarliest(Timestamp original, Timestamp timestamp) {
		if (original == null) {
			return timestamp;
		}
		else if (timestamp == null) {
			return original;
		}
		else if (original != null && timestamp != null) {
			return original.after(timestamp) ? timestamp : original;
		}
		return null;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	List<WalletTransaction> listTransactionsInAscendingOrder() {
		Session session = database.getCurrentSession();
		return session.createCriteria(WalletTransaction.class)
				.add(Restrictions.eq(WalletTransaction.TYPE_ID, typeId))
				.addOrder(Order.asc(WalletTransaction.TRANSACTION_DATE_TIME))
				.list();
	}
	
	@Transactional
	boolean match(Queue<WalletTransaction> buys, Queue<WalletTransaction> sales) {
		WalletTransaction buyTransaction = buys.peek();
		WalletTransaction sellTransaction = sales.peek();
		
		if (sellTransaction == null) {
			// There are no (valid) sell orders left to process.
			return true;
		}
		
		if (buyTransaction == null) {
			/*
			 *  The buy order queue has been depleted. This
			 *  makes it impossible to match transactions.
			 */
			return true;
		}
		
		if (buyTransaction.beforeOrEquals(sellTransaction)) {
			long buyRemaining = buyTransaction.getRemaining();
			long sellRemaining = sellTransaction.getRemaining();
			
			if (sellRemaining > buyRemaining) {
				buyTransaction.setRemaining(0);
				long diff = sellRemaining - buyRemaining;
				sellTransaction.setRemaining(diff);
				persistTransactionMatch(buyTransaction, sellTransaction, buyRemaining);
			}
			else if (sellRemaining < buyRemaining) {
				long diff = buyRemaining - sellRemaining;
				buyTransaction.setRemaining(diff);
				sellTransaction.setRemaining(0);
				persistTransactionMatch(buyTransaction, sellTransaction, sellRemaining);
			}
			else if (sellRemaining == buyRemaining) {
				buyTransaction.setRemaining(0);
				sellTransaction.setRemaining(0);
				persistTransactionMatch(buyTransaction, sellTransaction, sellRemaining);
			}
		}
		else {
			sellTransaction.setRemaining(0);
		}

		Session session = database.getCurrentSession();
		session.update(buyTransaction);
		session.update(sellTransaction);

		if (buyTransaction.getRemaining() == 0) {
			buys.remove();
		}
		
		if (sellTransaction.getRemaining() == 0) {
			sales.remove();
		}
		
		return false;
	}

	@Transactional
	void persistTransactionMatch(WalletTransaction buy, WalletTransaction sell, long amount) {
		TransactionMatch match = new TransactionMatch();
		match.setKey(new TransactionMatchIdentifier(buy.getTransactionId(), sell.getTransactionId()));
		match.setQuantity(amount);

		database.getCurrentSession().saveOrUpdate(match);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	Queue<WalletTransaction> queryRemainingBuyTransactions() {
		Session session = database.getCurrentSession();
		return new LinkedList<WalletTransaction>(
				session.createCriteria(WalletTransaction.class)
				.add(Restrictions.eq(WalletTransaction.TYPE_ID, typeId))
				.add(Restrictions.gt(WalletTransaction.REMAINING, 0L))
				.add(Restrictions.lt(WalletTransaction.PRICE, BigDecimal.ZERO))
				.addOrder(Order.asc(WalletTransaction.TRANSACTION_DATE_TIME))
				.list()
		);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	Queue<WalletTransaction> queryRemainingSellTransactions() {
		Session session = database.getCurrentSession();
		return new LinkedList<WalletTransaction>(
				session.createCriteria(WalletTransaction.class)
				.add(Restrictions.eq(WalletTransaction.TYPE_ID, typeId))
				.add(Restrictions.gt(WalletTransaction.REMAINING, 0L))
				.add(Restrictions.gt(WalletTransaction.PRICE, BigDecimal.ZERO))
				.addOrder(Order.asc(WalletTransaction.TRANSACTION_DATE_TIME))
				.list()
		);
	}

	private enum State {
		EXHAUSTED, PARTIAL, UNTOUCHED;
	}
	
}
