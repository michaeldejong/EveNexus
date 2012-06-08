package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import java.awt.Dimension;

import javax.swing.JPanel;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.engine.DisplayType;
import nl.minicom.evenexus.core.report.engine.Model;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.utils.dialogs.titles.DialogTitle;

/**
 * This class is inherited by all 'pages' of the ReportWizardDialog.
 * 
 * @author michael
 */
public abstract class ReportWizardPage extends JPanel {

	private static final long serialVersionUID = -518048166321128035L;
	
	private final ReportModel reportModel;
	
	/**
	 * This constructs a new {@link ReportWizardPage}.
	 * 
	 * @param reportModel
	 * 		The {@link ReportModel}.
	 */
	public ReportWizardPage(ReportModel reportModel) {
		this.reportModel = reportModel;
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
	 * This method removes all listeners from the {@link ReportModel}.
	 */
	public abstract void removeListeners();
	
	/**
	 * @return
	 * 		True if the user is allowed to go back to the previous page.
	 */
	public abstract boolean allowPrevious();
	
	/**
	 * @return
	 * 		True if the user is allowed to proceed to the next page.
	 */
	public abstract boolean allowNext();
	
	/**
	 * @return
	 * 		True if the user is allowed to execute the report.
	 */
	public final boolean allowExecute() {
		if (reportModel.getReportItems().isEmpty()) {
			return false;
		}
		
		Model<ReportGroup> grouping1 = reportModel.getGrouping1();
		if (!grouping1.isEnabled() || !grouping1.isSet()) {
			return false;
		}
		
		Model<DisplayType> displayType = reportModel.getDisplayType();
		if (!displayType.isEnabled() || !displayType.isSet()) {
			return false;
		}
		
		for (ReportGroup group : reportModel.getReportGroups()) {
			if (!displayType.getValue().supports(group.getType())) {
				return false;
			}
		}
		
		return true;
	}

}
