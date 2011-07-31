package nl.minicom.evenexus.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class SettingsManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(SettingsManager.class);
		
	private final BugReportDialog dialog;
	private final SortedProperties settings;
	private final File file;
	
	@Inject
	public SettingsManager(BugReportDialog dialog) {
		this.dialog = dialog;
		settings = new SortedProperties();
		file = new File("session.properties");
	}
	
	public void initialize() throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		}
		
		FileReader reader = new FileReader(file);
		settings.load(reader);
		reader.close();
	}
	
	public int loadInt(String name, int defaultValue) {
		Object result = loadObject(name);
		if (result != null) {
			try {
				return Integer.parseInt((String) result);
			}
			catch (Exception e) {
				LOG.warn(e.getLocalizedMessage(), e);
			}
		}	
		saveObject(name, defaultValue);		
		return defaultValue;
	}

	public boolean loadBoolean(String name, boolean defaultValue) {
		Object result = loadObject(name);
		if (result != null) {
			return Boolean.parseBoolean((String) result);
		}
		saveObject(name, defaultValue);
		return defaultValue;
	}

	public long loadLong(String name, long defaultValue) {
		Object result = loadObject(name);
		if (result != null) {
			return Long.parseLong((String) result);
		}
		saveObject(name, defaultValue);
		return defaultValue;
	}

	public String loadString(String name, String defaultValue) {
		Object result = loadObject(name);
		if (result != null) {
			return result.toString();
		}
		saveObject(name, defaultValue);
		return defaultValue;
	}
	
	private String loadObject(String name) {
		try {
			if (settings.containsKey(name)) {
				return settings.getProperty(name);
			}
		}
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
		}
		return null;
	}
	
	public void saveObject(String name, Boolean value) {
		saveObject(name, Boolean.toString(value));
	}
	
	public void saveObject(String name, Integer value) {
		saveObject(name, Integer.toString(value));
	}
	
	public void saveObject(String name, Long value) {
		saveObject(name, Long.toString(value));
	}
	
	public void saveObject(String name, String value) {
		try {
			settings.put(name, value);
			save();
		}
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
		}
	}
	
	public void save() {
		try {
			int newVersion = 1;
			if (loadInt(SETTINGS_VERSION, 1) < newVersion) {
				saveObject(SETTINGS_VERSION, newVersion);
			}
			FileOutputStream out = new FileOutputStream(file);
			settings.store(out, null);
			out.flush();
			out.close();
		}
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
		}
	}
	
	public class SortedProperties extends Properties {

		private static final long serialVersionUID = 7671073468823707074L;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Enumeration keys() {
			Enumeration keysEnum = super.keys();
			Vector keyList = new Vector();
			while(keysEnum.hasMoreElements()){
				keyList.add(keysEnum.nextElement());
			}
			Collections.sort(keyList);
			return keyList.elements();
		}
		
	}

	public static final String APPLICATION_HEIGHT = "application.height";
	public static final String APPLICATION_MAXIMIZED = "application.maximized";
	public static final String APPLICATION_WIDTH = "application.width";
	public static final String APPLICATION_X = "application.x";
	public static final String APPLICATION_Y = "application.y";
	
	public static final String DASHBOARD_GRAPH_PURCHASES_VISIBLE = "dashboard.graph.purchases.visible";
	public static final String DASHBOARD_GRAPH_SALES_VISIBLE = "dashboard.graph.sales.visible";
	public static final String DASHBOARD_GRAPH_PROFITS_VISIBLE = "dashboard.graph.profits.visible";
	public static final String DASHBOARD_GRAPH_TAXES_VISIBLE = "dashboard.graph.taxes.visible";
	
	public static final String FILTER_DASHBOARD_PERIOD = "filter.dashboard.period";
	public static final String FILTER_JOURNAL_PERIOD = "filter.journal.period";
	public static final String FILTER_PROFIT_PERIOD = "filter.profit.period";
	public static final String FILTER_TRANSACTION_PERIOD = "filter.transaction.period";

	public static final String PROXY_ENABLED = "proxy.enabled";
	public static final String PROXY_HOST = "proxy.host";
	public static final String PROXY_PORT = "proxy.port";

	public static final String SETTINGS_VERSION = "settings.version";
		
	public static final String TABLE_ACCOUNT_APIKEY_VISIBLE = "table.account.apikey.visible";
	public static final String TABLE_ACCOUNT_APIKEY_WIDTH = "table.account.apikey.width";
	public static final String TABLE_ACCOUNT_CHARACTER_VISIBLE = "table.account.character.visible";
	public static final String TABLE_ACCOUNT_CHARACTER_WIDTH = "table.account.character.width";
	public static final String TABLE_ACCOUNT_CHARID_VISIBLE = "table.account.charid.visible";
	public static final String TABLE_ACCOUNT_CHARID_WIDTH = "table.account.charid.width";
	public static final String TABLE_ACCOUNT_USERID_VISIBLE = "table.account.userid.visible";
	public static final String TABLE_ACCOUNT_USERID_WIDTH = "table.account.userid.width";

	public static final String TABLE_JOURNAL_AMOUNT_VISIBLE = "table.journal.amount.visible";
	public static final String TABLE_JOURNAL_AMOUNT_WIDTH = "table.journal.amount.width";
	public static final String TABLE_JOURNAL_BALANCE_VISIBLE = "table.journal.balance.visible";
	public static final String TABLE_JOURNAL_BALANCE_WIDTH = "table.journal.balance.width";
	public static final String TABLE_JOURNAL_DATE_VISIBLE = "table.journal.date.visible";
	public static final String TABLE_JOURNAL_DATE_WIDTH = "table.journal.date.width";
	public static final String TABLE_JOURNAL_RECIPIENT_VISIBLE = "table.journal.recipient.visible";
	public static final String TABLE_JOURNAL_RECIPIENT_WIDTH = "table.journal.recipient.width";
	public static final String TABLE_JOURNAL_REASON_VISIBLE = "table.journal.reason.visible";
	public static final String TABLE_JOURNAL_REASON_WIDTH = "table.journal.reason.width";
	public static final String TABLE_JOURNAL_SENDER_VISIBLE = "table.journal.sender.visible";
	public static final String TABLE_JOURNAL_SENDER_WIDTH = "table.journal.sender.width";
	public static final String TABLE_JOURNAL_TAXED_VISIBLE = "table.journal.taxed.visible";
	public static final String TABLE_JOURNAL_TAXED_WIDTH = "table.journal.taxed.width";
	public static final String TABLE_JOURNAL_TYPE_VISIBLE = "table.journal.type.visible";
	public static final String TABLE_JOURNAL_TYPE_WIDTH = "table.journal.type.width";
	
	public static final String TABLE_MARKETORDER_ISSUED_VISIBLE = "table.marketorder.issued.visible";
	public static final String TABLE_MARKETORDER_ISSUED_WIDTH = "table.marketorder.issued.width";
	public static final String TABLE_MARKETORDER_ITEM_VISIBLE = "table.marketorder.item.visible";
	public static final String TABLE_MARKETORDER_ITEM_WIDTH = "table.marketorder.item.width";
	public static final String TABLE_MARKETORDER_MINVOLUME_VISIBLE = "table.marketorder.minvolume.visible";
	public static final String TABLE_MARKETORDER_MINVOLUME_WIDTH = "table.marketorder.minvolume.width";
	public static final String TABLE_MARKETORDER_PRICE_VISIBLE = "table.marketorder.price.visible";
	public static final String TABLE_MARKETORDER_PRICE_WIDTH = "table.marketorder.price.width";
	public static final String TABLE_MARKETORDER_STATION_VISIBLE = "table.marketorder.station.visible";
	public static final String TABLE_MARKETORDER_STATION_WIDTH = "table.marketorder.station.width";
	public static final String TABLE_MARKETORDER_VOLENTERED_VISIBLE = "table.marketorder.volentered.visible";
	public static final String TABLE_MARKETORDER_VOLENTERED_WIDTH = "table.marketorder.volentered.width";
	public static final String TABLE_MARKETORDER_VOLREMAINING_VISIBLE = "table.marketorder.volremaining.visible";
	public static final String TABLE_MARKETORDER_VOLREMAINING_WIDTH = "table.marketorder.volremaining.width";

	public static final String TABLE_PROFIT_DATE_VISIBLE = "table.profit.date.visible";
	public static final String TABLE_PROFIT_DATE_WIDTH = "table.profit.date.width";
	public static final String TABLE_PROFIT_ITEM_VISIBLE = "table.profit.item.visible";
	public static final String TABLE_PROFIT_ITEM_WIDTH = "table.profit.item.width";
	public static final String TABLE_PROFIT_TOTAL_PROFIT_WITH_TAXES_VISIBLE = "table.profit.total.profit.with.taxes.visible";
	public static final String TABLE_PROFIT_TOTAL_PROFIT_WITH_TAXES_WIDTH = "table.profit.total.profit.with.taxes.width";
	public static final String TABLE_PROFIT_TOTAL_PROFIT_WITHOUT_TAXES_VISIBLE = "table.profit.total.profit.without.taxes.visible";
	public static final String TABLE_PROFIT_TOTAL_PROFIT_WITHOUT_TAXES_WIDTH = "table.profit.total.profit.without.taxes.width";
	public static final String TABLE_PROFIT_TOTAL_TAXES_VISIBLE = "table.profit.total.taxes.visible";
	public static final String TABLE_PROFIT_TOTAL_TAXES_WIDTH = "table.profit.total.taxes.width";
	public static final String TABLE_PROFIT_QUANTITY_VISIBLE = "table.profit.quantity.visible";
	public static final String TABLE_PROFIT_QUANTITY_WIDTH = "table.profit.quantity.width";
	public static final String TABLE_PROFIT_UNIT_PROFIT_WITH_TAXES_VISIBLE = "table.profit.profit.unit.profit.with.taxes.visible";
	public static final String TABLE_PROFIT_UNIT_PROFIT_WITH_TAXES_WIDTH = "table.profit.profit.unit.profit.with.taxes.width";
	public static final String TABLE_PROFIT_UNIT_PROFIT_WITHOUT_TAXES_VISIBLE = "table.profit.unit.profit.without.taxes.visible";
	public static final String TABLE_PROFIT_UNIT_PROFIT_WITHOUT_TAXES_WIDTH = "table.profit.unit.profit.without.taxes.width";
	public static final String TABLE_PROFIT_UNIT_TAXES_VISIBLE = "table.profit.unit.taxes.visible";
	public static final String TABLE_PROFIT_UNIT_TAXES_WIDTH = "table.profit.unit.taxes.width";
	
	public static final String TABLE_TRANSACTION_CLIENT_VISIBLE = "table.transaction.client.visible";
	public static final String TABLE_TRANSACTION_CLIENT_WIDTH = "table.transaction.client.width";
	public static final String TABLE_TRANSACTION_DATE_VISIBLE = "table.transaction.date.visible";
	public static final String TABLE_TRANSACTION_DATE_WIDTH = "table.transaction.date.width";
	public static final String TABLE_TRANSACTION_ITEM_VISIBLE = "table.transaction.item.visible";
	public static final String TABLE_TRANSACTION_ITEM_WIDTH = "table.transaction.item.width";
	public static final String TABLE_TRANSACTION_PRICE_VISIBLE = "table.transaction.price.visible";
	public static final String TABLE_TRANSACTION_PRICE_WIDTH = "table.transaction.price.width";
	public static final String TABLE_TRANSACTION_STATION_VISIBLE = "table.transaction.station.visible";
	public static final String TABLE_TRANSACTION_STATION_WIDTH = "table.transaction.station.width";
	public static final String TABLE_TRANSACTION_TAXES_VISIBLE = "table.transaction.taxes.visible";
	public static final String TABLE_TRANSACTION_TAXES_WIDTH = "table.transaction.taxes.width";
	public static final String TABLE_TRANSACTION_TOTAL_WITHOUT_TAXES_VISIBLE = "table.transaction.total.without.taxes.visible";
	public static final String TABLE_TRANSACTION_TOTAL_WITHOUT_TAXES_WIDTH = "table.transaction.total.without.taxes.width";
	public static final String TABLE_TRANSACTION_TOTAL_WITH_TAXES_VISIBLE = "table.transaction.total.with.taxes.visible";
	public static final String TABLE_TRANSACTION_TOTAL_WITH_TAXES_WIDTH = "table.transaction.total.with.taxes.width";
	public static final String TABLE_TRANSACTION_QUANTITY_VISIBLE = "table.transaction.quantity.visible";
	public static final String TABLE_TRANSACTION_QUANTITY_WIDTH = "table.transaction.quantity.width";

	public static final String USER_DEFINED_API_SERVER_ENABLED = "userdefinedapiserver.enabled";
	public static final String USER_DEFINED_API_SERVER_HOST = "userdefinedapiserver.host";
	
}
