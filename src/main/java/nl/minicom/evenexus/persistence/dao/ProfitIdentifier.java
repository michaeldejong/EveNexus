package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProfitIdentifier implements Serializable, Comparable<ProfitIdentifier> {

	private static final long serialVersionUID = -6237540725758955743L;

	@Column(name = "buytransactionid", nullable = false)
	private long buyTransactionId;
	
	@Column(name = "selltransactionid", nullable = false)
	private long sellTransactionId;
	
	public ProfitIdentifier() { }
	
	public ProfitIdentifier(long buyTransactionId, long sellTransactionId) {
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
		if (other instanceof ProfitIdentifier) {
			ProfitIdentifier otherId = (ProfitIdentifier) other;
			return 	buyTransactionId == otherId.buyTransactionId && 
					sellTransactionId == otherId.sellTransactionId;
		}
		return false;
	}
	
	@Override
	public final int hashCode() {
		return (int) ((buyTransactionId * 5 + sellTransactionId * 19 + 21) % Integer.MAX_VALUE);
	}

	@Override
	public int compareTo(ProfitIdentifier o) {
		long diff = buyTransactionId - o.buyTransactionId;
		if (diff != 0) {
			return diff < 0 ? -1 : 1;
		}
		
		return (sellTransactionId - o.sellTransactionId) < 0 ? -1 : 1;
	}
	
}
