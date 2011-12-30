package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This class identifies an import log entity. 
 *
 * @author michael
 */
@Embeddable
public class ImportLogIdentifier implements Serializable {

	private static final long serialVersionUID = -6237540725758955743L;

	public static final String IMPORTER = "importer";
	public static final String CHARACTER_ID = "characterId";
	
	@Column(name = IMPORTER, nullable = false)
	private long importerId = 0;
	
	@Column(name = CHARACTER_ID, nullable = false)
	private long characterId = 0;
	
	/**
	 * No-args contructor.
	 */
	protected ImportLogIdentifier() {
		this (0, 0);
	}
	
	/**
	 * This constructs a new {@link ImportLogIdentifier}.
	 * 
	 * @param importerId
	 * 		The id of the importer.
	 * 
	 * @param characterId
	 * 		The id of the character.
	 */
	public ImportLogIdentifier(long importerId, long characterId) {
		this.importerId = importerId;
		this.characterId = characterId;
	}
	
	/**
	 * @return
	 * 		The id of the importer.
	 */
	public long getImporterId() {
		return importerId;
	}

	/**
	 * This method sets the id of the importer.
	 * 
	 * @param importerId
	 * 		The new id of the importer.
	 */
	public void setImporterId(long importerId) {
		this.importerId = importerId;
	}
	
	/**
	 * @return
	 * 		The id of the character.
	 */
	public long getCharacterId() {
		return characterId;
	}

	/**
	 * This method sets the id of the character.
	 * 
	 * @param characterId
	 * 		The id of the character.
	 */
	public void setCharacterId(long characterId) {
		this.characterId = characterId;
	}
	
	@Override
	public final boolean equals(Object other) {
		if (other instanceof StandingIdentifier) {
			ImportLogIdentifier otherId = (ImportLogIdentifier) other;
			return characterId == otherId.characterId && importerId == otherId.importerId;
		}
		return false;
	}
	
	@Override
	public final int hashCode() {
		return (int) ((characterId * 5 + importerId * 19 + 21) % Integer.MAX_VALUE);
	}
	
}
