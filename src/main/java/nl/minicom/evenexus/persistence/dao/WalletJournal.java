package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "journal")
public class WalletJournal implements Serializable {
	
	private static final long serialVersionUID = -5992502737944420079L;

	public static final String REF_ID = "refId";
	public static final String JOURNAL_TYPE_ID = "journaltypeid";
	public static final String DATE = "date";
	public static final String OWNER_NAME_1 = "ownername1";
	public static final String OWNER_ID_1 = "ownerid1";
	public static final String OWNER_NAME_2 = "ownername2";
	public static final String OWNER_ID_2 = "ownerid2";
	public static final String ARG_NAME_1 = "argname1";
	public static final String ARG_ID_1 = "argid1";
	public static final String AMOUNT = "amount";
	public static final String BALANCE = "balance";
	public static final String REASON = "reason";
	public static final String TAX_RECEIVER_ID = "taxreceiverid";
	public static final String TAX_AMOUNT = "taxamount";
	
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
	
	public long getRefId() {
		return refId;
	}

	public void setRefId(long refId) {
		this.refId = refId;
	}

	public int getJournalTypeId() {
		return journalTypeId;
	}

	public void setJournalTypeId(int journalTypeId) {
		this.journalTypeId = journalTypeId;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getOwnerName1() {
		return ownerName1;
	}

	public void setOwnerName1(String ownerName1) {
		this.ownerName1 = ownerName1;
	}

	public Long getOwnerId1() {
		return ownerId1;
	}

	public void setOwnerId1(Long ownerId1) {
		this.ownerId1 = ownerId1;
	}

	public String getOwnerName2() {
		return ownerName2;
	}

	public void setOwnerName2(String ownerName2) {
		this.ownerName2 = ownerName2;
	}

	public Long getOwnerId2() {
		return ownerId2;
	}

	public void setOwnerId2(Long ownerId2) {
		this.ownerId2 = ownerId2;
	}

	public String getArgName1() {
		return argName1;
	}

	public void setArgName1(String argName1) {
		this.argName1 = argName1;
	}

	public long getArgId1() {
		return argId1;
	}

	public void setArgId1(long argId1) {
		this.argId1 = argId1;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getTaxReceiverId() {
		return taxReceiverId;
	}

	public void setTaxReceiverId(Long taxReceiverId) {
		this.taxReceiverId = taxReceiverId;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	
}
