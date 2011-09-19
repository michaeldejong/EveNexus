package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "apikeys")
public class ApiKey implements Serializable {

	private static final long serialVersionUID = -5243514709906664430L;
	
	public static final String VERIFICATION_CODE = "verificationCode";
	public static final String KEY_ID = "keyId";
	
	public static final String CHARACTER_ID = "characterId";
	public static final String CHARACTER_NAME = "characterName";
	public static final String CORPORATION_ID = "corporationId";
	public static final String CORPORATION_NAME = "corporationName";

	@Column(name = VERIFICATION_CODE, nullable = false)
	private String verificationCode;

	@Column(name = KEY_ID, nullable = false)
	private long keyId;

	@Id
	@Column(name = CHARACTER_ID, nullable = false)
	private Long characterId;
	
	@Column(name = CHARACTER_NAME, nullable = false)
	private String characterName;
	
	@Column(name = CORPORATION_ID, nullable = false)
	private Long corporationId;
	
	@Column(name = CORPORATION_NAME, nullable = false)
	private String corporationName;
	
	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public long getKeyId() {
		return keyId;
	}

	public void setKeyId(long keyId) {
		this.keyId = keyId;
	}

	public Long getCharacterId() {
		return characterId;
	}

	public void setCharacterId(Long characterId) {
		this.characterId = characterId;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public long getCorporationId() {
		return corporationId;
	}

	public void setCorporationId(long corporationId) {
		this.corporationId = corporationId;
	}

	public String getCorporationName() {
		return corporationName;
	}

	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}

}
