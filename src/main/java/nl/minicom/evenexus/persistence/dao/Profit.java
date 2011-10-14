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

	/**
	 * @return
	 * 		The {@link ProfitIdentifier} of this object.
	 */
	public ProfitIdentifier getId() {
		return id;
	}

	/**
	 * This method sets the id of this DAO.
	 * 
	 * @param id
	 * 		The new id of this object.
	 */
	public void setId(ProfitIdentifier id) {
		this.id = id;
	}

	/**
	 * @return 
	 * 		The internal typeId identifying the item.
	 */
	public long getTypeID() {
		return typeID;
	}

	/**
	 * This method sets the typeID of this {@link Profit} DAO.
	 * 
	 * @param typeID
	 * 		The new typeID.
	 */
	public void setTypeID(long typeID) {
		this.typeID = typeID;
	}

	/**
	 * @return 
	 * 		The item's name.
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * This sets the item's name.
	 * 
	 * @param typeName
	 * 		The new item name.
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return 
	 * 		The timestamp when the profit was made.
	 */
	public Timestamp getDate() {
		return date;
	}

	/**
	 * This method sets the date.
	 * 
	 * @param date
	 * 		The new date of the {@link Profit} DAO.
	 */
	public void setDate(Timestamp date) {
		this.date = date;
	}

	/**
	 * @return 
	 * 		The number of items involved in this profit.
	 */
	public long getQuantity() {
		return quantity;
	}

	/**
	 * This method sets a new quantity.
	 * 
	 * @param quantity
	 * 		The new quantity.
	 */
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return 
	 * 		The amount of taxes per item.
	 */
	public BigDecimal getTaxes() {
		return taxes;
	}

	/**
	 * This method sets the taxes.
	 * 
	 * @param taxes
	 * 		The new taxes for this item.
	 */
	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}
	
	/**
	 * @return
	 * 		The buy price for one item.
	 */
	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	/**
	 * This method sets the new buy price.
	 * 
	 * @param buyPrice
	 * 		The new buy price.
	 */
	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	/**
	 * @return
	 *		The sell price for one item.
	 */
	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	/**
	 * This method sets the sell price.
	 * 
	 * @param sellPrice
	 * 		The new sell price.
	 */
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * @return 
	 * 		The profit per item without taxes.
	 */
	public BigDecimal getGrossProfit() {
		return grossProfit;
	}
	
	/**
	 * This method sets the gross profit.
	 * 
	 * @param grossProfit
	 * 		The new gross profit.
	 */
	public void setGrossProfit(BigDecimal grossProfit) {
		this.grossProfit = grossProfit;
	}
	
	/**
	 * @return
	 * 		The profit per item including taxes.
	 */
	public BigDecimal getNetProfit() {
		return netProfit;
	}
	
	/**
	 * This method sets the net profit.
	 * 
	 * @param netProfit
	 * 		The new net profit.
	 */
	public void setNetProfit(BigDecimal netProfit) {
		this.netProfit = netProfit;
	}

	/**
	 * @return
	 * 		The amount of total taxes paid.
	 */
	public BigDecimal getTotalTaxes() {
		return totalTaxes;
	}

	/**
	 * This method sets the total taxes.
	 * 
	 * @param totalTaxes
	 * 		The new total taxes.
	 */
	public void setTotalTaxes(BigDecimal totalTaxes) {
		this.totalTaxes = totalTaxes;
	}

	/**
	 * @return 
	 * 		The total profit without taxes.
	 */
	public BigDecimal getTotalGrossProfit() {
		return totalGrossProfit;
	}
	
	/**
	 * This method sets the total gross profit.
	 * 
	 * @param totalGrossProfit
	 * 		The new total gross profit.
	 */
	public void setTotalGrossProfit(BigDecimal totalGrossProfit) {
		this.totalGrossProfit = totalGrossProfit;
	}
	
	/**
	 * @return
	 * 		The total profit including taxes.
	 */
	public BigDecimal getTotalNetProfit() {
		return totalNetProfit;
	}
	
	/**
	 * This method sets the total net profit.
	 * 
	 * @param totalNetProfit
	 * 		The new total net profit.
	 */
	public void setTotalNetProfit(BigDecimal totalNetProfit) {
		this.totalNetProfit = totalNetProfit;
	}

	/**
	 * @return
	 * 		The profit in percent including taxes.
	 */
	public double getPercentalGrossProfit() {
		return percentalGrossProfit;
	}

	/**
	 * This method sets the percental gross profit.
	 * 
	 * @param percentalGrossProfit
	 * 		The new percental gross profit.
	 */
	public void setPercentalGrossProfit(double percentalGrossProfit) {
		this.percentalGrossProfit = percentalGrossProfit;
	}
	
	/**
	 * @return
	 * 		The profit in percent without taxes.
	 */
	public double getPercentalNetProfit() {
		return percentalNetProfit;
	}
	
	/**
	 * This method sets the percental net profit.
	 * 
	 * @param percentalNetProfit
	 * 		The new percental net profit.
	 */
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
	
	@Override
	public String toString() {
		return "<Profit buyId=" + id.getBuyTransactionId() + " sellId=" + id.getSellTransactionId() + ">";
	}
}
