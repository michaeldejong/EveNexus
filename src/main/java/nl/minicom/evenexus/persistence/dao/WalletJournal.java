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

	@Id
	@Column(name = "refId", nullable = false)
	private long refId;

	@Column(name = "journaltypeid", nullable = false)
	private int journalTypeId;

	@Column(name = "date", nullable = false)
	private Timestamp date;

	@Column(name = "ownername1")
	private String ownerName1;

	@Column(name = "ownerid1")
	private Long ownerId1;

	@Column(name = "ownername2")
	private String ownerName2;

	@Column(name = "ownerid2")
	private Long ownerId2;

	@Column(name = "argname1")
	private String argName1;

	@Column(name = "argid1")
	private long argId1;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "balance")
	private BigDecimal balance;

	@Column(name = "reason")
	private String reason;

	@Column(name = "taxreceiverid")
	private Long taxReceiverId;

	@Column(name = "taxamount")
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
