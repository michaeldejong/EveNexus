package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StandingIdentifier implements Serializable {

	private static final long serialVersionUID = -6237540725758955743L;

	@Column(name = "characterid")
	private long characterId;

	@Column(name = "fromid")
	private long fromId;
	
	protected StandingIdentifier() {
		this (0, 0);
	}
	
	public StandingIdentifier(long characterId, long fromId) {
		this.characterId = characterId;
		this.fromId = fromId;
	}
	
	public long getCharacterId() {
		return characterId;
	}

	public void setCharacterId(long characterId) {
		this.characterId = characterId;
	}
	
	public long getFromId() {
		return fromId;
	}

	public void setFromId(long fromId) {
		this.fromId = fromId;
	}
	
	@Override
	public final boolean equals(Object other) {
		if (other instanceof StandingIdentifier) {
			StandingIdentifier otherId = (StandingIdentifier) other;
			return characterId == otherId.characterId && fromId == otherId.fromId;
		}
		return false;
	}
	
	@Override
	public final int hashCode() {
		return (int) ((characterId * 5 + fromId * 19 + 21) % Integer.MAX_VALUE);
	}
	
}
