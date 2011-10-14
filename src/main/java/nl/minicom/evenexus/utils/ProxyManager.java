package nl.minicom.evenexus.utils;

import javax.inject.Inject;


/**
 * This class is responsible for loading and initializing the proxy details.
 * 
 * @author michael
 */
public class ProxyManager {

	private final SettingsManager settingsManager;
	
	/**
	 * This constructs a new {@link ProxyManager} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 */
	@Inject
	public ProxyManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}
	
	/**
	 * This method initializes this {@link ProxyManager} object.
	 */
	public void initialize() {
		setProxyParameters();
	}
	
	/**
	 * This method disables the proxy settings.
	 */
	public void disableProxy() {
		settingsManager.saveObject(SettingsManager.PROXY_ENABLED, false);
		setProxyParameters();
	}
	
	/**
	 * This method sets the host and port of the proxy and saves these values.
	 * 
	 * @param hostname
	 * 		The new hostname of the proxy.
	 * 
	 * @param port
	 * 		The new port of the proxy.
	 */
	public void setProxyHostAndPort(String hostname, int port) {
		settingsManager.saveObject(SettingsManager.PROXY_ENABLED, true);
		settingsManager.saveObject(SettingsManager.PROXY_HOST, hostname);
		settingsManager.saveObject(SettingsManager.PROXY_PORT, port);
		setProxyParameters();
	}

	private void setProxyParameters() {
		System.getProperties().put("proxySet", settingsManager.loadBoolean(SettingsManager.PROXY_ENABLED, false));
		System.getProperties().put("proxyHost", settingsManager.loadString(SettingsManager.PROXY_HOST, ""));
		System.getProperties().put("proxyPort", settingsManager.loadString(SettingsManager.PROXY_PORT, ""));
	}
	
}
