package nl.minicom.evenexus.inventory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

import nl.minicom.evenexus.persistence.Database;
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

public class InventoryWorker implements Runnable {

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
	public void run() {
		if (typeId < 0) {
			throw new IllegalStateException("InventoryWorker not yet initialized!");
		}
		
		// Drop transactionMatches of the past days for this typeId, and reset remaining flag for wallet transactions.
		revertMostRecentTransactionsIfRequired();
		
		final Queue<WalletTransaction> buyTransactions = queryRemainingBuyTransactions();
		final Queue<WalletTransaction> sellTransactions = queryRemainingSellTransactions();
		
		while (!sellTransactions.isEmpty()) {
			if (match(buyTransactions, sellTransactions)) {
				break;
			}
		}
	}
	
	@Transactional
	void revertMostRecentTransactionsIfRequired() {
		Session session = database.getCurrentSession();
		
		Timestamp timestamp = getEarliestMatchErrorTimestamp(session);
		if (timestamp != null) {
			List<TransactionMatch> matches = listInvalidMatches(timestamp, session);
			LOG.info("Found " + matches.size() + " invalid matches for type: " + typeId + ", now de-matching...");
			
			for (TransactionMatch match : matches) {
				WalletTransaction buy = getWalletTransaction(match.getBuyTransactionId());
				WalletTransaction sell = getWalletTransaction(match.getSellTransactionId());
				
				buy.setRemaining(Math.min(buy.getQuantity(), buy.getRemaining() + match.getQuantity()));
				sell.setRemaining(Math.min(sell.getQuantity(), sell.getRemaining() + match.getQuantity()));
				
				session.update(buy);
				session.update(sell);
				session.delete(match);
			}
			
			LOG.info("Finished de-matching " + matches.size() + " invalid matches for type: " + typeId + ".");
		}
	}
	
	/**
	 * This method retrieves a certain {@link WalletTransaction} object from the database.
	 * 
	 * @param id
	 * 		The id of the {@link WalletTransaction}.
	 * 
	 * @return
	 * 		The retrieved {@link WalletTransaction}.
	 */
	@Transactional
	WalletTransaction getWalletTransaction(long id) {
		Session session = database.getCurrentSession();
		return (WalletTransaction) session.get(WalletTransaction.class, id);
	}
	
	@SuppressWarnings("unchecked")
	private List<TransactionMatch> listInvalidMatches(Timestamp timestamp, Session session) {
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
		
		Query q = session.createQuery(query.toString());
		q.setTimestamp(0, timestamp);
		q.setTimestamp(1, timestamp);
		
		return (List<TransactionMatch>) q.list();
	}

	@SuppressWarnings("unchecked")
	private Timestamp getEarliestMatchErrorTimestamp(Session session) {
		List<WalletTransaction> transactions = session.createCriteria(WalletTransaction.class)
				.add(Restrictions.eq(WalletTransaction.TYPE_ID, typeId))
				.addOrder(Order.desc(WalletTransaction.TRANSACTION_DATE_TIME))
				.list();
		
		Timestamp result = null;
		
		boolean buyTransactionsWithoutStock = false;
		boolean sellTransactionsWithoutStock = false;
		
		for (WalletTransaction transaction : transactions) {
			Timestamp time = transaction.getTransactionDateTime();
			if (transaction.isBuy()) {
				if (buyTransactionsWithoutStock && transaction.getRemaining() > 0) {
					result = getEarliest(result, time);
				}
				else if (transaction.getRemaining() == 0) {
					buyTransactionsWithoutStock = true;
				}
			} 
			else {
				if (sellTransactionsWithoutStock && transaction.getRemaining() > 0) {
					result = getEarliest(result, time);
				}
				else if (transaction.getRemaining() == 0) {
					sellTransactionsWithoutStock = true;
				}
			}
		}
		
		return result;
	}
	
	private Timestamp getEarliest(Timestamp result, Timestamp time) {
		if (result == null || time.before(result)) {
			return time;
		}
		return result;
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
		
		if (buyTransaction.beforeOrSimultaniously(sellTransaction)) {
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

}
