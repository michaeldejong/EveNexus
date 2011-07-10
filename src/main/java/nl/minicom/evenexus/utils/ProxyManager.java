package nl.minicom.evenexus.utils;

import javax.inject.Inject;


public class ProxyManager {

	private final SettingsManager settingsManager;
	
	@Inject
	public ProxyManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}
	
	public void initialize() {
		setProxyParameters();
	}
	
	public void disableProxy() {
		settingsManager.saveObject(SettingsManager.PROXY_ENABLED, false);
		setProxyParameters();
	}
	
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
