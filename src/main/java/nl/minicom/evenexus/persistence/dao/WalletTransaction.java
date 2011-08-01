package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transactions")
public class WalletTransaction implements Serializable {
	
	private static final long serialVersionUID = -767602436142772543L;
	
	public enum Type {
		BUY,
		SELL
	}

	@Id
	@Column(name = "transactionid")
	private long transactionId;

	@Column(name = "transactiondatetime")
	private Timestamp transactionDateTime;

	@Column(name = "characterid")
	private long characterId;

	@Column(name = "quantity")
	private long quantity;
	
	@Column(name = "remaining")
	private long remaining;

	@Column(name = "typename")
	private String typeName;

	@Column(name = "typeid")
	private long typeId;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "taxes")
	private BigDecimal taxes;

	@Column(name = "clientid")
	private long clientId;

	@Column(name = "clientname")
	private String clientName;

	@Column(name = "stationid")
	private long stationId;

	@Column(name = "stationname")
	private String stationName;

	@Column(name = "ispersonal")
	private boolean isPersonal;
	
	public Timestamp getTransactionDateTime() {
		return transactionDateTime;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public void setTransactionDateTime(Timestamp transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	public long getCharacterId() {
		return characterId;
	}

	public void setCharacterId(long characterId) {
		this.characterId = characterId;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public long getRemaining() {
		return remaining;
	}

	public void setRemaining(long remaining) {
		this.remaining = remaining;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public boolean isPersonal() {
		return isPersonal;
	}

	public void setPersonal(boolean isPersonal) {
		this.isPersonal = isPersonal;
	}

	public boolean isBuy() {
		return price.compareTo(BigDecimal.ZERO) == -1;
	}

	public boolean beforeOrEquals(WalletTransaction other) {
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(transactionDateTime.getTime());
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		
		Calendar otherTime = Calendar.getInstance();
		otherTime.setTimeInMillis(other.transactionDateTime.getTime());
		otherTime.set(Calendar.SECOND, 0);
		otherTime.set(Calendar.MILLISECOND, 0);
		
		return time.before(otherTime) || time.equals(otherTime);
	}
	
	public boolean equals(Object other) {
		if (other instanceof WalletTransaction) {
			WalletTransaction transaction = (WalletTransaction) other;
			
			return transactionId == transaction.transactionId 
				&& transactionDateTime.equals(transaction.transactionDateTime) 
				&& characterId == transaction.characterId 
				&& quantity == transaction.quantity 
				&& remaining == transaction.remaining 
				&& typeName.equals(transaction.typeName) 
				&& typeId == transaction.typeId 
				&& price.compareTo(transaction.price) == 0 
				&& taxes.compareTo(transaction.taxes) == 0 
				&& clientId == transaction.clientId 
				&& clientName.equals(transaction.clientName) 
				&& stationId == transaction.stationId 
				&& stationName.equals(transaction.stationName) 
				&& isPersonal == transaction.isPersonal;
		}
		return false;
	}
	
}
