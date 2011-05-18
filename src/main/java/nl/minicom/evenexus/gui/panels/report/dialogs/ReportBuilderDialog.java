package nl.minicom.evenexus.gui.panels.report.dialogs;

import javax.swing.GroupLayout;
import javax.swing.JPanel;

import nl.minicom.evenexus.core.report.definition.ReportDefinition;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.utils.dialogs.CustomDialog;
import nl.minicom.evenexus.gui.utils.dialogs.DialogTitle;

public class ReportBuilderDialog extends CustomDialog {

	private static final long serialVersionUID = 5707542816331260941L;
	
	private final ReportBuilderPagePanel pageDisplay;
	private final ReportBuilderPageNavigationPanel navigationPanel;

	private final ReportDefinition definition;
		
	public ReportBuilderDialog() {
		this(new ReportModel());
	}
	
	public ReportBuilderDialog(ReportModel model) {
		super(DialogTitle.REPORT_ITEM_TITLE, 360, 420);
		this.definition = new ReportDefinition();
		
		pageDisplay = new ReportBuilderPagePanel(this, definition, model);
		navigationPanel = new ReportBuilderPageNavigationPanel(pageDisplay, model);
		
		setTitle("Report creation wizard");
		
		buildGui();
		setVisible(true);
	}

	@Override
	public void createGui(JPanel guiPanel) {
		GroupLayout layout = new GroupLayout(guiPanel);
		guiPanel.setLayout(layout);        
		layout.setHorizontalGroup(
		      	layout.createSequentialGroup()
		      			.addGroup(layout.createParallelGroup()
		      					.addComponent(pageDisplay)
		      					.addComponent(navigationPanel)
		      			)
    	);
    	layout.setVerticalGroup(
	    		layout.createSequentialGroup()
   						.addComponent(pageDisplay)
   						.addGap(6)
   						.addComponent(navigationPanel)
    	);
	}

}
