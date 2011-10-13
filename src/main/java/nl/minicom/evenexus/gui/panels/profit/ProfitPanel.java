package nl.minicom.evenexus.gui.panels.profit;


import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.gui.tables.columns.TableColumnSelectionFrame;
import nl.minicom.evenexus.gui.tables.columns.models.ProfitColumnModel;
import nl.minicom.evenexus.gui.tables.datamodel.implementations.ProfitTableDataModel;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;
import nl.minicom.evenexus.inventory.InventoryEvent;
import nl.minicom.evenexus.inventory.InventoryListener;
import nl.minicom.evenexus.inventory.InventoryManager;
import nl.minicom.evenexus.utils.SettingsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProfitPanel extends TabPanel implements InventoryListener {

	private static final long serialVersionUID = -4187071888216622511L;
	private static final Logger LOG = LoggerFactory.getLogger(ProfitPanel.class);

	private final Table table;
	private final ColumnModel columnModel;
	private final ProfitTableDataModel profitData;
	private final SettingsManager settingsManager;
	private final InventoryManager inventoryManager;
	
	@Inject
	public ProfitPanel(SettingsManager settingsManager,
			InventoryManager inventoryManager, 
			ProfitTableDataModel profitData,
			Table table) {	
		
		this.settingsManager = settingsManager;
		this.inventoryManager = inventoryManager;
		this.columnModel = new ProfitColumnModel(settingsManager);
		this.profitData = profitData;
		this.table = table;
	}	

	public void initialize() {
		columnModel.initialize();
		profitData.initialize();
		table.initialize(profitData, columnModel);

		setBackground(GuiConstants.getTabBackground());
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);		
		ToolBar panel = createTopMenu();
		        
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGap(7)
        		.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        			.addComponent(panel)
        			.addComponent(scrollPane)
        		)
				.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addGap(5)
    			.addComponent(panel)
	    		.addGap(7)
    			.addComponent(scrollPane)
	    		.addGap(7)
    	);
    	
    	inventoryManager.addListener(this);
	}

	@Override
	public void reloadTab() {
		table.reload();
		LOG.info("Profit panel reloaded!");
	}
	
	private ToolBar createTopMenu() {
		ToolBar toolBar = new ToolBar(settingsManager);		
		JPanel typeNameSearchField = toolBar.createTypeNameSearchField(table);
		JPanel periodSelectionField = toolBar.createPeriodSelectionField(table, SettingsManager.FILTER_PROFIT_PERIOD);
		ToolBarButton button = toolBar.createTableSelectColumnsButton(new TableColumnSelectionFrame(columnModel, table));
		JPanel spacer = toolBar.createSpacer();
		
        GroupLayout layout = new GroupLayout(toolBar);
        toolBar.setLayout(layout);
        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
			.addComponent(typeNameSearchField)
			.addGap(3)
			.addComponent(periodSelectionField)
			.addGap(3)
			.addComponent(spacer)
			.addGap(3)
			.addComponent(button)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(typeNameSearchField)
					.addComponent(periodSelectionField)
					.addComponent(spacer)
					.addComponent(button)
		        )
        	)
    	);
		
		return toolBar;
	}

	@Override
	public void onUpdate(InventoryEvent event) {
		if (event.isFinished()) {
			reloadTab();
		}
	}

}
