package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name = "transactions")
public class WalletTransaction implements Serializable {
	
	private static final long serialVersionUID = -767602436142772543L;
	
	public static final String TRANSACTION_ID = "transactionId";
	public static final String TRANSACTION_DATE_TIME = "transactionDateTime";
	public static final String CHARACTER_ID = "characterId";
	public static final String QUANTITY = "quantity";
	public static final String REMAINING = "remaining";
	public static final String TYPE_NAME = "typeName";
	public static final String TYPE_ID = "typeId";
	public static final String PRICE = "price";
	public static final String TAXES = "taxes";
	public static final String CLIENT_ID = "clientId";
	public static final String CLIENT_NAME = "clientName";
	public static final String STATION_ID = "stationId";
	public static final String STATION_NAME = "stationName";
	public static final String IS_PERSONAL = "isPersonal";
	
	public enum Type {
		BUY,
		SELL
	}

	@Id
	@Column(name = TRANSACTION_ID)
	private long transactionId;

	@Column(name = TRANSACTION_DATE_TIME)
	private Timestamp transactionDateTime;

	@Column(name = CHARACTER_ID)
	private long characterId;

	@Column(name = QUANTITY)
	private long quantity;
	
	@Column(name = REMAINING)
	private long remaining;

	@Column(name = TYPE_NAME)
	private String typeName;

	@Column(name = TYPE_ID)
	private long typeId;

	@Column(name = PRICE)
	private BigDecimal price;

	@Column(name = TAXES)
	private BigDecimal taxes;

	@Column(name = CLIENT_ID)
	private long clientId;

	@Column(name = CLIENT_NAME)
	private String clientName;

	@Column(name = STATION_ID)
	private long stationId;

	@Column(name = STATION_NAME)
	private String stationName;

	@Column(name = IS_PERSONAL)
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
			
			return new EqualsBuilder()
				.append(transactionId, transaction.transactionId)
				.append(transactionDateTime, transaction.transactionDateTime)
				.append(characterId, transaction.characterId)
				.append(quantity, transaction.quantity)
				.append(remaining, transaction.remaining)
				.append(typeName, transaction.typeName)
				.append(typeId, transaction.typeId)
				.appendSuper(price.doubleValue() == transaction.price.doubleValue())
				.appendSuper(taxes.doubleValue() == transaction.taxes.doubleValue())
				.append(clientId, transaction.clientId)
				.append(clientName, transaction.clientName)
				.append(stationId, transaction.stationId)
				.append(stationName, transaction.stationName)
				.append(isPersonal, transaction.isPersonal)
				.isEquals();
		}
		return false;
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(transactionId)
			.append(transactionDateTime)
			.append(characterId)
			.append(quantity)
			.append(remaining)
			.append(typeName)
			.append(typeId)
			.append(price)
			.append(taxes)
			.append(clientId)
			.append(clientName)
			.append(stationId)
			.append(stationName)
			.append(isPersonal)
			.toHashCode();
	}
	
}
