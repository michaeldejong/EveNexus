package nl.minicom.evenexus.eveapi;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.minicom.evenexus.utils.SettingsManager;

@Singleton
public class ApiServerManager {

	public static final String DEFAULT_API_SERVER = "http://api.eve-online.com";
	
	private final SettingsManager settingsManager;
	
	@Inject
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
	
	public void setApiServer(String serverPath) {
		settingsManager.saveObject(SettingsManager.USER_DEFINED_API_SERVER_ENABLED, true);
		
		String hostName = serverPath;
		if (serverPath != null && (serverPath.endsWith("\\") || serverPath.endsWith("/"))) {
			hostName = serverPath.substring(0, serverPath.length() - 1);
		}		
		settingsManager.saveObject(SettingsManager.USER_DEFINED_API_SERVER_HOST, hostName);
	}
	
}
