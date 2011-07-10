package nl.minicom.evenexus.gui.tables.columns.models;


import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.utils.SettingsManager;

public class MarketOrdersColumnModel extends ColumnModel {

	@Inject
	public MarketOrdersColumnModel(SettingsManager settingsManager) {
		add(new Column(
				settingsManager,
				"Item",
				"typeName",
				SettingsManager.TABLE_MARKETORDER_ITEM_VISIBLE,
				true,
				SettingsManager.TABLE_MARKETORDER_ITEM_WIDTH,
				237,
				String.class
		));
		
		add(new Column(
				settingsManager,
				"Volume entered",
				"volEntered",
				SettingsManager.TABLE_MARKETORDER_VOLENTERED_VISIBLE,
				true,
				SettingsManager.TABLE_MARKETORDER_VOLENTERED_WIDTH,
				87,
				Long.class,
				Table.INTEGER_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Volume remaining",
				"volRemaining",
				SettingsManager.TABLE_MARKETORDER_VOLREMAINING_VISIBLE,
				true,
				SettingsManager.TABLE_MARKETORDER_VOLREMAINING_WIDTH,
				86,
				Long.class,
				Table.INTEGER_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Minimum volume",
				"minVolume",
				SettingsManager.TABLE_MARKETORDER_MINVOLUME_VISIBLE,
				false,
				SettingsManager.TABLE_MARKETORDER_MINVOLUME_WIDTH,
				50,
				Long.class,
				Table.INTEGER_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Station",
				"stationName",
				SettingsManager.TABLE_MARKETORDER_STATION_VISIBLE,
				true,
				SettingsManager.TABLE_MARKETORDER_STATION_WIDTH,
				236,
				String.class
		));
		
		add(new Column(
				settingsManager,
				"Price",
				"orderPrice",
				SettingsManager.TABLE_MARKETORDER_PRICE_VISIBLE,
				true,
				SettingsManager.TABLE_MARKETORDER_PRICE_WIDTH,
				137,
				BigDecimal.class,
				Table.CURRENCY_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Issued", 
				"issued",
				SettingsManager.TABLE_MARKETORDER_ISSUED_VISIBLE,
				false,
				SettingsManager.TABLE_MARKETORDER_ISSUED_WIDTH,
				100, 
				Timestamp.class,
				Table.DATE_TIME_RENDERER
		));
	}
	
}
