package nl.minicom.evenexus.gui.panels.transactions;


import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.importers.ImportListener;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.TableColumnSelectionFrame;
import nl.minicom.evenexus.gui.tables.columns.models.TransactionColumnModel;
import nl.minicom.evenexus.gui.tables.datamodel.implementations.TransactionTableDataModel;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;
import nl.minicom.evenexus.utils.SettingsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TransactionsPanel extends TabPanel implements ImportListener {

	private static final long serialVersionUID = -4187071888216622511L;	
	private static final Logger LOG = LoggerFactory.getLogger(TransactionsPanel.class);

	private final Table table;
	private final TransactionColumnModel columnModel;
	private final TransactionTableDataModel tableDataModel;
	private final SettingsManager settingsManager;
	
	@Inject
	public TransactionsPanel(
			ImportManager importManager,
			SettingsManager settingsManager,
			TransactionColumnModel columnModel, 
			TransactionTableDataModel tableDataModel,
			Table table) {
		
		this.table = table;
		this.tableDataModel = tableDataModel;
		this.columnModel = columnModel;
		this.settingsManager = settingsManager;
		
		importManager.addListener(Api.CHAR_WALLET_TRANSACTIONS, this);
	}
	
	public synchronized void initialize() {
		columnModel.initialize();
		tableDataModel.initialize();
		table.initialize(tableDataModel, columnModel);
		
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
	public synchronized void onImportComplete() {
		reloadTab();
	}

	@Override
	public synchronized void reloadTab() {
		table.reload();
		LOG.info("Transaction panel reloaded!");
	}

	private ToolBar createTopMenu() {
		ToolBar toolBar = new ToolBar(settingsManager);
		
		JPanel typeNameSearchField = toolBar.createTypeNameSearchField(table);
		JPanel periodSelectionField = toolBar.createPeriodSelectionField(table, SettingsManager.FILTER_TRANSACTION_PERIOD);
		ToolBarButton button = toolBar.createTableSelectColumnsButton(new TableColumnSelectionFrame(table.getColumns(), table));
		
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
