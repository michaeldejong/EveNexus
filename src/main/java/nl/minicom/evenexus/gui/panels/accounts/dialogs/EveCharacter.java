package nl.minicom.evenexus.gui.panels.accounts.dialogs;

public class EveCharacter implements Comparable<EveCharacter> {

	private final long keyId;
	private final String vCode;
	private final String characterName;
	private final long characterID;
	private final long corporationId;
	private final String corporationName;
	
	public EveCharacter(long keyId, String vCode, long characterID, String characterName, long corporationId, String corporationName) {
		this.keyId = keyId;
		this.vCode = vCode;
		this.characterName = characterName;
		this.characterID = characterID;
		this.corporationId = corporationId;
		this.corporationName = corporationName;
	}
	
	public long getKeyId() {
		return keyId;
	}
	
	public String getVerificationCode() {
		return vCode;
	}
	
	public String getCharacterName() {
		return characterName;
	}
	
	public long getCharacterId() {
		return characterID;
	}

	public long getCorporationId() {
		return corporationId;
	}
	
	public String getCorporationName() {
		return corporationName;
	}

	@Override
	public int compareTo(EveCharacter o) {
		return new Long(getCharacterId()).compareTo(new Long(o.getCharacterId()));
	}
	
}
