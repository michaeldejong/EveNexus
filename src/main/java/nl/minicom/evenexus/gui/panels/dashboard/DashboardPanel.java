package nl.minicom.evenexus.gui.panels.dashboard;

import java.awt.Color;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.importers.ImportListener;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.tables.columns.GraphColumnSelectionFrame;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;
import nl.minicom.evenexus.utils.SettingsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardPanel extends TabPanel implements ImportListener {

	private static final long serialVersionUID = 9040274995425958160L;
	private static final Logger LOG = LoggerFactory.getLogger(DashboardPanel.class);
	
	private final LineGraphEngine chartPanel;
	private final SettingsManager settingsManager;

	@Inject
	public DashboardPanel(ImportManager importManager,
			SettingsManager settingsManager,
			ProfitGraphElement profitGraphElement,
			TaxesGraphElement taxesGraphElement,
			SalesGraphElement salesGraphElement,
			PurchasesGraphElement purchaseGraphElement) {
		

		this.settingsManager = settingsManager;
		
		int period = settingsManager.loadInt(SettingsManager.FILTER_DASHBOARD_PERIOD, 14);
		this.chartPanel = new LineGraphEngine(period);
		this.chartPanel.addGraphElement(profitGraphElement);
		this.chartPanel.addGraphElement(taxesGraphElement);
		this.chartPanel.addGraphElement(salesGraphElement);
		this.chartPanel.addGraphElement(purchaseGraphElement);		
		this.chartPanel.setBorder(new LineBorder(Color.GRAY, 1));
		this.chartPanel.reload();

		Gui.setLookAndFeel();
		setBackground(Color.WHITE);
		ToolBar toolBar = createTopMenu();
        
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);        
		layout.setHorizontalGroup(
      	layout.createSequentialGroup()
        		.addGap(7)
        		.addGroup(layout.createParallelGroup(Alignment.LEADING)
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
    	
    	importManager.addListener(Api.CHAR_WALLET_TRANSACTIONS, this);
	}
	
	@Override
	public void onImportComplete() {
		reloadTab();
	}

	public void reloadTab() {
		chartPanel.reload();
		LOG.debug("Dashboard panel reloaded!");
	}
	
	private ToolBar createTopMenu() {
		ToolBar toolBar = new ToolBar(settingsManager);
		JPanel periodSelectionField = toolBar.createPeriodSelectionField(chartPanel, settingsManager, SettingsManager.FILTER_DASHBOARD_PERIOD);
		ToolBarButton lineSelectionButton = toolBar.createTableSelectColumnsButton(new GraphColumnSelectionFrame(chartPanel));
		
        GroupLayout layout = new GroupLayout(toolBar);
        toolBar.setLayout(layout);
        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        	.addComponent(periodSelectionField)
        	.addGap(7)
        	.addComponent(lineSelectionButton)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(periodSelectionField)
		        	.addComponent(lineSelectionButton)
		        )
        	)
    	);
		
		return toolBar;
	}

}
