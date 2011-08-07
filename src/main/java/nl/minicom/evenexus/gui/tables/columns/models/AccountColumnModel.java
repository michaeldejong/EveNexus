package nl.minicom.evenexus.gui.tables.columns.models;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.utils.SettingsManager;

public class AccountColumnModel extends ColumnModel {

	@Inject
	public AccountColumnModel(SettingsManager settingsManager) {
		super(settingsManager);
	}

	@Override
	public void initialize() {
		add(new Column(
				getSettingsManager(), 
				"Character", 
				"name", 
				SettingsManager.TABLE_ACCOUNT_CHARACTER_VISIBLE,
				true,
				SettingsManager.TABLE_ACCOUNT_CHARACTER_WIDTH,
				174,
				String.class
		));
		
		add(new Column(
				getSettingsManager(), 
				"User ID", 
				"userid", 
				SettingsManager.TABLE_ACCOUNT_USERID_VISIBLE,
				true,
				SettingsManager.TABLE_ACCOUNT_USERID_WIDTH,
				88,
				Long.class,
				ColumnModel.ALIGN_RIGHT
		));
		
		add(new Column(
				getSettingsManager(), 
				"API Key", 
				"apikey", 
				SettingsManager.TABLE_ACCOUNT_APIKEY_VISIBLE,
				true,
				SettingsManager.TABLE_ACCOUNT_APIKEY_WIDTH,
				450,
				String.class
		));
		
		add(new Column(
				getSettingsManager(), 
				"Character ID", 
				"charid", 
				SettingsManager.TABLE_ACCOUNT_CHARID_VISIBLE,
				true,
				SettingsManager.TABLE_ACCOUNT_CHARID_WIDTH,
				88,
				Long.class,
				ColumnModel.ALIGN_RIGHT
		));
	}
	
}
