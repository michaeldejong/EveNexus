package nl.minicom.evenexus.gui.panels.marketorders;


import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.importers.ImportListener;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.gui.tables.columns.TableColumnSelectionFrame;
import nl.minicom.evenexus.gui.tables.columns.models.MarketOrdersColumnModel;
import nl.minicom.evenexus.gui.tables.datamodel.implementations.BuyOrdersTableDataModel;
import nl.minicom.evenexus.gui.tables.datamodel.implementations.SellOrdersTableDataModel;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;
import nl.minicom.evenexus.utils.SettingsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This tab displays all the data on market orders.
 *
 * @author michael
 */
public class MarketOrdersPanel extends TabPanel implements ImportListener {

	private static final long serialVersionUID = -4187071888216622511L;
	private static final Logger LOG = LoggerFactory.getLogger(MarketOrdersPanel.class);

	private final Table table1;
	private final Table table2;
	private final ColumnModel columnModel;
	private final SellOrdersTableDataModel sellOrderData;
	private final BuyOrdersTableDataModel buyOrderData;
	private final SettingsManager settingsManager;

	/**
	 * This contructs a new {@link MarketOrdersPanel} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 * 
	 * @param importManager
	 * 		The {@link ImportManager}.
	 * 
	 * @param sellOrderData
	 * 		The {@link SellOrdersTableDataModel}.
	 * 
	 * @param buyOrderDate
	 * 		The {@link BuyOrdersTableDataModel}.
	 * 
	 * @param tableProvider
	 * 		A provider for {@link Table} objects.
	 * 
	 * @param columnModel
	 * 		The {@link MarketOrdersColumnModel}.
	 */
	@Inject
	public MarketOrdersPanel(SettingsManager settingsManager,
			ImportManager importManager,
			SellOrdersTableDataModel sellOrderData,
			BuyOrdersTableDataModel buyOrderDate,
			Provider<Table> tableProvider,
			MarketOrdersColumnModel columnModel) {	
		
		this.table1 = tableProvider.get();
		this.table2 = tableProvider.get();
		this.columnModel = columnModel;
		this.sellOrderData = sellOrderData;
		this.buyOrderData = buyOrderDate;
		this.settingsManager = settingsManager;
    	
    	importManager.addListener(Api.CHAR_MARKET_ORDERS, this);
	}

	/**
	 * This method intializes this {@link MarketOrdersPanel} object.
	 */
	public void initialize() {
		columnModel.initialize();
		table1.initialize(sellOrderData, columnModel);
		table2.initialize(buyOrderData, columnModel);

		setBackground(GuiConstants.getTabBackground());
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
	}
	
	@Override
	public void onImportComplete() {
		reloadTab();
	}

	/**
	 * This method reloads all the contents of this panel.
	 */
	protected void reloadContent() {
		table1.reload();
		table2.reload();
		LOG.info("Market orders panel reloaded!");
	}

	private ToolBar createTopMenu() {
		ToolBar toolBar = new ToolBar(settingsManager);		
		JPanel typeNameSearchField = toolBar.createTypeNameSearchField(table1, table2);
		TableColumnSelectionFrame columnSelectionFrame = new TableColumnSelectionFrame(columnModel, table1, table2);
		ToolBarButton button = toolBar.createTableSelectColumnsButton(columnSelectionFrame);
		JPanel spacer = toolBar.createSpacer();
		
        GroupLayout layout = new GroupLayout(toolBar);
        toolBar.setLayout(layout);
        
		layout.setHorizontalGroup(
        	layout.createSequentialGroup()
			.addComponent(typeNameSearchField)
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
					.addComponent(spacer)
					.addComponent(button)
		        )
        	)
    	);
		
		return toolBar;
	}
	
}
