package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

/**
 * This interface is meant as a callback. The ReportWizardPage objects should call this method 
 * to notify the ReportWizardDialog of a change.
 *
 * @author michael
 */
public interface ReportPageListener {
	
	/**
	 * This method is called when a model is modified.
	 */
	void onModification();

}
