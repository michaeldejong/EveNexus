package nl.minicom.evenexus.eveapi;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.minicom.evenexus.utils.SettingsManager;

/**
 * The {@link ApiServerManager} allows the developer to set and request API server related settings.
 *
 * @author michael
 */
@Singleton
public class ApiServerManager {

	public static final String DEFAULT_API_SERVER = "http://api.eve-online.com";
	
	private final SettingsManager settingsManager;
	
	/**
	 * This constructs a new {@link ApiServerManager} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 */
	@Inject
	public ApiServerManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}
	
	/**
	 * @return
	 * 		The currently set API server host.
	 */
	public String getApiServer() {
		if (settingsManager.loadBoolean(SettingsManager.USER_DEFINED_API_SERVER_ENABLED, false)) {
			return settingsManager.loadString(SettingsManager.USER_DEFINED_API_SERVER_HOST, DEFAULT_API_SERVER);
		}
		return DEFAULT_API_SERVER;
	}
	
	/**
	 * This method disables the custom API server settings. After calling this method the {@link ApiServerManager}
	 * will redirect the developer to the default API server.
	 */
	public void disableUserApiServer() {
		settingsManager.saveObject(SettingsManager.USER_DEFINED_API_SERVER_ENABLED, false);
	}
	
	/**
	 * This method sets the API server path.
	 * 
	 * @param serverPath
	 * 		The new API server path.
	 */
	public void setApiServer(String serverPath) {
		settingsManager.saveObject(SettingsManager.USER_DEFINED_API_SERVER_ENABLED, true);
		
		String hostName = serverPath;
		if (serverPath != null && (serverPath.endsWith("\\") || serverPath.endsWith("/"))) {
			hostName = serverPath.substring(0, serverPath.length() - 1);
		}		
		settingsManager.saveObject(SettingsManager.USER_DEFINED_API_SERVER_HOST, hostName);
	}
	
}
