package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.gui.tables.datamodel.IPeriodFilter;
import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.utils.SettingsManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;


public class JournalTableDataModel implements ITableDataModel, IPeriodFilter {
	
	private static final Logger logger = LogManager.getRootLogger();

	private int period;
	
	public JournalTableDataModel(SettingsManager settingsManager) {
		setPeriod(settingsManager.loadInt(SettingsManager.FILTER_JOURNAL_PERIOD, IPeriodFilter.WEEK));
	}
	
	@Override
	public List<Object[]> reload() {
		try {
			return loadTable();			
		}
		catch (HibernateException e) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}

	private List<Object[]> loadTable() throws HibernateException {
		final String sql = new StringBuilder()
		.append("SELECT ")
		.append("journal.date, ")
		.append("journal.ownername1, ")
		.append("journal.ownername2, ")
		.append("journal.argname1, ")
		.append("journal.amount, ")
		.append("journal.balance, ")
		.append("journal.reason, ")
		.append("journal.taxamount, ")
		.append("reftypes.description ")
		.append("FROM journal, reftypes ")
		.append("WHERE journal.journalTypeID = reftypes.refTypeID ")
		.append("AND journal.date > DATEADD('DAY', ?, CURRENT_TIMESTAMP()) ")
		.append("ORDER BY journal.date DESC, journal.refID DESC")
		.toString();
		
		return new Query<List<Object[]>>() {
			@Override
			protected List<Object[]> doQuery(Session session) {
				List<Object[]> result = new ArrayList<Object[]>();
				SQLQuery query = session.createSQLQuery(sql);
				query.setLong(0, period * -1);
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
		return new String[] {
				"date", "ownername1", "ownername2", "argname1", "amount", 
				"balance", "reason", "taxamount", "description"
		};
	}

	@Override
	public void setPeriod(int days) {
		period = days;
	}

}
