package nl.minicom.evenexus.eveapi.importers;


import nl.minicom.evenexus.eveapi.exceptions.WarnableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImporterThread extends Thread {
	
	private static final Logger LOG = LoggerFactory.getLogger(ImporterThread.class);
	
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
			LOG.warn(e.getLocalizedMessage(), e);
		}
		catch (Throwable e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
	}
	
	public String toString() {
		return "Import thread: " + importer.getImporter().getName();
	}
	
}
