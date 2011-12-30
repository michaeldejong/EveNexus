package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity represent all the data on a market order.
 *
 * @author michael
 */
@Entity
@Table(name = "marketorders")
public class MarketOrder implements Serializable {
	
	private static final long serialVersionUID = 7476899993373397147L;
	
	public static final String ORDER_ID = "orderId";
	public static final String CHAR_ID = "charId";
	public static final String STATION_ID = "stationId";
	public static final String VOL_ENTERED = "volEntered";
	public static final String VOL_REMAINING = "volRemaining";
	public static final String MIN_VOLUME = "minVolume";
	public static final String ORDER_STATE = "orderState";
	public static final String TYPE_ID = "typeId";
	public static final String RANGE = "range";
	public static final String ACCOUNT_KEY = "accountKey";
	public static final String DURATION = "duration";
	public static final String ESCROW = "escrow";
	public static final String PRICE = "price";
	public static final String BID = "bid";
	public static final String ISSUED = "issued";
	
	@Id
	@Column(name = ORDER_ID)
	private long orderId;

	@Column(name = CHAR_ID)
	private long charId;

	@Column(name = STATION_ID)
	private long stationId;

	@Column(name = VOL_ENTERED)
	private long volEntered;

	@Column(name = VOL_REMAINING)
	private long volRemaining;

	@Column(name = MIN_VOLUME)
	private long minVolume;

	@Column(name = ORDER_STATE)
	private int orderState;

	@Column(name = TYPE_ID)
	private long typeId;

	@Column(name = RANGE)
	private int range;

	@Column(name = ACCOUNT_KEY)
	private int accountKey;

	@Column(name = DURATION)
	private int duration;

	@Column(name = ESCROW)
	private BigDecimal escrow;

	@Column(name = PRICE)
	private BigDecimal price;

	@Column(name = BID)
	private boolean bid;

	@Column(name = ISSUED)
	private Timestamp issued;

	/**
	 * @return
	 * 		The id of this {@link MarketOrder}.
	 */
	public long getOrderId() {
		return orderId;
	}

	/**
	 * This method sets the id of this {@link MarketOrder}.
	 * 
	 * @param orderId
	 * 		The id of this {@link MarketOrder}.
	 */
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * @return
	 * 		The id of the character who placed this {@link MarketOrder}.
	 */
	public long getCharacterId() {
		return charId;
	}

	/**
	 * This method sets the id of the character who placed this {@link MarketOrder}.
	 * 
	 * @param charId
	 * 		The id of the character.
	 */
	public void setCharacterId(long charId) {
		this.charId = charId;
	}

	/**
	 * @return
	 * 		The id of the station in which this {@link MarketOrder} was placed.
	 */
	public long getStationId() {
		return stationId;
	}

	/**
	 * This method sets the id of the station in which this {@link MarketOrder} was placed.
	 * 
	 * @param stationId
	 * 		The id of the station.
	 */
	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	/**
	 * @return
	 * 		The amount of items originally put on sale or requested to buy.
	 */
	public long getVolumeEntered() {
		return volEntered;
	}

	/**
	 * This method sets the amount of items originally put on sale or request to buy.
	 * 
	 * @param volumeEntered
	 * 		The amount of items.
	 */
	public void setVolumeEntered(long volumeEntered) {
		this.volEntered = volumeEntered;
	}

	/**
	 * @return
	 * 		The amount of items still for sale, or requested to buy.
	 */
	public long getVolumeRemaining() {
		return volRemaining;
	}

	/**
	 * This method sets the amount of items still for sale, or requested to buy.
	 * 
	 * @param volumeRemaining
	 * 		The amount of items.
	 */
	public void setVolumeRemaining(long volumeRemaining) {
		this.volRemaining = volumeRemaining;
	}

	/**
	 * @return
	 * 		The minimum amount of items we wish to purchase per transaction. 
	 * 		This is 1 by default for sell transactions.
	 */
	public long getMinimumVolume() {
		return minVolume;
	}

	/**
	 * This method sets the minimum volume of this {@link MarketOrder}.
	 * 
	 * @param minimumVolume
	 * 		The minimum volume.
	 */
	public void setMinimumVolume(long minimumVolume) {
		this.minVolume = minimumVolume;
	}

	/**
	 * @return
	 * 		The status of the order.
	 */
	public int getOrderState() {
		return orderState;
	}

	/**
	 * This method sets the status of the order.
	 * 
	 * @param orderState
	 * 		The state of the order.
	 */
	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	/**
	 * @return
	 * 		The id of the item we want to buy or sell.
	 */
	public long getTypeId() {
		return typeId;
	}

	/**
	 * This method sets the id of the item we wish to sell or buy.
	 * 
	 * @param typeId
	 * 		The id of the item.
	 */
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return
	 * 		The range of this {@link MarketOrder} in jumps.
	 */
	public int getRange() {
		return range;
	}

	/**
	 * This method sets the range (in jumps) of this {@link MarketOrder}.
	 * 
	 * @param range
	 * 		The new range of this {@link MarketOrder}.
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * @return
	 * 		The account key which belongs to this {@link MarketOrder}.
	 */
	public int getAccountKey() {
		return accountKey;
	}

	/**
	 * This method sets the account key of this {@link MarketOrder}.
	 * 
	 * @param accountKey
	 * 		The new account key.
	 */
	public void setAccountKey(int accountKey) {
		this.accountKey = accountKey;
	}

	/**
	 * @return
	 * 		The duration of this {@link MarketOrder} in days.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * This method sets the duration of this {@link MarketOrder} in days.
	 * 
	 * @param duration
	 * 		The new duration of this {@link MarketOrder}.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return
	 * 		The amount of ISK in escrow.
	 */
	public BigDecimal getEscrow() {
		return escrow;
	}

	/**
	 * This method sets the amount of ISK in escrow.
	 * 
	 * @param escrow
	 * 		The amount of ISK in escrow.
	 */
	public void setEscrow(BigDecimal escrow) {
		this.escrow = escrow;
	}

	/**
	 * @return
	 * 		The price of the {@link MarketOrder}.
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * This method sets the price of this {@link MarketOrder}.
	 * 
	 * @param price
	 * 		The price of this {@link MarketOrder}.
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return
	 * 		True if this is a buy order, or false if it is a sell order.
	 */
	public boolean isBid() {
		return bid;
	}

	/**
	 * This method sets the bid flag.
	 * 
	 * @param bid
	 * 		True if this {@link MarketOrder} is a buy order, false if it is a sell order.
	 */
	public void setBid(boolean bid) {
		this.bid = bid;
	}

	/**
	 * @return
	 * 		The {@link Timestamp} when this {@link MarketOrder} was issued.
	 */
	public Timestamp getIssued() {
		return issued;
	}

	/**
	 * This method sets the {@link Timestamp} of when this {@link MarketOrder} was issued.
	 * 
	 * @param issued
	 * 		A {@link Timestamp} of when this {@link MarketOrder} was issued.
	 */
	public void setIssued(Timestamp issued) {
		this.issued = issued;
	}

}
