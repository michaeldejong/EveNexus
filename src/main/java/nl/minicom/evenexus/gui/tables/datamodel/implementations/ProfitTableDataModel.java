package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.gui.tables.datamodel.IPeriodFilter;
import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.gui.tables.datamodel.ITypeNameFilter;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.utils.SettingsManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;


public class ProfitTableDataModel implements ITableDataModel, ITypeNameFilter, IPeriodFilter {

	private final static Logger logger = LogManager.getRootLogger();

	private int period;
	private String typeName;
	
	public ProfitTableDataModel(SettingsManager settingsManager) {
		setTypeName(null);
		setPeriod(settingsManager.loadInt(SettingsManager.FILTER_PROFIT_PERIOD, IPeriodFilter.WEEK));
	}
	
	@Override
	public List<Object[]> reload() {
		try {
			return loadTable();			
		}
		catch (HibernateException e) {
			logger.error(e);
		}
		return null;
	}

	private List<Object[]> loadTable() throws HibernateException {
		final String sql = new StringBuilder()
		.append("SELECT ")
		.append("typeName, ")
		.append("date, ")
		.append("quantity, ")
		.append("value, ")
		.append("taxes, ")
		.append("value + taxes AS profit, ")
		.append("value * quantity AS totalValue, ")
		.append("taxes * quantity AS totalTaxes, ")
		.append("(value + taxes) * quantity AS totalProfit ")
		.append("FROM profit ")
		.append("WHERE LCASE(typeName) LIKE ? ")
		.append("AND date > DATEADD('DAY', ?, CURRENT_TIMESTAMP()) ")
		.append("ORDER BY date DESC, transactionID DESC")
		.toString();
		
		return new Query<List<Object[]>>() {
			@Override
			protected List<Object[]> doQuery(Session session) {
				List<Object[]> result = new ArrayList<Object[]>();
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
		}.doQuery();
	}

	@Override
	public String[] getFields() {
		return new String[] {};
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
