package nl.minicom.evenexus.gui.tables.columns.models;


import java.math.BigDecimal;
import java.sql.Timestamp;

import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.utils.SettingsManager;

public class ProfitColumnModel extends ColumnModel {

	public ProfitColumnModel(SettingsManager settingsManager) {
		add(new Column(
				settingsManager,
				"Date", 
				"date", 
				SettingsManager.TABLE_PROFIT_DATE_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_DATE_WIDTH,
				130, 
				Timestamp.class, 
				Table.DATE_TIME_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Item", 
				"typename", 
				SettingsManager.TABLE_PROFIT_ITEM_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_ITEM_WIDTH,
				323,
				String.class
		));
		
		add(new Column(
				settingsManager,
				"Quantity", 
				"quantity", 
				SettingsManager.TABLE_PROFIT_QUANTITY_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_QUANTITY_WIDTH,
				74,
				Long.class,
				Table.INTEGER_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Profit per unit (exc. taxes)", 
				"value", 
				SettingsManager.TABLE_PROFIT_UNIT_PROFIT_WITHOUT_TAXES_VISIBLE,
				false,
				SettingsManager.TABLE_PROFIT_UNIT_PROFIT_WITHOUT_TAXES_WIDTH,
				100, 
				BigDecimal.class, 
				Table.CURRENCY_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Total profit (exc. taxes)", 
				"totalValue", 
				SettingsManager.TABLE_PROFIT_TOTAL_PROFIT_WITHOUT_TAXES_VISIBLE,
				false,
				SettingsManager.TABLE_PROFIT_TOTAL_PROFIT_WITHOUT_TAXES_WIDTH,
				100, 
				BigDecimal.class,  
				Table.CURRENCY_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Taxes per unit", 
				"taxes", 
				SettingsManager.TABLE_PROFIT_UNIT_TAXES_VISIBLE,
				false,
				SettingsManager.TABLE_PROFIT_UNIT_TAXES_WIDTH,
				100, 
				BigDecimal.class, 
				Table.CURRENCY_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Total taxes", 
				"totalTaxes", 
				SettingsManager.TABLE_PROFIT_TOTAL_TAXES_VISIBLE,
				false,
				SettingsManager.TABLE_PROFIT_TOTAL_TAXES_WIDTH,
				100, 
				BigDecimal.class, 
				Table.CURRENCY_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Profit per unit (inc. taxes)", 
				"profit", 
				SettingsManager.TABLE_PROFIT_UNIT_PROFIT_WITH_TAXES_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_UNIT_PROFIT_WITH_TAXES_WIDTH,
				128, 
				BigDecimal.class,  
				Table.CURRENCY_RENDERER
		));
		
		add(new Column(
				settingsManager,
				"Total profit (inc. taxes)", 
				"totalProfit", 
				SettingsManager.TABLE_PROFIT_TOTAL_PROFIT_WITH_TAXES_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_TOTAL_PROFIT_WITH_TAXES_WIDTH,
				128, 
				BigDecimal.class, 
				Table.CURRENCY_RENDERER
		));
	}
	
}
