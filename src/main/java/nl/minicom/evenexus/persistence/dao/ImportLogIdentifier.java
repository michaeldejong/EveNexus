package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ImportLogIdentifier implements Serializable {

	private static final long serialVersionUID = -6237540725758955743L;

	@Column(name = "importer", nullable = false)
	private long importerId;
	
	@Column(name = "characterid", nullable = false)
	private long characterId;
	
	public ImportLogIdentifier(long importerId, long characterId) {
		this.importerId = importerId;
		this.characterId = characterId;
	}
	
	public long getImporterId() {
		return importerId;
	}

	public void setImporterId(long importerId) {
		this.importerId = importerId;
	}
	
	public long getCharacterId() {
		return characterId;
	}

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
