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
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.tables.columns.GraphColumnSelectionFrame;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;
import nl.minicom.evenexus.inventory.InventoryEvent;
import nl.minicom.evenexus.inventory.InventoryListener;
import nl.minicom.evenexus.inventory.InventoryManager;
import nl.minicom.evenexus.utils.SettingsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardPanel extends TabPanel implements ImportListener {

	private static final long serialVersionUID = 9040274995425958160L;
	private static final Logger LOG = LoggerFactory.getLogger(DashboardPanel.class);
	
	private final ImportManager importManager;
	private final InventoryManager inventoryManager;
	private final SettingsManager settingsManager;
	private final ProfitGraphElement profitGraphElement;
	private final TaxesGraphElement taxesGraphElement;
	private final SalesGraphElement salesGraphElement;
	private final PurchasesGraphElement purchasesGraphElement;
	private final LineGraphEngine chartPanel;

	@Inject
	public DashboardPanel(ImportManager importManager,
			InventoryManager inventoryManager,
			SettingsManager settingsManager,
			ProfitGraphElement profitGraphElement,
			TaxesGraphElement taxesGraphElement,
			SalesGraphElement salesGraphElement,
			PurchasesGraphElement purchasesGraphElement,
			LineGraphEngine graphingEngine) {

		this.settingsManager = settingsManager;
		this.importManager = importManager;
		this.inventoryManager = inventoryManager;
		this.profitGraphElement = profitGraphElement;
		this.taxesGraphElement = taxesGraphElement;
		this.salesGraphElement = salesGraphElement;
		this.purchasesGraphElement = purchasesGraphElement;
		this.chartPanel = graphingEngine;
	}
	
	public void initialize() {
		chartPanel.setPeriod(settingsManager.loadInt(SettingsManager.FILTER_DASHBOARD_PERIOD, 14));
		chartPanel.addGraphElement(profitGraphElement);
		chartPanel.addGraphElement(taxesGraphElement);
		chartPanel.addGraphElement(salesGraphElement);
		chartPanel.addGraphElement(purchasesGraphElement);		
		chartPanel.setBorder(new LineBorder(Color.GRAY, 1));
		chartPanel.reload();

		Gui.setLookAndFeel();
		setBackground(GuiConstants.getTabBackground());
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
    	inventoryManager.addListener(new InventoryListener() {
			@Override
			public void onUpdate(InventoryEvent event) {
				if (event.isFinished()) {
					reloadTab();
				}
			}
		});
	}
	
	@Override
	public void onImportComplete() {
		reloadTab();
	}

	protected void reloadContent() {
		chartPanel.reload();
		LOG.info("Dashboard panel reloaded!");
	}
	
	private ToolBar createTopMenu() {
		ToolBar toolBar = new ToolBar(settingsManager);
		String settingName = SettingsManager.FILTER_DASHBOARD_PERIOD;
		JPanel periodSelectionField = toolBar.createPeriodSelectionField(chartPanel, settingsManager, settingName);
		GraphColumnSelectionFrame columnSelectionFrame = new GraphColumnSelectionFrame(chartPanel);
		ToolBarButton lineSelectionButton = toolBar.createTableSelectColumnsButton(columnSelectionFrame);
		JPanel spacer = toolBar.createSpacer();
		
        GroupLayout layout = new GroupLayout(toolBar);
        toolBar.setLayout(layout);
        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        	.addComponent(periodSelectionField)
        	.addGap(3)
        	.addComponent(spacer)
        	.addGap(3)
        	.addComponent(lineSelectionButton)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(periodSelectionField)
					.addComponent(spacer)
		        	.addComponent(lineSelectionButton)
		        )
        	)
    	);
		
		return toolBar;
	}

}
