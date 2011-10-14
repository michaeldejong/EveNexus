package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Entity representing matching fulfilled buy and sell orders.
 * 
 * @author Michael
 * @author Lars
 */
@Entity
@Table(name = "transactionmatches")
public class TransactionMatch implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String KEY = "key";
	public static final String QUANTITY = "quantity";
	
	@Id
	private TransactionMatchIdentifier key;

	@Column(name = QUANTITY)
	private long quantity;

	/**
	 * @return
	 * 		The id of this object.
	 */
	public TransactionMatchIdentifier getKey() {
		return key;
	}

	/**
	 * This method sets the id of this object.
	 * 
	 * @param key
	 * 		The new id of this object.
	 */
	public void setKey(TransactionMatchIdentifier key) {
		this.key = key;
	}

	/**
	 * @return
	 * 		The number of items matching those transactions.
	 */
	public long getQuantity() {
		return quantity;
	}

	/**
	 * This method sets the quantity.
	 * 
	 * @param quantity
	 * 		The new quantity of this object.
	 */
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * @return
	 * 		The quantity of this object.
	 */
	public long getBuyTransactionId() {
		return key.getBuyTransactionId();
	}
	
	/**
	 * @return
	 * 		The sell transaction's id.
	 */
	public long getSellTransactionId() {
		return key.getSellTransactionId();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof TransactionMatch) {
			TransactionMatch profit = (TransactionMatch) other;
			
			return new EqualsBuilder()
			.append(key, profit.key)
			.isEquals();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(key)
			.toHashCode();
	}
	
}
