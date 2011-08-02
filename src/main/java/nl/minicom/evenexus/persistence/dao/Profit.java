package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name = "profit")
public class Profit implements Serializable {

	private static final long serialVersionUID = 6583278267993803229L;

	public static final String ID = "id";
	public static final String TYPE_ID = "typeid";
	public static final String TYPE_NAME = "typename";
	public static final String DATE = "date";
	public static final String QUANTITY = "quantity";
	public static final String VALUE = "value";
	public static final String TAXES = "taxes";
	
	@Id
	private ProfitIdentifier id;

	@Column(name = TYPE_ID)
	private long typeID;

	@Column(name = TYPE_NAME)
	private String typeName;

	@Column(name = DATE)
	private Timestamp date;

	@Column(name = QUANTITY)
	private long quantity;

	@Column(name = VALUE)
	private BigDecimal value;

	@Column(name = TAXES)
	private BigDecimal taxes;
	
	public ProfitIdentifier getId() {
		return id;
	}

	public void setId(ProfitIdentifier id) {
		this.id = id;
	}

	public long getTypeID() {
		return typeID;
	}

	public void setTypeID(long typeID) {
		this.typeID = typeID;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}
	
	public boolean equals(Object other) {
		if (other instanceof Profit) {
			Profit profit = (Profit) other;
			
			return new EqualsBuilder()
			.append(id, profit.id)
			.append(typeID, profit.typeID)
			.append(typeName, profit.typeName)
			.append(date, profit.date)
			.append(quantity, profit.quantity)
			.append(value, profit.value)
			.append(taxes, profit.taxes)
			.isEquals();
		}
		return false;
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(id)
			.append(typeID)
			.append(typeName)
			.append(date)
			.append(quantity)
			.append(value)
			.append(taxes)
			.toHashCode();
	}
	
}
