package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;

@Entity
@Table(name = "marketorders")
public class MarketOrder implements Serializable {
	
	private static final long serialVersionUID = 7476899993373397147L;
	
	public static final String ORDER_ID = "orderid";
	public static final String CHAR_ID = "charid";
	public static final String STATION_ID = "stationid";
	public static final String VOL_ENTERED = "volentered";
	public static final String VOL_REMAINING = "volremaining";
	public static final String MIN_VOLUME = "minvolume";
	public static final String ORDER_STATE = "orderstate";
	public static final String TYPE_ID = "typeid";
	public static final String RANGE = "range";
	public static final String ACCOUNT_KEY = "accountkey";
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

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	public long getCharacterId() {
		return charId;
	}

	public void setCharacterId(long charId) {
		this.charId = charId;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public long getVolumeEntered() {
		return volEntered;
	}

	public void setVolumeEntered(long volumeEntered) {
		this.volEntered = volumeEntered;
	}

	public long getVolumeRemaining() {
		return volRemaining;
	}

	public void setVolumeRemaining(long volumeRemaining) {
		this.volRemaining = volumeRemaining;
	}

	public long getMinimumVolume() {
		return minVolume;
	}

	public void setMinimumVolume(long minimumVolume) {
		this.minVolume = minimumVolume;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(int accountKey) {
		this.accountKey = accountKey;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public BigDecimal getEscrow() {
		return escrow;
	}

	public void setEscrow(BigDecimal escrow) {
		this.escrow = escrow;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isBid() {
		return bid;
	}

	public void setBid(boolean bid) {
		this.bid = bid;
	}

	public Timestamp getIssued() {
		return issued;
	}

	public void setIssued(Timestamp issued) {
		this.issued = issued;
	}

	public static void markAllActiveAsExpired(Session session, long characterId) {
		// TODO Auto-generated method stub
	}
	
}
