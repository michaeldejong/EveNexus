package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.datamodel.IPeriodFilter;
import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.gui.tables.datamodel.ITypeNameFilter;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.utils.SettingsManager;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TransactionTableDataModel implements ITableDataModel, ITypeNameFilter, IPeriodFilter {

	private final static Logger LOG = LoggerFactory.getLogger(TransactionTableDataModel.class);

	private final Database database;
	
	private long period;
	private String typeName;
	
	@Inject
	public TransactionTableDataModel(SettingsManager settingsManager, Database database) {
		this.database = database;
		
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
	public void setTypeName(String name) {
		if (name == null || name.isEmpty()) {
			typeName = "%";
			return;
		}
		typeName = "%" + name.toLowerCase() + "%";
	}

	@Override
	public void setPeriod(int days) {
		period = days;
	}

}
