package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "importlogger")
public class ImportLog implements Serializable {
	
	private static final long serialVersionUID = -7536922778053044002L;

	@Id
	private ImportLogIdentifier key;

	@Column(name = "lastrun", nullable = false)
	private Timestamp lastRun;
	
	public ImportLog() {
		this (new ImportLogIdentifier());
	}
	
	public ImportLog(long importerId, long characterId) {
		this (new ImportLogIdentifier(importerId, characterId));
	}
	
	public ImportLog(ImportLogIdentifier identifier) {
		this.key = identifier;
	}
		
	public long getImporterId() {
		return key.getImporterId();
	}

	public void setImporterId(long importerId) {
		key.setImporterId(importerId);
	}

	public long getCharacterId() {
		return key.getCharacterId();
	}

	public void setCharacterId(long characterId) {
		key.setCharacterId(characterId);
	}

	public Timestamp getLastRun() {
		return lastRun;
	}

	public void setLastRun(Timestamp lastRun) {
		this.lastRun = lastRun;
	}
	
}
