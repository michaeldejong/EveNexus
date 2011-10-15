package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The {@link ApiKey} class is an entity which contains information about a character in the game.
 *
 * @author michael
 */
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
	
	/**
	 * @return
	 * 		The verification code of this api key.
	 */
	public String getVerificationCode() {
		return verificationCode;
	}

	/**
	 * This method sets the verification code of this api key.
	 * 
	 * @param verificationCode
	 * 		The verification code of this api key.
	 */
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	/**
	 * @return
	 * 		The id of the api key.
	 */
	public long getKeyId() {
		return keyId;
	}

	/**
	 * This method sets the id of this api key.
	 * 
	 * @param keyId
	 * 		The api key id.
	 */
	public void setKeyId(long keyId) {
		this.keyId = keyId;
	}

	/**
	 * @return
	 * 		The character's id.
	 */
	public Long getCharacterId() {
		return characterId;
	}

	/**
	 * This method sets the character's id.
	 * 
	 * @param characterId
	 * 		The new character's id.
	 */
	public void setCharacterId(Long characterId) {
		this.characterId = characterId;
	}

	/**
	 * @return
	 * 		The name of the character.
	 */
	public String getCharacterName() {
		return characterName;
	}

	/**
	 * This method sets the name of the character.
	 * 
	 * @param characterName
	 * 		The name of the character.
	 */
	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	/**
	 * @return
	 * 		The id of the corporation.
	 */
	public long getCorporationId() {
		return corporationId;
	}

	/**
	 * This method sets the corporation id.
	 * 
	 * @param corporationId
	 * 		The id of the new corporation.
	 */
	public void setCorporationId(long corporationId) {
		this.corporationId = corporationId;
	}

	/**
	 * @return
	 * 		The name of the corporation.
	 */
	public String getCorporationName() {
		return corporationName;
	}

	/**
	 * This method sets the name of this corporation.
	 * 
	 * @param corporationName
	 * 		The name of the corporation.
	 */
	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}

}
