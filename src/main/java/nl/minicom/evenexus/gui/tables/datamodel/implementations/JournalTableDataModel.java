package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.datamodel.IPeriodFilter;
import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.utils.SettingsManager;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JournalTableDataModel implements ITableDataModel, IPeriodFilter {
	
	private static final Logger LOG = LoggerFactory.getLogger(JournalTableDataModel.class);

	private final Database database;
	private final BugReportDialog dialog;
	private final SettingsManager settingsManager;
	
	private int period;
	
	@Inject
	public JournalTableDataModel(SettingsManager settingsManager, Database database, BugReportDialog dialog) {
		this.database = database;
		this.settingsManager = settingsManager;
		this.dialog = dialog;
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
		.append("journal.date AS date, ")
		.append("journal.ownername1 AS ownername1, ")
		.append("journal.ownername2 AS ownername2, ")
		.append("journal.argname1 AS argname1, ")
		.append("journal.amount AS amount, ")
		.append("journal.balance AS balance, ")
		.append("journal.reason AS reason, ")
		.append("journal.taxamount AS taxamount, ")
		.append("reftypes.description AS description ")
		.append("FROM journal, reftypes ")
		.append("WHERE journal.journalTypeID = reftypes.refTypeID ")
		.append("AND journal.date > DATEADD('DAY', ?, CURRENT_TIMESTAMP()) ")
		.append("ORDER BY journal.date DESC, journal.refID DESC")
		.toString();
		
		List<Object[]> result = new ArrayList<Object[]>();
		Session session = database.getCurrentSession();
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

	@Override
	public String[] getFields() {
		return new String[] {
				"date", "ownername1", "ownername2", "argname1", "amount", 
				"balance", "reason", "taxamount", "description"
		};
	}

	public void initialize() {
		setPeriod(settingsManager.loadInt(SettingsManager.FILTER_JOURNAL_PERIOD, IPeriodFilter.WEEK));
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
