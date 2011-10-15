package nl.minicom.evenexus.core;


import nl.minicom.evenexus.utils.SettingsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The {@link ShutdownThread} is a special {@link Thread} implementation, which
 * is called when the application is terminated. This class is responsible for 
 * persisting changes, and cleaning up resources (if applicable).
 * 
 * @author michael
 */
public class ShutdownThread extends Thread {
	
	private static final Logger LOG = LoggerFactory.getLogger(ShutdownThread.class);
	
	private final SettingsManager settingsManager;
	
	/**
	 * This contructs a new {@link ShutdownThread} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 */
	public ShutdownThread(SettingsManager settingsManager) {
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
