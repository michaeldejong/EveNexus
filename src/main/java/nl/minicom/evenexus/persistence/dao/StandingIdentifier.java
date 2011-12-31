package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This identifies the standing between two entities.
 * 
 * @author michael
 */
@Embeddable
public class StandingIdentifier implements Serializable {

	private static final long serialVersionUID = -6237540725758955743L;

	public static final String CHARACTER_ID = "characterId";
	public static final String FROM_ID = "fromId";
	
	@Column(name = CHARACTER_ID)
	private long characterId;

	@Column(name = FROM_ID)
	private long fromId;
	
	/*
	 * Private no-args contructor for serialization.
	 */
	@SuppressWarnings("unused")
	private StandingIdentifier() {
		this (0, 0);
	}
	
	/**
	 * This constructs a new {@link StandingIdentifier} object.
	 * 
	 * @param characterId
	 * 		The id of the character.
	 * 
	 * @param fromId
	 * 		The id of the entity.
	 */
	public StandingIdentifier(long characterId, long fromId) {
		this.characterId = characterId;
		this.fromId = fromId;
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
	
	/**
	 * @return
	 * 		The id of the entity giving the standing.
	 */
	public long getFromId() {
		return fromId;
	}

	/**
	 * This method sets the id of the entity giving the standing.
	 * 
	 * @param fromId
	 * 		The id of the entity.
	 */
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
