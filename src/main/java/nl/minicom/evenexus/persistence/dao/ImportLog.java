package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The {@link ImportLog} class is an entity which contains information about when API importers were run.
 *
 * @author michael
 */
@Entity
@Table(name = "importlogger")
public class ImportLog implements Serializable {
	
	private static final long serialVersionUID = -7536922778053044002L;
	
	public static final String KEY = "key";
	public static final String LAST_RUN = "lastRun";

	@Id
	private ImportLogIdentifier key;

	@Column(name = LAST_RUN, nullable = false)
	private Timestamp lastRun;
	
	/**
	 * This constructs a new {@link ImportLog} object.
	 */
	public ImportLog() {
		this (new ImportLogIdentifier());
	}
	
	/**
	 * This constructs a new {@link ImportLog} object.
	 * 
	 * @param importerId
	 * 		The importer id.
	 * 
	 * @param characterId
	 * 		The character id.
	 */
	public ImportLog(long importerId, long characterId) {
		this (new ImportLogIdentifier(importerId, characterId));
	}
	
	/**
	 * This constructs a new {@link ImportLog} object.
	 * 
	 * @param identifier
	 * 		The {@link ImportLogIdentifier} identifier.
	 */
	public ImportLog(ImportLogIdentifier identifier) {
		this.key = identifier;
	}
		
	/**
	 * @return
	 * 		The importer id.
	 */
	public long getImporterId() {
		return key.getImporterId();
	}

	/**
	 * This method sets the importer id.
	 * 
	 * @param importerId
	 * 		The new importer id.
	 */
	public void setImporterId(long importerId) {
		key.setImporterId(importerId);
	}

	/**
	 * @return
	 * 		The character id of this {@link ImportLog} object.
	 */
	public long getCharacterId() {
		return key.getCharacterId();
	}

	/**
	 * This method sets the character id.
	 * 
	 * @param characterId
	 * 		The new character id.
	 */
	public void setCharacterId(long characterId) {
		key.setCharacterId(characterId);
	}

	/**
	 * @return
	 * 		The {@link Timestamp} of when the importer was last run for the specified character.
	 */
	public Timestamp getLastRun() {
		return lastRun;
	}

	/**
	 * This method sets the last run of this {@link ImportLog} object.
	 * 
	 * @param lastRun
	 * 		The {@link Timestamp} when this importer was last run for the specified character.
	 */
	public void setLastRun(Timestamp lastRun) {
		this.lastRun = lastRun;
	}
	
}
