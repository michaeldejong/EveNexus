package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SkillIdentifier implements Serializable {

	private static final long serialVersionUID = -6237540725758955743L;

	public static final String CHARACTER_ID = "characterId";
	public static final String TYPE_ID = "typeId";
	
	@Column(name = CHARACTER_ID)
	private long characterId;

	@Column(name = TYPE_ID)
	private long typeId;
	
	protected SkillIdentifier() {
		this (0, 0);
	}
	
	public SkillIdentifier(long characterId, long typeId) {
		this.characterId = characterId;
		this.typeId = typeId;
	}
	
	public long getCharacterId() {
		return characterId;
	}

	public void setCharacterId(long characterId) {
		this.characterId = characterId;
	}
	
	public long getTypeId() {
		return typeId;
	}

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
