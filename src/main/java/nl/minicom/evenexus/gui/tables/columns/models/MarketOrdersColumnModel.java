package nl.minicom.evenexus.gui.tables.columns.models;


import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.utils.SettingsManager;

public class MarketOrdersColumnModel extends ColumnModel {

	@Inject
	public MarketOrdersColumnModel(SettingsManager settingsManager) {
		super(settingsManager);
	}
	
	@Override
	public void initialize() {
		add(new Column(
				getSettingsManager(),
				"Item",
				"typeName",
				SettingsManager.TABLE_MARKETORDER_ITEM_VISIBLE,
				true,
				SettingsManager.TABLE_MARKETORDER_ITEM_WIDTH,
				237,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Volume entered",
				"volEntered",
				SettingsManager.TABLE_MARKETORDER_VOLENTERED_VISIBLE,
				true,
				SettingsManager.TABLE_MARKETORDER_VOLENTERED_WIDTH,
				87,
				Long.class,
				ColumnModel.INTEGER
		));
		
		add(new Column(
				getSettingsManager(),
				"Volume remaining",
				"volRemaining",
				SettingsManager.TABLE_MARKETORDER_VOLREMAINING_VISIBLE,
				true,
				SettingsManager.TABLE_MARKETORDER_VOLREMAINING_WIDTH,
				86,
				Long.class,
				ColumnModel.INTEGER
		));
		
		add(new Column(
				getSettingsManager(),
				"Minimum volume",
				"minVolume",
				SettingsManager.TABLE_MARKETORDER_MINVOLUME_VISIBLE,
				false,
				SettingsManager.TABLE_MARKETORDER_MINVOLUME_WIDTH,
				50,
				Long.class,
				ColumnModel.INTEGER
		));
		
		add(new Column(
				getSettingsManager(),
				"Station",
				"stationName",
				SettingsManager.TABLE_MARKETORDER_STATION_VISIBLE,
				true,
				SettingsManager.TABLE_MARKETORDER_STATION_WIDTH,
				236,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Price",
				"price",
				SettingsManager.TABLE_MARKETORDER_PRICE_VISIBLE,
				true,
				SettingsManager.TABLE_MARKETORDER_PRICE_WIDTH,
				137,
				BigDecimal.class,
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Issued", 
				"issued",
				SettingsManager.TABLE_MARKETORDER_ISSUED_VISIBLE,
				false,
				SettingsManager.TABLE_MARKETORDER_ISSUED_WIDTH,
				100, 
				Timestamp.class,
				ColumnModel.DATE_TIME
		));
	}
	
}
