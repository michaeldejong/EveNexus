package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "profit")
public class Profit implements Serializable {

	private static final long serialVersionUID = 6583278267993803229L;

	@Id
	private ProfitIdentifier id;

	@Column(name = "typeid")
	private long typeID;

	@Column(name = "typename")
	private String typeName;

	@Column(name = "date")
	private Timestamp date;

	@Column(name = "quantity")
	private long quantity;

	@Column(name = "value")
	private BigDecimal value;

	@Column(name = "taxes")
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
			
			return id.equals(profit.id) 
				&& typeID == profit.typeID 
				&& typeName.equals(profit.typeName) 
				&& date.equals(profit.date) 
				&& quantity == profit.quantity 
				&& value.compareTo(profit.value) == 0 
				&& taxes.compareTo(profit.taxes) == 0;
		}
		return false;
	}
	
}
