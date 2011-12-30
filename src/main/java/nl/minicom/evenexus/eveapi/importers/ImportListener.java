package nl.minicom.evenexus.eveapi.importers;

/**
 * This Listener allows ImporterTasks to notify the ImportManager.
 *
 * @author michael
 */
public interface ImportListener {

	/**
	 * This method is called when an ImportTask is done with the import.
	 */
	void onImportComplete();
	
}
