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

	public static final String BUY_TRANSACTION_ID = "buyTransactionId";
	public static final String SELL_TRANSACTION_ID = "sellTransactionId";
	
	@Column(name = BUY_TRANSACTION_ID, nullable = false)
	private long buyTransactionId;
	
	@Column(name = SELL_TRANSACTION_ID, nullable = false)
	private long sellTransactionId;
	
	@SuppressWarnings("unused")
	private TransactionMatchIdentifier() {
		// Private no-args constructor for serialization.
	}
	
	/**
	 * This constructs a new {@link TransactionMatchIdentifier}.
	 * 
	 * @param buyTransactionId
	 * 		The id of the buy {@link WalletTransaction}.
	 * 
	 * @param sellTransactionId
	 * 		The id of the sell {@link WalletTransaction}.
	 */
	public TransactionMatchIdentifier(long buyTransactionId, long sellTransactionId) {
		this.buyTransactionId = buyTransactionId;
		this.sellTransactionId = sellTransactionId;
	}
	
	/**
	 * @return
	 * 		The id of the buy {@link WalletTransaction}.
	 */
	public long getBuyTransactionId() {
		return buyTransactionId;
	}

	/**
	 * This method sets the id of the buy {@link WalletTransaction}.
	 * 
	 * @param transactionId
	 * 		The id of the buy {@link WalletTransaction}.
	 */
	public void setBuyTransactionId(long transactionId) {
		this.buyTransactionId = transactionId;
	}
	
	/**
	 * @return
	 * 		The id of the sell {@link WalletTransaction}.
	 */
	public long getSellTransactionId() {
		return sellTransactionId;
	}

	/**
	 * This method sets the id of the sell {@link WalletTransaction}.
	 * 
	 * @param transactionId
	 * 		The id of the sell {@link WalletTransaction}.
	 */
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
