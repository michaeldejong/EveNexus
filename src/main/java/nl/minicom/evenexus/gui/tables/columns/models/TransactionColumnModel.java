package nl.minicom.evenexus.gui.tables.columns.models;


import java.math.BigDecimal;
import java.sql.Timestamp;

import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.utils.SettingsManager;

public class TransactionColumnModel extends ColumnModel {

	public TransactionColumnModel(SettingsManager settingsManager) {
		add(new Column(
				settingsManager,
				"Date", 
				"transactionDateTime",
				SettingsManager.TABLE_TRANSACTION_DATE_VISIBLE,
				true,
				SettingsManager.TABLE_TRANSACTION_DATE_WIDTH,
				138, 
				Timestamp.class, 
				Table.DATE_TIME_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Item", 
				"typename", 
				SettingsManager.TABLE_TRANSACTION_ITEM_VISIBLE,
				true,
				SettingsManager.TABLE_TRANSACTION_ITEM_WIDTH,
				302,
				String.class
		));
		
		add(new Column(
				settingsManager,
				"Station", 
				"stationname", 
				SettingsManager.TABLE_TRANSACTION_STATION_VISIBLE,
				false,
				SettingsManager.TABLE_TRANSACTION_STATION_WIDTH,
				300,
				String.class
		));
		
		add(new Column(
				settingsManager,
				"Client", 
				"clientName", 
				SettingsManager.TABLE_TRANSACTION_CLIENT_VISIBLE,
				false,
				SettingsManager.TABLE_TRANSACTION_CLIENT_WIDTH,
				200,
				String.class
		));
		
		add(new Column(
				settingsManager,
				"Quantity", 
				"quantity", 
				SettingsManager.TABLE_TRANSACTION_QUANTITY_VISIBLE,
				true,
				SettingsManager.TABLE_TRANSACTION_QUANTITY_WIDTH,
				80, 
				Long.class, 
				Table.INTEGER_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Price", 
				"price", 
				SettingsManager.TABLE_TRANSACTION_PRICE_VISIBLE,
				true,
				SettingsManager.TABLE_TRANSACTION_PRICE_WIDTH,
				132, 
				BigDecimal.class, 
				Table.CURRENCY_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Taxes", 
				"totaltax", 
				SettingsManager.TABLE_TRANSACTION_TAXES_VISIBLE,
				false,
				SettingsManager.TABLE_TRANSACTION_TAXES_WIDTH,
				100, 
				BigDecimal.class, 
				Table.CURRENCY_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Total (exc. taxes)", 
				"totalnotax", 
				SettingsManager.TABLE_TRANSACTION_TOTAL_WITHOUT_TAXES_VISIBLE,
				false,
				SettingsManager.TABLE_TRANSACTION_TOTAL_WITHOUT_TAXES_WIDTH,
				100, 
				BigDecimal.class, 
				Table.CURRENCY_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Total (inc. taxes)", 
				"totalwithtax", 
				SettingsManager.TABLE_TRANSACTION_TOTAL_WITH_TAXES_VISIBLE,
				true,
				SettingsManager.TABLE_TRANSACTION_TOTAL_WITH_TAXES_WIDTH,
				131, 
				BigDecimal.class, 
				Table.CURRENCY_RENDERER
		));
	}
	
}
