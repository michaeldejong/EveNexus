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
@Table(name = "transaction_matches")
public class TransactionMatch implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ID = "id";
	public static final String QUANTITY = "quantity";
	
	@Id
	private TransactionMatchIdentifier id;

	@Column(name = QUANTITY)
	private long quantity;

	public TransactionMatchIdentifier getId() {
		return id;
	}

	public void setId(TransactionMatchIdentifier id) {
		this.id = id;
	}

	/**
	 * Answer the number of items matching those transactions.
	 */
	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof TransactionMatch) {
			TransactionMatch profit = (TransactionMatch) other;
			
			return new EqualsBuilder()
			.append(id, profit.id)
			.isEquals();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(id)
			.toHashCode();
	}
}
