package nl.minicom.evenexus.gui.panels.accounts.dialogs;

public class EveCharacter implements Comparable<EveCharacter> {

	private final String name;
	private final int userID;
	private final String apiKey;
	private final long characterID;
	
	public EveCharacter(String name, int userID, String apiKey, long characterID) {
		this.name = name;
		this.userID = userID;
		this.apiKey = apiKey;
		this.characterID = characterID;
	}
	
	public String getName() {
		return name;
	}
	
	public int getUserId() {
		return userID;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public long getCharacterId() {
		return characterID;
	}

	@Override
	public int compareTo(EveCharacter o) {
		return new Long(getCharacterId()).compareTo(new Long(o.getCharacterId()));
	}
	
}
