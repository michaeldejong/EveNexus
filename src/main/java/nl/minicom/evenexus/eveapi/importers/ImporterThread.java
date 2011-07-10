package nl.minicom.evenexus.eveapi.importers;


import javax.inject.Inject;

import nl.minicom.evenexus.eveapi.exceptions.WarnableException;
import nl.minicom.evenexus.persistence.dao.ApiKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImporterThread extends Thread {
	
	private static final Logger LOG = LoggerFactory.getLogger(ImporterThread.class);
	
	private final ImporterTask importer;
	
	private ApiKey apiKey;
	
	@Inject
	public ImporterThread(ImporterTask importer) {
		this.importer = importer;
	}
	
	public void initialize(ApiKey apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public void run() {
		try {
			importer.runImporter(apiKey);
		}
		catch (WarnableException e) {
			LOG.warn(e.getLocalizedMessage(), e);
		}
		catch (Throwable e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
	}
	
	public String toString() {
		return "Import thread: " + importer.getName();
	}
	
}
