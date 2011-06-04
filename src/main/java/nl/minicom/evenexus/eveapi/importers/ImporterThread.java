package nl.minicom.evenexus.eveapi.importers;


import nl.minicom.evenexus.eveapi.exceptions.WarnableException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class ImporterThread extends Thread {
	
	private static Logger logger = LogManager.getRootLogger();
	
	private final ImporterTask importer;
	
	public ImporterThread(ImporterTask importer) {
		this.importer = importer;
	}

	@Override
	public void run() {
		try {
			importer.runImporter();
		}
		catch (WarnableException e) {
			logger.warn(e);
		}
		catch (Throwable e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}
	
	public String toString() {
		return "Import thread: " + importer.getImporter().getName();
	}
	
}
