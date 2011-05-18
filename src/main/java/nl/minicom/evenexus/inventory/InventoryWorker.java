package nl.minicom.evenexus.inventory;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;

import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.Profit;
import nl.minicom.evenexus.persistence.dao.ProfitIdentifier;
import nl.minicom.evenexus.persistence.dao.WalletTransaction;
import nl.minicom.evenexus.persistence.dao.WalletTransaction.Type;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class InventoryWorker implements Runnable {

	private final long typeId;
	
	public InventoryWorker(long typeId) {
		this.typeId = typeId;
	}

	@Override
	public void run() {
		final Queue<WalletTransaction> remainingBuyTransactions = queryRemainingBuyTransactions();
		final Queue<WalletTransaction> remainingSellTransactions = queryRemainingSellTransactions();
		
		while (!remainingSellTransactions.isEmpty()) {
			boolean breakWhileLoop = new Query<Boolean>() {
				@Override
				protected Boolean doQuery(Session session) {
					WalletTransaction buyTransaction = remainingBuyTransactions.peek();
					WalletTransaction sellTransaction = remainingSellTransactions.peek();
					
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
							persistProfit(buyTransaction, sellTransaction, buyRemaining, session);
						}
						else if (sellRemaining < buyRemaining) {
							long diff = buyRemaining - sellRemaining;
							buyTransaction.setRemaining(diff);
							sellTransaction.setRemaining(0);
							persistProfit(buyTransaction, sellTransaction, sellRemaining, session);
						}
						else if (sellRemaining == buyRemaining) {
							buyTransaction.setRemaining(0);
							sellTransaction.setRemaining(0);
							persistProfit(buyTransaction, sellTransaction, sellRemaining, session);
						}
					}
					else {
						sellTransaction.setRemaining(0);
					}

					session.update(buyTransaction);
					session.update(sellTransaction);

					if (buyTransaction.getRemaining() == 0) {
						remainingBuyTransactions.remove();
					}
					if (sellTransaction.getRemaining() == 0) {
						remainingSellTransactions.remove();
					}
					return false;
				}
			}.doQuery();
			
			if (breakWhileLoop) {
				break;
			}
		}
	}

	private void persistProfit(WalletTransaction buyTransaction, WalletTransaction sellTransaction, long amount, Session session) {
		Profit profit = new Profit();
		profit.setId(new ProfitIdentifier(buyTransaction.getTransactionId(), sellTransaction.getTransactionId()));
		profit.setDate(sellTransaction.getTransactionDateTime());
		profit.setQuantity(amount);
		profit.setTaxes(buyTransaction.getTaxes().add(sellTransaction.getTaxes()));
		profit.setTypeID(sellTransaction.getTypeId());
		profit.setTypeName(sellTransaction.getTypeName());
		profit.setValue(sellTransaction.getPrice().add(buyTransaction.getPrice()));
		session.save(profit);
	}

	private Queue<WalletTransaction> queryRemainingBuyTransactions() {
		return queryRemainingOrders(Type.BUY);
	}
	private Queue<WalletTransaction> queryRemainingSellTransactions() {
		return queryRemainingOrders(Type.SELL);
	}

	@SuppressWarnings("unchecked")
	private Queue<WalletTransaction> queryRemainingOrders(final Type type) {
		return new Query<Queue<WalletTransaction>>() {
			@Override
			protected Queue<WalletTransaction> doQuery(Session session) {
				if (type == Type.BUY) {
					return new LinkedList<WalletTransaction>(
						session.createCriteria(WalletTransaction.class)
						.add(Restrictions.eq("typeId", typeId))
						.add(Restrictions.gt("remaining", 0L))
						.add(Restrictions.lt("price", BigDecimal.ZERO))
						.addOrder(Order.asc("transactionDateTime"))
						.list()
					);
				}
				else if (type == Type.SELL) {
					return new LinkedList<WalletTransaction>(
						session.createCriteria(WalletTransaction.class)
						.add(Restrictions.eq("typeId", typeId))
						.add(Restrictions.gt("remaining", 0L))
						.add(Restrictions.gt("price", BigDecimal.ZERO))
						.addOrder(Order.asc("transactionDateTime"))
						.list()
					);
				}
				return null;
			}
		}.doQuery();
	}

}
