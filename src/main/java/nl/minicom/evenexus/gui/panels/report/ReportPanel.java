package nl.minicom.evenexus.gui.panels.report;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.swing.GroupLayout;

import nl.minicom.evenexus.core.report.engine.Dataset;
import nl.minicom.evenexus.core.report.engine.ReportExecutor;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.core.report.persistence.expressions.Expression;
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.icons.Icon;
import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.panels.report.dialogs.ReportWizardDialog;
import nl.minicom.evenexus.gui.panels.report.renderers.LineGraphChart;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;
import nl.minicom.evenexus.utils.SettingsManager;

import org.jfree.chart.ChartPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@link TabPanel} defines the panel displaying the reports.
 * 
 * @author michael
 */
@Singleton
public class ReportPanel extends TabPanel {

	private static final long serialVersionUID = -4187071888216622511L;
	private static final Logger LOG = LoggerFactory.getLogger(ReportPanel.class);
	
	private final SettingsManager settingsManager;
	private final ChartPanel chartPanel;
	private final ReportExecutor executor;
	private final Provider<ReportWizardDialog> dialogProvider;
	
	private Dataset dataset = null;
	private ReportModel reportModel = null;
	
	/**
	 * Constructs a new {@link ReportPanel}.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 * 
	 * @param dialogProvider
	 * 		A {@link Provider} which supplies {@link ReportWizardDialog}s.
	 * 
	 * @param executor
	 * 		The {@link ReportExecutor}.
	 */
	@Inject
	public ReportPanel(SettingsManager settingsManager, 
			Provider<ReportWizardDialog> dialogProvider,
			ReportExecutor executor) {
		
		this.settingsManager = settingsManager;
		this.chartPanel = new ChartPanel(null);
		this.executor = executor;
		this.dialogProvider = dialogProvider;
	}

	/**
	 * This method intializes the {@link ReportPanel}.
	 */
	public void initialize() {
		setBackground(GuiConstants.getTabBackground());
		
		ToolBarButton createReport = new ToolBarButton(Icon.PIE_CHART_32, "Create a new report");
        createReport.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		dialogProvider.get().initialize();
        	}
        });
        
        ToolBar toolBar = new ToolBar(settingsManager);
        GroupLayout layout = new GroupLayout(toolBar);
        toolBar.setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
    		.addComponent(createReport)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(createReport)
			)
    	);
        
        layout = new GroupLayout(this);
        setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGap(7)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(toolBar)
					.addComponent(chartPanel)
				)
				.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addGap(5)
	    		.addComponent(toolBar)
	    		.addGap(7)
	    		.addComponent(chartPanel)
	    		.addGap(7)
    	);
	}

	@Override
	protected void reloadContent() {
		LOG.info("Report panel reloaded!");
	}

	/**
	 * This method displays a new report in this {@link ReportPanel}.
	 * 
	 * @param reportModel
	 * 		The {@link ReportModel} to display.
	 */
	public void displayReport(ReportModel reportModel) {
		executor.deleteReport();
		this.reportModel = reportModel;
		executor.initialize(reportModel);
		this.dataset = executor.createDataSet();
		
		switch (reportModel.getDisplayType().getValue()) {
			case BAR_CHART:
				chartPanel.setChart(LineGraphChart.render(reportModel, dataset));
				break;
			default:
				break;
		}
	}

	/**
	 * This method displays a subselection of a report in this {@link ReportPanel}.
	 * 
	 * @param groupExpressions
	 * 		The expressions which define what data to select from the {@link Dataset}.
	 */
	public void displayReport(Expression... groupExpressions) {
		dataset = executor.createDataSet(groupExpressions);
//		switch (reportModel.getDisplayType().getValue()) {
//			case GRAPH:
				chartPanel.setChart(LineGraphChart.render(reportModel, dataset, groupExpressions));
//				break;
//			default:
//				break;
//		}
	}
	
}
