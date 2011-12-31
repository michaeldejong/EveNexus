package nl.minicom.evenexus.gui.tables.columns.models;


import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.utils.SettingsManager;

/**
 * This class defines all the available columns in the journals table.
 * 
 * @author michael
 */
public class JournalColumnModel extends ColumnModel {

	/**
	 * Constructs a new {@link JournalColumnModel}.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 */
	@Inject
	public JournalColumnModel(SettingsManager settingsManager) {
		super(settingsManager);
	}
	
	@Override
	public void initialize() {
		add(new Column(
				getSettingsManager(),
				"Date", 
				"date", 
				SettingsManager.JOURNAL_DATE_VISIBLE, 
				true, 
				SettingsManager.JOURNAL_DATE_WIDTH, 
				147,
				Timestamp.class,
				ColumnModel.DATE_TIME
		));
		
		add(new Column(
				getSettingsManager(),
				"Type", 
				"description", 
				SettingsManager.JOURNAL_TYPE_VISIBLE, 
				true,
				SettingsManager.JOURNAL_TYPE_WIDTH,
				196,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Sender", 
				"ownerName1", 
				SettingsManager.JOURNAL_SENDER_VISIBLE, 
				true,
				SettingsManager.JOURNAL_SENDER_WIDTH,
				137,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Recipient", 
				"ownerName2", 
				SettingsManager.JOURNAL_RECIPIENT_VISIBLE, 
				true,
				SettingsManager.JOURNAL_RECIPIENT_WIDTH,
				137,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Amount", 
				"amount", 
				SettingsManager.JOURNAL_AMOUNT_VISIBLE, 
				true,
				SettingsManager.JOURNAL_AMOUNT_WIDTH,
				166,
				Long.class, 
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Balance", 
				"balance", 
				SettingsManager.JOURNAL_BALANCE_VISIBLE, 
				false,
				SettingsManager.JOURNAL_BALANCE_WIDTH,
				100, 
				BigDecimal.class,
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Reason", 
				"reason", 
				SettingsManager.JOURNAL_REASON_VISIBLE, 
				false,
				SettingsManager.JOURNAL_REASON_WIDTH,
				350,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Taxed", 
				"taxAmount", 
				SettingsManager.JOURNAL_TAXED_VISIBLE, 
				false,
				SettingsManager.JOURNAL_TAXED_WIDTH,
				100,
				BigDecimal.class,
				ColumnModel.CURRENCY
		));
	}
	
}
