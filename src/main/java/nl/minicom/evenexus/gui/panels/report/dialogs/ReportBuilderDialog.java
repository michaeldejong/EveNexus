package nl.minicom.evenexus.gui.panels.report.dialogs;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.JPanel;

import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.utils.dialogs.CustomDialog;
import nl.minicom.evenexus.gui.utils.dialogs.DialogTitle;

public class ReportBuilderDialog extends CustomDialog {

	private static final long serialVersionUID = 5707542816331260941L;
	
	private final ReportBuilderPagePanel pageDisplay;
	private final ReportBuilderPageNavigationPanel navigationPanel;

	@Inject
	public ReportBuilderDialog(ReportBuilderPagePanel reportBuilderPagePanel) {
		super(DialogTitle.REPORT_ITEM_TITLE, 360, 420);

		ReportModel model = new ReportModel();
		
		pageDisplay = reportBuilderPagePanel.initialize(this, model);
		navigationPanel = new ReportBuilderPageNavigationPanel(pageDisplay, model);
	}
	
	public void initialize() {
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
