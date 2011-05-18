package nl.minicom.evenexus.eveapi;

import nl.minicom.evenexus.utils.SettingsManager;


public class ApiServerManager {

	public static final String DEFAULT_API_SERVER = "http://api.eve-online.com";
	
	private final SettingsManager settingsManager;
	
	public ApiServerManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}
	
	public String getApiServer() {
		if (settingsManager.loadBoolean(SettingsManager.USER_DEFINED_API_SERVER_ENABLED, false)) {
			return settingsManager.loadString(SettingsManager.USER_DEFINED_API_SERVER_HOST, DEFAULT_API_SERVER);
		}
		return DEFAULT_API_SERVER;
	}
	
	public void disableUserApiServer() {
		settingsManager.saveObject(SettingsManager.USER_DEFINED_API_SERVER_ENABLED, false);
	}
	
	public void setApiServer(String hostname) {
		settingsManager.saveObject(SettingsManager.USER_DEFINED_API_SERVER_ENABLED, true);
		
		if (hostname != null && (hostname.endsWith("\\") || hostname.endsWith("/"))) {
			hostname = hostname.substring(0, hostname.length() - 1);
		}		
		settingsManager.saveObject(SettingsManager.USER_DEFINED_API_SERVER_HOST, hostname);
	}
	
}
