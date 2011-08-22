package nl.minicom.evenexus.gui.tables.columns.models;


import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.persistence.dao.Profit;
import nl.minicom.evenexus.utils.SettingsManager;

public class ProfitColumnModel extends ColumnModel {

	@Inject
	public ProfitColumnModel(SettingsManager settingsManager) {
		super(settingsManager);
	}
	
	@Override
	public void initialize() {
		add(new Column(
				getSettingsManager(),
				"Date", 
				Profit.DATE, 
				SettingsManager.TABLE_PROFIT_DATE_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_DATE_WIDTH,
				130, 
				Timestamp.class,
				ColumnModel.DATE_TIME
		));
		
		add(new Column(
				getSettingsManager(),
				"Item", 
				Profit.TYPE_NAME, 
				SettingsManager.TABLE_PROFIT_ITEM_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_ITEM_WIDTH,
				323,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Quantity", 
				Profit.QUANTITY, 
				SettingsManager.TABLE_PROFIT_QUANTITY_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_QUANTITY_WIDTH,
				74,
				Long.class,
				ColumnModel.INTEGER
		));
		
		add(new Column(
				getSettingsManager(),
				"Profit per unit (exc. taxes)", 
				Profit.GROSS_PROFIT, 
				SettingsManager.TABLE_PROFIT_UNIT_PROFIT_WITHOUT_TAXES_VISIBLE,
				false,
				SettingsManager.TABLE_PROFIT_UNIT_PROFIT_WITHOUT_TAXES_WIDTH,
				100, 
				BigDecimal.class, 
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Total profit (exc. taxes)", 
				Profit.TOTAL_GROSS_PROFIT, 
				SettingsManager.TABLE_PROFIT_TOTAL_PROFIT_WITHOUT_TAXES_VISIBLE,
				false,
				SettingsManager.TABLE_PROFIT_TOTAL_PROFIT_WITHOUT_TAXES_WIDTH,
				100, 
				BigDecimal.class,  
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Percental profit (exc. taxes)", 
				Profit.PERCENTAL_GROSS_PROFIT, 
				SettingsManager.TABLE_PROFIT_PERCENTAL_PROFIT_WITHOUT_TAXES_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_PERCENTAL_PROFIT_WITHOUT_TAXES_WIDTH,
				128, 
				BigDecimal.class, 
				ColumnModel.PERCENTAGE
		));
		
		add(new Column(
				getSettingsManager(),
				"Profit per unit (inc. taxes)", 
				Profit.NET_PROFIT, 
				SettingsManager.TABLE_PROFIT_UNIT_PROFIT_WITH_TAXES_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_UNIT_PROFIT_WITH_TAXES_WIDTH,
				128, 
				BigDecimal.class,  
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Total profit (inc. taxes)", 
				Profit.TOTAL_NET_PROFIT, 
				SettingsManager.TABLE_PROFIT_TOTAL_PROFIT_WITH_TAXES_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_TOTAL_PROFIT_WITH_TAXES_WIDTH,
				128, 
				BigDecimal.class, 
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Percental profit (inc. taxes)", 
				Profit.PERCENTAL_NET_PROFIT, 
				SettingsManager.TABLE_PROFIT_PERCENTAL_PROFIT_WITH_TAXES_VISIBLE,
				true,
				SettingsManager.TABLE_PROFIT_PERCENTAL_PROFIT_WITH_TAXES_WIDTH,
				128, 
				BigDecimal.class, 
				ColumnModel.PERCENTAGE
		));
		
		add(new Column(
				getSettingsManager(),
				"Taxes per unit", 
				Profit.TAXES, 
				SettingsManager.TABLE_PROFIT_UNIT_TAXES_VISIBLE,
				false,
				SettingsManager.TABLE_PROFIT_UNIT_TAXES_WIDTH,
				100, 
				BigDecimal.class, 
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Total taxes", 
				Profit.TOTAL_TAXES, 
				SettingsManager.TABLE_PROFIT_TOTAL_TAXES_VISIBLE,
				false,
				SettingsManager.TABLE_PROFIT_TOTAL_TAXES_WIDTH,
				100, 
				BigDecimal.class, 
				ColumnModel.CURRENCY
		));

	}
	
}
