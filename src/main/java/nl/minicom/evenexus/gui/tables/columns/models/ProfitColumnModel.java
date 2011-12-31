package nl.minicom.evenexus.gui.tables.columns.models;


import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.persistence.dao.Profit;
import nl.minicom.evenexus.utils.SettingsManager;

/**
 * This {@link ColumnModel} defines all the available columns for the profit table.
 * 
 * @author michael
 */
public class ProfitColumnModel extends ColumnModel {

	/**
	 * Constructs a new {@link ProfitColumnModel}.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 */
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
				SettingsManager.PROFIT_DATE_VISIBLE,
				true,
				SettingsManager.PROFIT_DATE_WIDTH,
				130, 
				Timestamp.class,
				ColumnModel.DATE_TIME
		));
		
		add(new Column(
				getSettingsManager(),
				"Item", 
				Profit.TYPE_NAME, 
				SettingsManager.PROFIT_ITEM_VISIBLE,
				true,
				SettingsManager.PROFIT_ITEM_WIDTH,
				323,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Quantity", 
				Profit.QUANTITY, 
				SettingsManager.PROFIT_QUANTITY_VISIBLE,
				true,
				SettingsManager.PROFIT_QUANTITY_WIDTH,
				74,
				Long.class,
				ColumnModel.INTEGER
		));
		
		add(new Column(
				getSettingsManager(),
				"Profit per unit (exc. taxes)", 
				Profit.GROSS_PROFIT, 
				SettingsManager.PROFIT_UNIT_PROFIT_EXC_VISIBLE,
				false,
				SettingsManager.PROFIT_UNIT_PROFIT_EXC_WIDTH,
				100, 
				BigDecimal.class, 
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Total profit (exc. taxes)", 
				Profit.TOTAL_GROSS_PROFIT, 
				SettingsManager.PROFIT_TOTAL_PROFIT_EXC_VISIBLE,
				false,
				SettingsManager.PROFIT_TOTAL_PROFIT_EXC_WIDTH,
				100, 
				BigDecimal.class,  
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Percental profit (exc. taxes)", 
				Profit.PERCENTAL_GROSS_PROFIT, 
				SettingsManager.PROFIT_PERC_PROFIT_EXC_VISIBLE,
				true,
				SettingsManager.PROFIT_PERC_PROFIT_EXC_WIDTH,
				128, 
				BigDecimal.class, 
				ColumnModel.PERCENTAGE
		));
		
		add(new Column(
				getSettingsManager(),
				"Profit per unit (inc. taxes)", 
				Profit.NET_PROFIT, 
				SettingsManager.PROFIT_UNIT_PROFIT_INC_VISIBLE,
				true,
				SettingsManager.PROFIT_UNIT_PROFIT_INC_WIDTH,
				128, 
				BigDecimal.class,  
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Total profit (inc. taxes)", 
				Profit.TOTAL_NET_PROFIT, 
				SettingsManager.PROFIT_TOTAL_PROFIT_INC_VISIBLE,
				true,
				SettingsManager.PROFIT_TOTAL_PROFIT_INC_WIDTH,
				128, 
				BigDecimal.class, 
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Percental profit (inc. taxes)", 
				Profit.PERCENTAL_NET_PROFIT, 
				SettingsManager.PROFIT_PERC_PROFIT_INC_VISIBLE,
				true,
				SettingsManager.PROFIT_PERC_PROFIT_INC_WIDTH,
				128, 
				BigDecimal.class, 
				ColumnModel.PERCENTAGE
		));
		
		add(new Column(
				getSettingsManager(),
				"Taxes per unit", 
				Profit.TAXES, 
				SettingsManager.PROFIT_UNIT_VISIBLE,
				false,
				SettingsManager.PROFIT_UNIT_WIDTH,
				100, 
				BigDecimal.class, 
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Total taxes", 
				Profit.TOTAL_TAXES, 
				SettingsManager.PROFIT_TOTAL_VISIBLE,
				false,
				SettingsManager.PROFIT_TOTAL_WIDTH,
				100, 
				BigDecimal.class, 
				ColumnModel.CURRENCY
		));

	}
	
}
