package nl.minicom.evenexus.gui.panels.profit;


import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.gui.tables.columns.TableColumnSelectionFrame;
import nl.minicom.evenexus.gui.tables.columns.models.ProfitColumnModel;
import nl.minicom.evenexus.gui.tables.datamodel.implementations.ProfitTableDataModel;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;
import nl.minicom.evenexus.utils.SettingsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProfitPanel extends TabPanel {

	private static final long serialVersionUID = -4187071888216622511L;
	private static final Logger LOG = LoggerFactory.getLogger(ProfitPanel.class);

	private final Table table;
	private final ColumnModel columnModel;
	private final ProfitTableDataModel profitData;
	private final SettingsManager settingsManager;
	
	@Inject
	public ProfitPanel(SettingsManager settingsManager,
			ImportManager importManager,
			ProfitTableDataModel profitData,
			Table table) {	
		
		this.settingsManager = settingsManager;
		this.columnModel = new ProfitColumnModel(settingsManager);
		this.profitData = profitData;
		this.table = table;
	}	

	public void initialize() {
		table.initialize(profitData, columnModel);
		
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
	}

	@Override
	public void reloadTab() {
		table.reload();
		LOG.debug("Profit panel reloaded!");
	}
	
	private ToolBar createTopMenu() {
		ToolBar toolBar = new ToolBar(settingsManager);		
		JPanel typeNameSearchField = toolBar.createTypeNameSearchField(table);
		JPanel periodSelectionField = toolBar.createPeriodSelectionField(table, SettingsManager.FILTER_PROFIT_PERIOD);
		ToolBarButton button = toolBar.createTableSelectColumnsButton(new TableColumnSelectionFrame(columnModel, table));
		
        GroupLayout layout = new GroupLayout(toolBar);
        toolBar.setLayout(layout);
        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
			.addComponent(typeNameSearchField)
			.addComponent(periodSelectionField)
			.addGap(7)
			.addComponent(button)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(typeNameSearchField)
					.addComponent(periodSelectionField)
					.addComponent(button)
		        )
        	)
    	);
		
		return toolBar;
	}

}
