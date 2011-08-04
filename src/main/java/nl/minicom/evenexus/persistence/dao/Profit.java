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
import org.hibernate.annotations.Formula;

/**
 * Entity representing matching fulfilled buy and sell orders.
 * 
 * @author Michael
 * @author Lars
 */
@Entity
@Table(name = "profit")
public class Profit implements Serializable {

	private static final long serialVersionUID = 6583278267993803229L;

	public static final String ID = "id";
	public static final String TYPE_ID = "typeid";
	public static final String TYPE_NAME = "typename";
	public static final String DATE = "date";
	public static final String QUANTITY = "quantity";
	// TODO this could be removed - leaving it due to ignorance of compatibility issues
	public static final String VALUE = "value";
	public static final String TAXES = "taxes";
	public static final String BUY_PRICE = "buyprice";
	public static final String SELL_PRICE = "sellprice";
	
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

	// TODO this could be removed - leaving it due to ignorance of compatibility issues
	@Column(name = VALUE)
	private BigDecimal value;

	@Column(name = TAXES)
	private BigDecimal taxes;
	
	@Column(name = BUY_PRICE)
	private BigDecimal buyPrice;
	
	@Column(name = SELL_PRICE)
	private BigDecimal sellPrice;

	/*
	 * The following fields can be calculated from the ones above. I chose to do this at database
	 * level (not in the object itself), so those fields could be used in hql queries (at least I
	 * hope so). On the negative side those values aren't available when creating new profit
	 * instances.  
	 */
	
	@Formula(BUY_PRICE + " + " + SELL_PRICE)
	private BigDecimal grossProfit;

	@Formula(BUY_PRICE + " + " + SELL_PRICE + " + " + TAXES)
	private BigDecimal netProfit;

	@Formula("(" + BUY_PRICE + " + " + SELL_PRICE + ") * " + QUANTITY)
	private BigDecimal totalGrossProfit;

	@Formula("(" + BUY_PRICE + " + " + SELL_PRICE + " + " + TAXES + ") * " + QUANTITY)
	private BigDecimal totalNetProfit;

	@Formula("(" + BUY_PRICE + " + " + SELL_PRICE + " + " + TAXES + ") / " + BUY_PRICE  + " * 100")
	private double percentalGrossProfit;

	@Formula("(" + BUY_PRICE + " + " + SELL_PRICE + ") / " + BUY_PRICE + " * 100")
	private double percentalNetProfit;

	public ProfitIdentifier getId() {
		return id;
	}

	public void setId(ProfitIdentifier id) {
		this.id = id;
	}

	/**
	 * Answer the internal typeId identifying the item.
	 */
	public long getTypeID() {
		return typeID;
	}

	public void setTypeID(long typeID) {
		this.typeID = typeID;
	}

	/**
	 * Answer the item's name.
	 */
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * Answer the timestamp when the profit was made.
	 */
	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	/**
	 * Answer the number of items involved in this profit.
	 */
	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	// TODO this could be removed - leaving it due to ignorance of compatibility issues
	public BigDecimal getValue() {
		return value;
	}

	// TODO this could be removed - leaving it due to ignorance of compatibility issues
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * Answer the amount of taxes per item.
	 */
	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}
	
	/**
	 * Answer the buy price for one item.
	 */
	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	/**
	 * Answer the sell price for one item.
	 */
	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * Answer the profit per item without taxes.
	 */
	public BigDecimal getGrossProfit() {
		return grossProfit;
	}
	
	public void setGrossProfit(BigDecimal grossProfit) {
		this.grossProfit = grossProfit;
	}
	
	/**
	 * Answer the profit per item including taxes.
	 */
	public BigDecimal getNetProfit() {
		return netProfit;
	}
	
	public void setNetProfit(BigDecimal netProfit) {
		this.netProfit = netProfit;
	}

	/**
	 * Answer the total profit without taxes.
	 */
	public BigDecimal getTotalGrossProfit()
	{
		return totalGrossProfit;
	}
	
	public void setTotalGrossProfit(BigDecimal totalGrossProfit) {
		this.totalGrossProfit = totalGrossProfit;
	}
	
	/**
	 * Answer the total profit including taxes.
	 */
	public BigDecimal getTotalNetProfit()
	{
		return totalNetProfit;
	}
	
	public void setTotalNetProfit(BigDecimal totalNetProfit) {
		this.totalNetProfit = totalNetProfit;
	}

	/**
	 * Answer the profit in percent including taxes.
	 */
	public double getPercentalGrossProfit() {
		return percentalGrossProfit;
	}

	public void setPercentalGrossProfit(double percentalGrossProfit) {
		this.percentalGrossProfit = percentalGrossProfit;
	}
	
	/**
	 * Answer the profit in percent without taxes.
	 */
	public double getPercentalNetProfit() {
		return percentalNetProfit;
	}
	
	public void setPercentalNetProfit(double percentalNetProfit) {
		this.percentalNetProfit = percentalNetProfit;
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
			.appendSuper(value.doubleValue() == profit.value.doubleValue())
			.appendSuper(taxes.doubleValue() == profit.taxes.doubleValue())
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
