package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * A {@link TransactionMatch} is identified by the ids of the buy transaction and sell transaction.   
 * 
 * @author Lars
 */
@Embeddable
public class TransactionMatchIdentifier implements Serializable, Comparable<TransactionMatchIdentifier> {

	private static final long serialVersionUID = 1L;

	public static final String BUY_TRANSACTION_ID = "buy_transaction_id";
	public static final String SELL_TRANSACTION_ID = "sell_transaction_id";
	
	@Column(name = BUY_TRANSACTION_ID, nullable = false)
	private long buyTransactionId;
	
	@Column(name = SELL_TRANSACTION_ID, nullable = false)
	private long sellTransactionId;
	
	public TransactionMatchIdentifier() {
		// Do nothing.
	}
	
	public TransactionMatchIdentifier(long buyTransactionId, long sellTransactionId) {
		this.buyTransactionId = buyTransactionId;
		this.sellTransactionId = sellTransactionId;
	}
	
	public long getBuyTransactionId() {
		return buyTransactionId;
	}

	public void setBuyTransactionId(long transactionId) {
		this.buyTransactionId = transactionId;
	}
	
	public long getSellTransactionId() {
		return sellTransactionId;
	}

	public void setSellTransactionId(long transactionId) {
		this.sellTransactionId = transactionId;
	}
	
	@Override
	public final boolean equals(Object other) {
		if (other instanceof TransactionMatchIdentifier) {
			TransactionMatchIdentifier otherId = (TransactionMatchIdentifier) other;
			return buyTransactionId == otherId.buyTransactionId 
				&& sellTransactionId == otherId.sellTransactionId;
		}
		return false;
	}
	
	@Override
	public final int hashCode() {
		return (int) ((buyTransactionId * 5 + sellTransactionId * 19 + 21) % Integer.MAX_VALUE);
	}

	@Override
	public int compareTo(TransactionMatchIdentifier o) {
		long diff = buyTransactionId - o.buyTransactionId;
		if (diff < 0) {
			return -1;
		}
		else if (diff > 0) {
			return 1;
		}
		
		if ((sellTransactionId - o.sellTransactionId) < 0) {
			return -1;
		}
		return 1;
	}
	
}
