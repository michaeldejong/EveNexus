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

/**
 * The {@link WalletTransaction} entity contains information about 
 * a certain transaction performed by a character.
 *
 * @author michael
 */
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
	
	/**
	 * @return
	 * 		The {@link Timestamp} of the {@link WalletTransaction} (when it took place).
	 */
	public Timestamp getTransactionDateTime() {
		return transactionDateTime;
	}

	/**
	 * This method sets the {@link Timestamp} of this {@link WalletTransaction}.
	 * 
	 * @param transactionDateTime
	 * 		A {@link Timestamp} of when this {@link WalletTransaction} took place.
	 */
	public void setTransactionDateTime(Timestamp transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	/**
	 * @return
	 * 		The id of this {@link WalletTransaction}.
	 */
	public long getTransactionId() {
		return transactionId;
	}

	/**
	 * This method sets the id of this {@link WalletTransaction}.
	 * 
	 * @param transactionId
	 * 		The new id of this {@link WalletTransaction}.
	 */
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return
	 * 		The id of the character who was involved in this {@link WalletTransaction}.
	 */
	public long getCharacterId() {
		return characterId;
	}

	/**
	 * This method sets the id of the character who was involved with this {@link WalletTransaction}.
	 * 
	 * @param characterId
	 * 		The id of the character.
	 */
	public void setCharacterId(long characterId) {
		this.characterId = characterId;
	}

	/**
	 * @return
	 * 		The quantity of items involved in this {@link WalletTransaction}.
	 */
	public long getQuantity() {
		return quantity;
	}

	/**
	 * This method sets the quantity of items involved in this {@link WalletTransaction}.
	 * 
	 * @param quantity
	 * 		The new quantity.
	 */
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return
	 * 		The amount of items which have not yet been matched to other {@link WalletTransaction}s.
	 */
	public long getRemaining() {
		return remaining;
	}

	/**
	 * This method sets the amount of unmatched items.
	 * 
	 * @param remaining
	 * 		The amount of items, which are still unmatched.
	 */
	public void setRemaining(long remaining) {
		this.remaining = remaining;
	}

	/**
	 * @return
	 * 		The name of the items which were traded.
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * This method sets the name of the items which were traded.
	 * 
	 * @param typeName
	 * 		The name of the items.
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return
	 * 		The id of the items traded.
	 */
	public long getTypeId() {
		return typeId;
	}

	/**
	 * This method sets the id of the items which were traded.
	 * 
	 * @param typeId
	 * 		The id of the items.
	 */
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return
	 * 		The price of the {@link WalletTransaction}.
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * This method sets the price of this {@link WalletTransaction}.
	 * 
	 * @param price
	 * 		The price of this {@link WalletTransaction}.
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return
	 * 		The amount of taxes paid over this {@link WalletTransaction}.
	 */
	public BigDecimal getTaxes() {
		return taxes;
	}

	/**
	 * This method sets the amount of taxes paid over this {@link WalletTransaction}.
	 * 
	 * @param taxes
	 * 		The amount of taxes paid.
	 */
	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	/**
	 * @return
	 * 		The id of the client.
	 */
	public long getClientId() {
		return clientId;
	}

	/**
	 * This method sets the id of the client.
	 * 
	 * @param clientId
	 * 		The id of the client.
	 */
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return
	 * 		The name of the client.
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * This method sets the name of the client.
	 * 
	 * @param clientName
	 * 		The name of the client.
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * @return
	 * 		The id of the {@link Station} where this {@link WalletTransaction} took place.
	 */
	public long getStationId() {
		return stationId;
	}

	/**
	 * This method sets the {@link Station} id of this {@link WalletTransaction}.
	 * 
	 * @param stationId
	 * 		The id of the {@link Station}.
	 */
	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	/**
	 * @return
	 * 		The name of the {@link Station} where this {@link WalletTransaction} took place.
	 */
	public String getStationName() {
		return stationName;
	}

	/**
	 * This method sets the {@link Station} name of thisi {@link WalletTransaction}.
	 * 
	 * @param stationName
	 * 		The new {@link Station} name.
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	/**
	 * @return
	 * 		True if this {@link WalletTransaction} is a personal {@link WalletTransaction}.
	 */
	public boolean isPersonal() {
		return isPersonal;
	}

	/**
	 * This method sets the 'personal' flag of this {@link WalletTransaction}.
	 * 
	 * @param isPersonal
	 * 		True if this is a personal {@link WalletTransaction}.
	 */
	public void setPersonal(boolean isPersonal) {
		this.isPersonal = isPersonal;
	}

	/**
	 * @return
	 * 		True if the {@link WalletTransaction} is a buy order.
	 */
	public boolean isBuy() {
		return price.compareTo(BigDecimal.ZERO) == -1;
	}

	/**
	 * This method checks if this {@link WalletTransaction} took place before,
	 * or on the same time as the 'other' {@link WalletTransaction}.
	 * 
	 * @param other
	 * 		The {@link WalletTransaction} to compare this {@link WalletTransaction} to.
	 * 
	 * @return
	 * 		True if this {@link WalletTransaction} takes place before or on the same time
	 * 		as the 'other' {@link WalletTransaction}.
	 */
	public boolean beforeOrSimultaniously(WalletTransaction other) {
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
	
	/**
	 * This method checks if this object is equal to 'other'.
	 * 
	 * @param other
	 * 		The other {@link Object} to compare this {@link WalletTransaction} to.
	 * 
	 * @return
	 * 		True if the objects are equal.
	 */
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
	
	/**
	 * @return
	 * 		The hash code of this object.
	 */
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
