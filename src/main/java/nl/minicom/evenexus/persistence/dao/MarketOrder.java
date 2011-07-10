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
	
	@Id
	@Column(name = "orderid")
	private long orderId;

	@Column(name = "charid")
	private long charId;

	@Column(name = "stationid")
	private long stationId;

	@Column(name = "volentered")
	private long volEntered;

	@Column(name = "volremaining")
	private long volRemaining;

	@Column(name = "minvolume")
	private long minVolume;

	@Column(name = "orderstate")
	private int orderState;

	@Column(name = "typeid")
	private long typeId;

	@Column(name = "range")
	private int range;

	@Column(name = "accountkey")
	private int accountKey;

	@Column(name = "duration")
	private int duration;

	@Column(name = "escrow")
	private BigDecimal escrow;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "bid")
	private boolean bid;

	@Column(name = "issued")
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
