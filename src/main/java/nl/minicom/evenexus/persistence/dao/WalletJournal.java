package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity describes an entry in the players journal.
 * 
 * @author michael
 */
@Entity
@Table(name = "journal")
public class WalletJournal implements Serializable {
	
	private static final long serialVersionUID = -5992502737944420079L;

	public static final String REF_ID = "refId";
	public static final String JOURNAL_TYPE_ID = "journalTypeId";
	public static final String DATE = "date";
	public static final String OWNER_NAME_1 = "ownerName1";
	public static final String OWNER_ID_1 = "ownerId1";
	public static final String OWNER_NAME_2 = "ownerName2";
	public static final String OWNER_ID_2 = "ownerId2";
	public static final String ARG_NAME_1 = "argName1";
	public static final String ARG_ID_1 = "argId1";
	public static final String AMOUNT = "amount";
	public static final String BALANCE = "balance";
	public static final String REASON = "reason";
	public static final String TAX_RECEIVER_ID = "taxReceiverId";
	public static final String TAX_AMOUNT = "taxAmount";
	
	@Id
	@Column(name = REF_ID, nullable = false)
	private long refId;

	@Column(name = JOURNAL_TYPE_ID, nullable = false)
	private int journalTypeId;

	@Column(name = DATE, nullable = false)
	private Timestamp date;

	@Column(name = OWNER_NAME_1)
	private String ownerName1;

	@Column(name = OWNER_ID_1)
	private Long ownerId1;

	@Column(name = OWNER_NAME_2)
	private String ownerName2;

	@Column(name = OWNER_ID_2)
	private Long ownerId2;

	@Column(name = ARG_NAME_1)
	private String argName1;

	@Column(name = ARG_ID_1)
	private long argId1;

	@Column(name = AMOUNT)
	private BigDecimal amount;

	@Column(name = BALANCE)
	private BigDecimal balance;

	@Column(name = REASON)
	private String reason;

	@Column(name = TAX_RECEIVER_ID)
	private Long taxReceiverId;

	@Column(name = TAX_AMOUNT)
	private BigDecimal taxAmount;
	
	/**
	 * @return
	 * 		The id of this {@link WalletJournal}.
	 */
	public long getRefId() {
		return refId;
	}

	/**
	 * This method sets the id of this {@link WalletJournal}.
	 * 
	 * @param refId
	 * 		The new id of this {@link WalletJournal}.
	 */
	public void setRefId(long refId) {
		this.refId = refId;
	}

	/**
	 * @return
	 * 		The id of the ref type of this {@link WalletJournal}.
	 */
	public int getJournalTypeId() {
		return journalTypeId;
	}

	/**
	 * This method sets the id of the ref type of this {@link WalletJournal}.
	 * 
	 * @param journalTypeId
	 * 		The id of the ref type.
	 */
	public void setJournalTypeId(int journalTypeId) {
		this.journalTypeId = journalTypeId;
	}

	/**
	 * @return
	 * 		The {@link Timestamp} of when this {@link WalletJournal} took place.
	 */
	public Timestamp getDate() {
		return date;
	}

	/**
	 * This method sets the {@link Timestamp} of when this {@link WalletJournal} took place.
	 * 
	 * @param date
	 * 		The {@link Timestamp}.
	 */
	public void setDate(Timestamp date) {
		this.date = date;
	}

	/**
	 * @return
	 * 		The name of the first owner.
	 */
	public String getOwnerName1() {
		return ownerName1;
	}

	/**
	 * This method sets the name of the first owner.
	 * 
	 * @param ownerName1
	 * 		The owner's name.
	 */
	public void setOwnerName1(String ownerName1) {
		this.ownerName1 = ownerName1;
	}

	/**
	 * @return
	 * 		The id of the first owner.
	 */
	public Long getOwnerId1() {
		return ownerId1;
	}

	/**
	 * This method sets the id of the first owner.
	 * 
	 * @param ownerId1
	 * 		The id of the first owner.
	 */
	public void setOwnerId1(Long ownerId1) {
		this.ownerId1 = ownerId1;
	}

	/**
	 * @return
	 * 		The name of the second owner.
	 */
	public String getOwnerName2() {
		return ownerName2;
	}

	/**
	 * This method sets the name of the second owner.
	 * 
	 * @param ownerName2
	 * 		The name of the second owner.
	 */
	public void setOwnerName2(String ownerName2) {
		this.ownerName2 = ownerName2;
	}

	/**
	 * @return
	 * 		The id of the second owner.
	 */
	public Long getOwnerId2() {
		return ownerId2;
	}

	/**
	 * This method sets the id of the second owner.
	 * 
	 * @param ownerId2
	 * 		The id of the second owner.
	 */
	public void setOwnerId2(Long ownerId2) {
		this.ownerId2 = ownerId2;
	}

	/**
	 * @return
	 * 		The name of the first argument.
	 */
	public String getArgName1() {
		return argName1;
	}
	
	/**
	 * This method sets the name of the first argument.
	 * 
	 * @param argName1
	 * 		The name of the first argument.
	 */
	public void setArgName1(String argName1) {
		this.argName1 = argName1;
	}

	/**
	 * @return
	 * 		The id of the first argument.
	 */
	public long getArgId1() {
		return argId1;
	}

	/**
	 * This method sets the id of the first argument.
	 * 
	 * @param argId1
	 * 		The id of the first argument.
	 */
	public void setArgId1(long argId1) {
		this.argId1 = argId1;
	}

	/**
	 * @return
	 * 		The ISK amount.
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * This method sets the ISK amount.
	 * 
	 * @param amount
	 * 		The new ISK amount.
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return
	 * 		The balance after this {@link WalletJournal} took place.
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * This method sets the balance after this {@link WalletJournal} took place.
	 * 
	 * @param balance
	 * 		The new ISK balance.
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * @return
	 * 		The user-specified reason.
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * This method sets the user-specified reason.
	 * 
	 * @param reason
	 * 		The reason.
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return
	 * 		The id of the tax receiver.
	 */
	public Long getTaxReceiverId() {
		return taxReceiverId;
	}

	/**
	 * This method sets the id of the tax receiver.
	 * 
	 * @param taxReceiverId
	 * 		The id of the tax receiver.
	 */
	public void setTaxReceiverId(Long taxReceiverId) {
		this.taxReceiverId = taxReceiverId;
	}

	/**
	 * @return
	 * 		The ISK amount that was taxed.
	 */
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	/**
	 * This method sets the ISK amount that was taxed.
	 * 
	 * @param taxAmount
	 * 		The amount of ISK that was taxed.
	 */
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	
}
