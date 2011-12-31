package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity describes the standing between two entities.
 *
 * @author michael
 */
@Entity
@Table(name = "standings")
public class Standing implements Serializable {
	
	private static final long serialVersionUID = 787285101503903188L;

	public static final String KEY = "key";
	public static final String FROM_NAME = "fromName";
	public static final String STANDING = "standing";
	
	@Id
	private final StandingIdentifier key = new StandingIdentifier(0, 0);

	@Column(name = FROM_NAME)
	private String fromName;

	@Column(name = STANDING)
	private BigDecimal standing;
	
	/**
	 * @return
	 * 		The name of the entity giving the standing.
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * This method sets the name of the entity giving the standing.
	 * 
	 * @param fromName
	 * 		The name of the entity.
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * @return
	 * 		The standing between the two entities.
	 */
	public BigDecimal getStanding() {
		return standing;
	}

	/**
	 * This method sets the standing between the two entities.
	 * 
	 * @param standing
	 * 		The standing.
	 */
	public void setStanding(BigDecimal standing) {
		this.standing = standing;
	}

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
	 * 		The id of the character.
	 */
	public void setCharacterId(long characterId) {
		key.setCharacterId(characterId);
	}

	/**
	 * @return
	 * 		The id of the entity giving the standing.
	 */
	public long getFromId() {
		return key.getFromId();
	}

	/**
	 * This method sets the id of the entity giving the standing.
	 * 
	 * @param fromId
	 * 		The id of the entity.
	 */
	public void setFromId(long fromId) {
		key.setFromId(fromId);
	}
	
}
