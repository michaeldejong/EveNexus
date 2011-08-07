package nl.minicom.evenexus.gui.tables.columns.models;


import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.utils.SettingsManager;

public class JournalColumnModel extends ColumnModel {

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
				SettingsManager.TABLE_JOURNAL_DATE_VISIBLE, 
				true, 
				SettingsManager.TABLE_JOURNAL_DATE_WIDTH, 
				147,
				Timestamp.class,
				ColumnModel.DATE_TIME
		));
		
		add(new Column(
				getSettingsManager(),
				"Type", 
				"description", 
				SettingsManager.TABLE_JOURNAL_TYPE_VISIBLE, 
				true,
				SettingsManager.TABLE_JOURNAL_TYPE_WIDTH,
				196,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Sender", 
				"ownerName1", 
				SettingsManager.TABLE_JOURNAL_SENDER_VISIBLE, 
				true,
				SettingsManager.TABLE_JOURNAL_SENDER_WIDTH,
				137,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Recipient", 
				"ownerName2", 
				SettingsManager.TABLE_JOURNAL_RECIPIENT_VISIBLE, 
				true,
				SettingsManager.TABLE_JOURNAL_RECIPIENT_WIDTH,
				137,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Amount", 
				"amount", 
				SettingsManager.TABLE_JOURNAL_AMOUNT_VISIBLE, 
				true,
				SettingsManager.TABLE_JOURNAL_AMOUNT_WIDTH,
				166,
				Long.class, 
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Balance", 
				"balance", 
				SettingsManager.TABLE_JOURNAL_BALANCE_VISIBLE, 
				false,
				SettingsManager.TABLE_JOURNAL_BALANCE_WIDTH,
				100, 
				BigDecimal.class,
				ColumnModel.CURRENCY
		));
		
		add(new Column(
				getSettingsManager(),
				"Reason", 
				"reason", 
				SettingsManager.TABLE_JOURNAL_REASON_VISIBLE, 
				false,
				SettingsManager.TABLE_JOURNAL_REASON_WIDTH,
				350,
				String.class
		));
		
		add(new Column(
				getSettingsManager(),
				"Taxed", 
				"taxAmount", 
				SettingsManager.TABLE_JOURNAL_TAXED_VISIBLE, 
				false,
				SettingsManager.TABLE_JOURNAL_TAXED_WIDTH,
				100,
				BigDecimal.class,
				ColumnModel.CURRENCY
		));
	}
	
}
