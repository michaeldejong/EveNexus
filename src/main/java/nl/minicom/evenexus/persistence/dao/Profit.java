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
import org.hibernate.annotations.Immutable;

/**
 * Entity representing matching fulfilled buy and sell orders.
 * 
 * @author Michael
 * @author Lars
 */
@Entity
@Immutable
@Table(name = "profits")
public class Profit implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TYPE_ID = "typeId";
	public static final String TYPE_NAME = "typeName";
	public static final String DATE = "date";
	public static final String QUANTITY = "quantity";
	public static final String BUY_PRICE = "buyPrice";
	public static final String SELL_PRICE = "sellPrice";
	public static final String TAXES = "taxes";
	public static final String GROSS_PROFIT = "grossProfit";
	public static final String NET_PROFIT = "netProfit";
	public static final String TOTAL_TAXES = "totalTaxes";
	public static final String TOTAL_GROSS_PROFIT = "totalGrossProfit";
	public static final String TOTAL_NET_PROFIT = "totalNetProfit";
	public static final String PERCENTAL_GROSS_PROFIT = "percentalGrossProfit";
	public static final String PERCENTAL_NET_PROFIT = "percentalNetProfit";
	
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

	@Column(name = BUY_PRICE)
	private BigDecimal buyPrice;
	
	@Column(name = SELL_PRICE)
	private BigDecimal sellPrice;

	@Column(name = TAXES)
	private BigDecimal taxes;
	
	@Column(name = GROSS_PROFIT)
	private BigDecimal grossProfit;

	@Column(name = NET_PROFIT)
	private BigDecimal netProfit;

	@Column(name = TOTAL_TAXES)
	private BigDecimal totalTaxes;
	
	@Column(name = TOTAL_GROSS_PROFIT)
	private BigDecimal totalGrossProfit;

	@Column(name = TOTAL_NET_PROFIT)
	private BigDecimal totalNetProfit;

	@Column(name = PERCENTAL_GROSS_PROFIT)
	private double percentalGrossProfit;

	@Column(name = PERCENTAL_NET_PROFIT)
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
	 * Answer the amount of total taxes paid.
	 */
	public BigDecimal getTotalTaxes() {
		return totalTaxes;
	}

	public void setTotalTaxes(BigDecimal totalTaxes) {
		this.totalTaxes = totalTaxes;
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

	@Override
	public boolean equals(Object other) {
		if (other instanceof Profit) {
			Profit profit = (Profit) other;
			
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
	
	public String toString() {
		return "<Profit buyId=" + id.getBuyTransactionId() + " sellId=" + id.getSellTransactionId() + ">";
	}
}
