package nl.minicom.evenexus.inventory;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;

import javax.inject.Inject;

import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.Profit;
import nl.minicom.evenexus.persistence.dao.ProfitIdentifier;
import nl.minicom.evenexus.persistence.dao.WalletTransaction;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class InventoryWorker implements Runnable {

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
		
		final Queue<WalletTransaction> buyTransactions = queryRemainingBuyTransactions();
		final Queue<WalletTransaction> sellTransactions = queryRemainingSellTransactions();
		
		while (!sellTransactions.isEmpty()) {
			if (match(buyTransactions, sellTransactions)) {
				break;
			}
		}
	}
	
	@Transactional
	protected boolean match(Queue<WalletTransaction> buys, Queue<WalletTransaction> sales) {
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
				persistProfit(buyTransaction, sellTransaction, buyRemaining);
			}
			else if (sellRemaining < buyRemaining) {
				long diff = buyRemaining - sellRemaining;
				buyTransaction.setRemaining(diff);
				sellTransaction.setRemaining(0);
				persistProfit(buyTransaction, sellTransaction, sellRemaining);
			}
			else if (sellRemaining == buyRemaining) {
				buyTransaction.setRemaining(0);
				sellTransaction.setRemaining(0);
				persistProfit(buyTransaction, sellTransaction, sellRemaining);
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
	protected void persistProfit(WalletTransaction buyTransaction, WalletTransaction sellTransaction, long amount) {
		Profit profit = new Profit();
		profit.setId(new ProfitIdentifier(buyTransaction.getTransactionId(), sellTransaction.getTransactionId()));
		profit.setDate(sellTransaction.getTransactionDateTime());
		profit.setQuantity(amount);
		profit.setTaxes(buyTransaction.getTaxes().add(sellTransaction.getTaxes()));
		profit.setTypeID(sellTransaction.getTypeId());
		profit.setTypeName(sellTransaction.getTypeName());
		profit.setValue(sellTransaction.getPrice().add(buyTransaction.getPrice()));

		Session session = database.getCurrentSession();
		session.save(profit);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	protected Queue<WalletTransaction> queryRemainingBuyTransactions() {
		Session session = database.getCurrentSession();
		return new LinkedList<WalletTransaction>(
				session.createCriteria(WalletTransaction.class)
				.add(Restrictions.eq("typeId", typeId))
				.add(Restrictions.gt("remaining", 0L))
				.add(Restrictions.lt("price", BigDecimal.ZERO))
				.addOrder(Order.asc("transactionDateTime"))
				.list()
		);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	protected Queue<WalletTransaction> queryRemainingSellTransactions() {
		Session session = database.getCurrentSession();
		return new LinkedList<WalletTransaction>(
				session.createCriteria(WalletTransaction.class)
				.add(Restrictions.eq("typeId", typeId))
				.add(Restrictions.gt("remaining", 0L))
				.add(Restrictions.gt("price", BigDecimal.ZERO))
				.addOrder(Order.asc("transactionDateTime"))
				.list()
		);
	}

}
