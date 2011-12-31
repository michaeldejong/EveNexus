package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class describes the skill of a character.
 * 
 * @author michael
 */
@Entity
@Table(name = "skills")
public class Skill implements Serializable {
	
	private static final long serialVersionUID = -1243953752872224201L;

	public static final String KEY = "key";
	public static final String LEVEL = "level";
	
	@Id
	private final SkillIdentifier key = new SkillIdentifier(0, 0);

	@Column(name = LEVEL)
	private int level;
	
	/**
	 * @return
	 * 		The id of the character.
	 */
	public long getCharacterId() {
		return key.getCharacterId();
	}
	
	/**
	 * This method sets the id of the character.
	 * 
	 * @param characterId
	 * 		The new id of the character.
	 */
	public void setCharacterId(long characterId) {
		key.setCharacterId(characterId);
	}
	
	/**
	 * @return
	 * 		The id of the skill.
	 */
	public long getTypeId() {
		return key.getTypeId();
	}
	
	/**
	 * This method sets the id of the skill.
	 * 
	 * @param typeId
	 * 		The new id of the skill.
	 */
	public void setTypeId(long typeId) {
		key.setTypeId(typeId);
	}
	
	/**
	 * @return
	 * 		The level of the skill. Value can be between 0 and 5 (inclusive).
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * This method sets the level of the skill.
	 * 
	 * @param level
	 * 		The level of the skill. Can be between 0 and 5 (inclusive).
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
}
