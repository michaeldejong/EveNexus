package nl.minicom.evenexus.core;


import nl.minicom.evenexus.utils.SettingsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ShutdownThread extends Thread {
	
	private static final Logger LOG = LoggerFactory.getLogger(ShutdownThread.class);
	
	private final SettingsManager settingsManager;
	
	public ShutdownThread(SettingsManager settingsManager) {
		super();
		this.settingsManager = settingsManager;
	}

	@Override
	public void run() {
		try {
			//Save all gui related information.
			settingsManager.save();
		}
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
	}
		
}
