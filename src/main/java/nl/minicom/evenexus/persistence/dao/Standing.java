package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "standings")
public class Standing implements Serializable {
	
	private static final long serialVersionUID = 787285101503903188L;

	public static final String KEY = "key";
	public static final String FROM_NAME = "fromname";
	public static final String STANDING = "standing";
	
	@Id
	private final StandingIdentifier key = new StandingIdentifier(0, 0);

	@Column(name = FROM_NAME)
	private String fromName;

	@Column(name = STANDING)
	private BigDecimal standing;
	
	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public BigDecimal getStanding() {
		return standing;
	}

	public void setStanding(BigDecimal standing) {
		this.standing = standing;
	}

	public long getCharacterId() {
		return key.getCharacterId();
	}

	public void setCharacterId(long characterId) {
		key.setCharacterId(characterId);
	}

	public long getFromId() {
		return key.getFromId();
	}

	public void setFromId(long fromId) {
		key.setFromId(fromId);
	}
	
}
