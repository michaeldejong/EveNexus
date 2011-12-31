package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This identifies a certain skill for a certain character.
 *
 * @author michael
 */
@Embeddable
public class SkillIdentifier implements Serializable {

	private static final long serialVersionUID = -6237540725758955743L;

	public static final String CHARACTER_ID = "characterId";
	public static final String TYPE_ID = "typeId";
	
	@Column(name = CHARACTER_ID)
	private long characterId;

	@Column(name = TYPE_ID)
	private long typeId;
	
	/*
	 * Private no-args constructor for serialization.
	 */
	@SuppressWarnings("unused")
	private SkillIdentifier() {
		this (0, 0);
	}
	
	/**
	 * Constructs a new {@link SkillIdentifier} object.
	 * 
	 * @param characterId
	 * 		The id of the character.
	 * 
	 * @param typeId
	 * 		The id of the skill.
	 */
	public SkillIdentifier(long characterId, long typeId) {
		this.characterId = characterId;
		this.typeId = typeId;
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
	 * 		The id of the skill.
	 */
	public long getTypeId() {
		return typeId;
	}

	/**
	 * This method sets the id of the skill.
	 * 
	 * @param typeId
	 * 		The id of the skill.
	 */
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	@Override
	public final boolean equals(Object other) {
		if (other instanceof StandingIdentifier) {
			SkillIdentifier otherId = (SkillIdentifier) other;
			return characterId == otherId.characterId && typeId == otherId.typeId;
		}
		return false;
	}
	
	@Override
	public final int hashCode() {
		return (int) ((characterId * 5 + typeId * 19 + 21) % Integer.MAX_VALUE);
	}
	
}
