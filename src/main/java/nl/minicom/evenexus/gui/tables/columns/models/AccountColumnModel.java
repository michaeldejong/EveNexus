package nl.minicom.evenexus.gui.tables.columns.models;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.utils.SettingsManager;

/**
 * This class defines all the columns in the accounts table.
 *
 * @author michael
 */
public class AccountColumnModel extends ColumnModel {

	/**
	 * This constructs a new {@link AccountColumnModel} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 */
	@Inject
	public AccountColumnModel(SettingsManager settingsManager) {
		super(settingsManager);
	}

	@Override
	public void initialize() {
		add(new Column(
				getSettingsManager(), 
				"KeyID", 
				"keyid", 
				SettingsManager.TABLE_ACCOUNT_KEYID_VISIBLE,
				true,
				SettingsManager.TABLE_ACCOUNT_KEYID_WIDTH,
				88,
				Long.class,
				ColumnModel.ALIGN_RIGHT
		));
		
		add(new Column(
				getSettingsManager(), 
				"Verification Code", 
				"verificationcode", 
				SettingsManager.TABLE_ACCOUNT_VCODE_VISIBLE,
				true,
				SettingsManager.TABLE_ACCOUNT_VCODE_WIDTH,
				450,
				String.class
		));
		
		add(new Column(
				getSettingsManager(), 
				"Character ID", 
				"characterid", 
				SettingsManager.TABLE_ACCOUNT_CHARID_VISIBLE,
				false,
				SettingsManager.TABLE_ACCOUNT_CHARID_WIDTH,
				88,
				Long.class,
				ColumnModel.ALIGN_RIGHT
		));
		
		add(new Column(
				getSettingsManager(), 
				"Character", 
				"charactername", 
				SettingsManager.TABLE_ACCOUNT_CHARNAME_VISIBLE,
				true,
				SettingsManager.TABLE_ACCOUNT_CHARNAME_WIDTH,
				174,
				String.class
		));
		
		add(new Column(
				getSettingsManager(), 
				"Corporation ID", 
				"corporationid", 
				SettingsManager.TABLE_ACCOUNT_CORPID_VISIBLE,
				false,
				SettingsManager.TABLE_ACCOUNT_CORPID_WIDTH,
				88,
				Long.class,
				ColumnModel.ALIGN_RIGHT
		));
		
		add(new Column(
				getSettingsManager(), 
				"Corporation", 
				"corporationname", 
				SettingsManager.TABLE_ACCOUNT_CORPNAME_VISIBLE,
				true,
				SettingsManager.TABLE_ACCOUNT_CORPNAME_WIDTH,
				174,
				String.class
		));
	}
	
}
