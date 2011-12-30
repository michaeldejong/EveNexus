package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import java.awt.Dimension;

import javax.swing.JPanel;

import nl.minicom.evenexus.gui.utils.dialogs.titles.DialogTitle;

/**
 * This class is inherited by all 'pages' of the ReportWizardDialog.
 * 
 * @author michael
 */
public abstract class ReportWizardPage extends JPanel {

	private static final long serialVersionUID = -518048166321128035L;
	
	/**
	 * This constructs a new {@link ReportWizardPage}.
	 */
	public ReportWizardPage() {
		setDimensions();
	}
	
	private void setDimensions() {	
		setMinimumSize(new Dimension(0, 0));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}

	/**
	 * @return
	 * 		The {@link DialogTitle} of the {@link ReportWizardPage}.
	 */
	public abstract DialogTitle getTitle();
	
	/**
	 * This method builds the gui.
	 */
	public abstract void buildGui();

	/**
	 * @return
	 * 		True if the user is allowed to proceed to the next page.
	 */
	public boolean allowNext() {
		return true;
	}

}
