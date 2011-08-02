package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "skills")
public class Skill implements Serializable {
	
	private static final long serialVersionUID = -1243953752872224201L;

	@Id
	private final SkillIdentifier key = new SkillIdentifier(0, 0);

	@Column(name = "level")
	private int level;
	
	public long getCharacterId() {
		return key.getCharacterId();
	}
	
	public void setCharacterId(long characterId) {
		key.setCharacterId(characterId);
	}
	
	public long getTypeId() {
		return key.getTypeId();
	}
	
	public void setTypeId(long typeId) {
		key.setTypeId(typeId);
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
}
