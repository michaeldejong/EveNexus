package nl.minicom.evenexus.gui.panels.marketorders;


import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nl.minicom.evenexus.core.Application;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.importers.ImportListener;
import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.gui.tables.columns.TableColumnSelectionFrame;
import nl.minicom.evenexus.gui.tables.columns.models.MarketOrdersColumnModel;
import nl.minicom.evenexus.gui.tables.datamodel.implementations.BuyOrdersTableDataModel;
import nl.minicom.evenexus.gui.tables.datamodel.implementations.SellOrdersTableDataModel;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class MarketOrdersPanel extends TabPanel implements ImportListener {

	private static final long serialVersionUID = -4187071888216622511L;
	private static final Logger logger = LogManager.getRootLogger();

	private final Table table1;
	private final Table table2;
	private final ColumnModel columnModel;
	private final Application application;

	public MarketOrdersPanel(Application application) {	
		super();	
		
		this.application = application;
		this.columnModel = new MarketOrdersColumnModel(application.getSettingsManager());
		this.table1 = new Table(new SellOrdersTableDataModel(), columnModel);
		this.table2 = new Table(new BuyOrdersTableDataModel(), columnModel);
		
		JScrollPane scrollPane1 = new JScrollPane(table1);
		scrollPane1.getVerticalScrollBar().setUnitIncrement(16);
		JScrollPane scrollPane2 = new JScrollPane(table2);
		scrollPane2.getVerticalScrollBar().setUnitIncrement(16);
		ToolBar panel = createTopMenu();
		        
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGap(7)
        		.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        			.addComponent(panel)
        			.addComponent(scrollPane1)
        			.addComponent(scrollPane2)
        		)
				.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addGap(5)
    			.addComponent(panel)
	    		.addGap(7)
    			.addComponent(scrollPane1)
	    		.addGap(7)
    			.addComponent(scrollPane2)
	    		.addGap(7)
    	);
    	
    	application.getImportManager().addListener(Api.CHAR_MARKET_ORDERS, this);
	}
	
	@Override
	public void onImportComplete() {
		reloadTab();
	}

	@Override
	public void reloadTab() {
		table1.reload();
		table2.reload();
		logger.debug("Market orders panel reloaded!");
	}

	private ToolBar createTopMenu() {
		ToolBar toolBar = new ToolBar(application.getSettingsManager());		
		JPanel typeNameSearchField = toolBar.createTypeNameSearchField(table1, table2);
		ToolBarButton button = toolBar.createTableSelectColumnsButton(new TableColumnSelectionFrame(columnModel, table1, table2));
		
        GroupLayout layout = new GroupLayout(toolBar);
        toolBar.setLayout(layout);
        
		layout.setHorizontalGroup(
        	layout.createSequentialGroup()
			.addComponent(typeNameSearchField)
			.addGap(7)
			.addComponent(button)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(typeNameSearchField)
					.addComponent(button)
		        )
        	)
    	);
		
		return toolBar;
	}
	
}
