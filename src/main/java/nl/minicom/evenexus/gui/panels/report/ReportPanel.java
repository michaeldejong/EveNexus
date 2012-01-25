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
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.icons.Icon;
import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.panels.report.dialogs.ReportWizardDialog;
import nl.minicom.evenexus.gui.panels.report.renderers.BarChart;
import nl.minicom.evenexus.gui.panels.report.renderers.Chart;
import nl.minicom.evenexus.gui.panels.report.renderers.LineChart;
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
	
	private Chart currentChart = null;
	private Dataset dataset = null;
	private DisplayConfiguration configuration;
	
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
        
        ToolBarButton moveLeft = new ToolBarButton(Icon.LEFT_48, "Move left");
        moveLeft.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		configuration.modifyOffset(-5);
        		currentChart.refreshGraph();
        	}
        });
        
        ToolBarButton moveRight = new ToolBarButton(Icon.RIGHT_48, "Move right");
        moveRight.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		configuration.modifyOffset(5);
        		currentChart.refreshGraph();
        	}
        });
        
        ToolBar toolBar = new ToolBar(settingsManager);
        GroupLayout layout = new GroupLayout(toolBar);
        toolBar.setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
    		.addComponent(createReport)
    		.addComponent(moveLeft)
    		.addComponent(moveRight)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(createReport)
				.addComponent(moveLeft)
				.addComponent(moveRight)
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
		if (currentChart != null) {
			currentChart.removeListeners();
		}
		
		executor.deleteReport();
		executor.initialize(reportModel);
		this.dataset = executor.createDataSet();
		configuration = new DisplayConfiguration(dataset.groupSize());
		
		switch (reportModel.getDisplayType().getValue()) {
			case BAR_CHART:
				currentChart = new BarChart(chartPanel, reportModel, dataset, configuration);
				break;
			case LINE_GRAPH:
				new LineChart().render(chartPanel, reportModel, dataset, configuration);
				break;
			default:
				break;
		}
	}
	
}
