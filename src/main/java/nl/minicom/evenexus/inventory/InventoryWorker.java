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

/**
 * This class is responsible for quering the {@link Database} for all {@link WalletTransaction}s 
 * related to a specific item.
 * 
 * @author michael
 */
public class InventoryWorker implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(InventoryWorker.class);
	
	private final Database database;
	
	private long typeId = -1;
	
	/**
	 * This constructs a new {@link InventoryWorker} object.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 */
	@Inject
	public InventoryWorker(Database database) {
		this.database = database;
	}
	
	/**
	 * This method initializes the {@link InventoryWorker} to only work on a specific item.
	 * 
	 * @param typeId
	 * 		The id of the item to look for.
	 */
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
	
	private void revertMostRecentTransactionsIfRequired() {
		Timestamp timestamp = getEarliestMatchErrorTimestamp();
		if (timestamp != null) {
			List<TransactionMatch> matches = listInvalidMatches(timestamp);
			LOG.info("Found " + matches.size() + " invalid matches for type: " + typeId + ", now de-matching...");
			
			for (TransactionMatch match : matches) {
				revertMatch(match);
			}
			
			LOG.info("Finished de-matching " + matches.size() + " invalid matches for type: " + typeId + ".");
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
		Timestamp result = null;
		boolean buyTransactionsWithoutStock = false;
		boolean sellTransactionsWithoutStock = false;
		
		List<WalletTransaction> transactions = listTransactionsInDescendingOrder();
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

	@Transactional
	@SuppressWarnings("unchecked")
	List<WalletTransaction> listTransactionsInDescendingOrder() {
		Session session = database.getCurrentSession();
		return session.createCriteria(WalletTransaction.class)
				.add(Restrictions.eq(WalletTransaction.TYPE_ID, typeId))
				.addOrder(Order.desc(WalletTransaction.TRANSACTION_DATE_TIME))
				.list();
	}
	
	private Timestamp getEarliest(Timestamp result, Timestamp time) {
		if (result == null || time.before(result)) {
			return time;
		}
		return result;
	}

	/**
	 * This method tries to match two {@link Queue}s of {@link WalletTransaction}s.
	 * 
	 * @param buys
	 * 		A {@link Queue} of buy {@link WalletTransaction}s.
	 * 
	 * @param sales
	 * 		A {@link Queue} of sell {@link WalletTransaction}s.
	 * 
	 * @return
	 * 		True if a match was made and processed.
	 */
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

	/**
	 * This method persists a transaction match to the database.
	 * 
	 * @param buy
	 * 		The buy {@link WalletTransaction}.
	 * 
	 * @param sell
	 * 		The sell {@link WalletTransaction}.
	 * 
	 * @param amount
	 * 		The amount of items which were matched.
	 */
	@Transactional
	void persistTransactionMatch(WalletTransaction buy, WalletTransaction sell, long amount) {
		TransactionMatch match = new TransactionMatch();
		match.setKey(new TransactionMatchIdentifier(buy.getTransactionId(), sell.getTransactionId()));
		match.setQuantity(amount);

		database.getCurrentSession().saveOrUpdate(match);
	}

	/**
	 * This method queries the buy {@link WalletTransaction}s for the specific item we're looking for, and
	 * which has remaining items.
	 * 
	 * @return
	 * 		A {@link Queue} of the specified {@link WalletTransaction}s.
	 */
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

	/**
	 * This method queries the sell {@link WalletTransaction}s for the specific item we're looking for, and
	 * which has remaining items.
	 * 
	 * @return
	 * 		A {@link Queue} of the specified {@link WalletTransaction}s.
	 */
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
