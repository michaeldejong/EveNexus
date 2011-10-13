package nl.minicom.evenexus.gui.panels.accounts.dialogs;

/**
 * This class is a data container for EVE Online characters. 
 *
 * @author michael
 */
public class EveCharacter implements Comparable<EveCharacter> {

	private final long keyId;
	private final String vCode;
	private final String characterName;
	private final long characterID;
	private final long corporationId;
	private final String corporationName;
	
	/**
	 * This constructs a new {@link EveCharacter} object.
	 * 
	 * @param keyId
	 * 		The keyId of the API.
	 * 
	 * @param vCode
	 * 		The vCode of the API.
	 * 
	 * @param charId
	 * 		The character id used in the API.
	 * 
	 * @param charName
	 * 		The name of the character.
	 * 
	 * @param corpId
	 * 		The corporation id used in the API.
	 * 
	 * @param corpName
	 * 		The name of the corporation.
	 */
	public EveCharacter(long keyId, String vCode, long charId, String charName, long corpId, String corpName) {
		this.keyId = keyId;
		this.vCode = vCode;
		this.characterName = charName;
		this.characterID = charId;
		this.corporationId = corpId;
		this.corporationName = corpName;
	}
	
	/**
	 * @return
	 * 		The key id as used in the API.
	 */
	public long getKeyId() {
		return keyId;
	}
	
	/**
	 * @return
	 * 		The verification code as used in the API.
	 */
	public String getVerificationCode() {
		return vCode;
	}
	
	/**
	 * @return
	 * 		The name of the character.
	 */
	public String getCharacterName() {
		return characterName;
	}
	
	/**
	 * @return
	 * 		The id of the character.
	 */
	public long getCharacterId() {
		return characterID;
	}

	/**
	 * @return
	 * 		The id of the corporation.
	 */
	public long getCorporationId() {
		return corporationId;
	}
	
	/**
	 * @return
	 * 		The name of the corporation.
	 */
	public String getCorporationName() {
		return corporationName;
	}

	@Override
	public int compareTo(EveCharacter o) {
		return new Long(getCharacterId()).compareTo(new Long(o.getCharacterId()));
	}
	
}
