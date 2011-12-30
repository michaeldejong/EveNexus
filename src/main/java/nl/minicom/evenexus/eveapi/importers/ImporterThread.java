package nl.minicom.evenexus.eveapi.importers;


import javax.inject.Inject;

import nl.minicom.evenexus.eveapi.exceptions.WarnableException;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;
import nl.minicom.evenexus.persistence.dao.ApiKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@link Thread} is responsible for running an {@link ImporterTask}.
 *
 * @author michael
 */
public class ImporterThread extends Thread {
	
	private static final Logger LOG = LoggerFactory.getLogger(ImporterThread.class);
	
	private final BugReportDialog dialog;
	
	private ImporterTask importer;
	private ApiKey apiKey;
	
	/**
	 * This constructs a new {@link ImporterThread} object.
	 * 
	 * @param dialog
	 * 		The {@link BugReportDialog}.
	 */
	@Inject
	public ImporterThread(BugReportDialog dialog) {
		this.dialog = dialog;
	}
	
	/**
	 * This method initializes the {@link ImporterThread}.
	 * 
	 * @param importer
	 * 		The {@link ImporterTask} to run.
	 * 
	 * @param apiKey
	 * 		The {@link ApiKey} to run the import on.
	 */
	public void initialize(ImporterTask importer, ApiKey apiKey) {
		this.importer = importer;
		this.apiKey = apiKey;
	}

	@Override
	public void run() {
		try {
			if (importer.isReady()) {
				importer.runImporter(apiKey);
			}
		}
		catch (WarnableException e) {
			LOG.warn(e.getLocalizedMessage(), e);
		}
		catch (Throwable e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
		}
	}
	
	/**
	 * @return 
	 * 		The description of this {@link Thread}.
	 */
	@Override
	public String toString() {
		return "Import thread: " + importer.getName();
	}
	
}
