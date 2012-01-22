package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.datamodel.IPeriodFilter;
import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.gui.tables.datamodel.ITypeNameFilter;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.utils.SettingsManager;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This {@link ITableDataModel} defines the data model for transactions.
 * 
 * @author michael
 */
public class TransactionTableDataModel implements ITableDataModel, ITypeNameFilter, IPeriodFilter {

	private static final Logger LOG = LoggerFactory.getLogger(TransactionTableDataModel.class);

	private final Database database;
	private final BugReportDialog dialog;
	private final SettingsManager settingsManager;
	
	private long period;
	private String typeName;
	
	/**
	 * Constructs a new {@link TransactionTableDataModel} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 * 
	 * @param dialog
	 * 		The {@link BugReportDialog}.
	 */
	@Inject
	public TransactionTableDataModel(SettingsManager settingsManager, Database database, BugReportDialog dialog) {
		this.database = database;
		this.dialog = dialog;
		this.settingsManager = settingsManager;
	}
	
	/**
	 * This method initializes the {@link TransactionTableDataModel}.
	 */
	public void initialize() {	
		setTypeName(null);
		setPeriod(settingsManager.loadInt(SettingsManager.FILTER_TRANSACTION_PERIOD, IPeriodFilter.WEEK));
	}
	
	@Override
	public List<Object[]> reload() {
		try {
			return loadTable();
		}
		catch (HibernateException e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
			throw e;
		}
	}

	private List<Object[]> loadTable() throws HibernateException {
		final String sql = new StringBuilder()
		.append("SELECT ")
		.append("transactionDateTime, ")
		.append("quantity, ")
		.append("typeName, ")
		.append("price, ")
		.append("taxes * quantity AS totaltax, ")
		.append("quantity * price AS totalnotax, ")
		.append("(price + taxes) * quantity AS totalwithtax, ")
		.append("clientName, ")
		.append("stationName ")
		.append("FROM transactions ")
		.append("WHERE LCASE(typeName) LIKE ? ")
		.append("AND transactionDateTime > DATEADD('DAY', ?, CURRENT_TIMESTAMP()) ")
		.append("ORDER BY transactionDateTime DESC, transactionID DESC")
		.toString();
		
		List<Object[]> result = new ArrayList<Object[]>();
		Session session = database.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setString(0, typeName);
		query.setLong(1, period * -1);
		ScrollableResults resultSet = query.scroll();
		if (resultSet.first()) {
			do {
				result.add(resultSet.get().clone());
			}
			while (resultSet.next());
		}
		return result;
	}

	@Override
	public String[] getFields() {
		return new String[] { 
				"transactiondatetime", "quantity", "typename", "price", "totaltax",
				"totalnotax", "totalwithtax", "clientname", "stationname"
		};
	}

	@Override
	public final void setTypeName(String name) {
		if (name == null || name.isEmpty()) {
			typeName = "%";
			return;
		}
		typeName = "%" + name.toLowerCase(Locale.US) + "%";
	}

	@Override
	public final void setPeriod(int days) {
		period = days;
	}

	@Override
	public void delete(Map<String, Object> row) {
		throw new UnsupportedOperationException();
	}

}
