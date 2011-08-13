package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.datamodel.IPeriodFilter;
import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.gui.tables.datamodel.ITypeNameFilter;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.Profit;
import nl.minicom.evenexus.persistence.dao.ProfitIdentifier;
import nl.minicom.evenexus.utils.SettingsManager;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProfitTableDataModel implements ITableDataModel, ITypeNameFilter, IPeriodFilter {

	private static final Logger LOG = LoggerFactory.getLogger(ProfitTableDataModel.class);

	private final Database database;
	private final BugReportDialog dialog;
	private final SettingsManager settingsManager;
	
	private int period;
	private String typeName;
	
	@Inject
	public ProfitTableDataModel(SettingsManager settingsManager, Database database, BugReportDialog dialog) {
		this.database = database;
		this.dialog = dialog;
		this.settingsManager = settingsManager;
	}
	
	public void initialize() {
		setTypeName(null);
		setPeriod(settingsManager.loadInt(SettingsManager.FILTER_PROFIT_PERIOD, IPeriodFilter.WEEK));
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
		// TODO refactor to work with objects?
		final String sql = new StringBuilder()
		.append("SELECT ")
		.append(Profit.TYPE_NAME + ", ")
		.append(Profit.DATE + ", ")
		.append(Profit.QUANTITY + ", ")
		.append(Profit.GROSS_PROFIT + ", ")
		.append(Profit.TAXES + ", ")
		.append(Profit.NET_PROFIT + ", ")
		.append(Profit.TOTAL_GROSS_PROFIT + ", ")
		.append(Profit.TOTAL_TAXES + ", ")
		.append(Profit.TOTAL_NET_PROFIT + ", ")
		.append(Profit.PERCENTAL_GROSS_PROFIT + ", ")
		.append(Profit.PERCENTAL_NET_PROFIT + " ")
		.append("FROM profits ")
		.append("WHERE LCASE(" + Profit.TYPE_NAME + ") LIKE ? ")
		.append("AND " + Profit.DATE + " > DATEADD('DAY', ?, CURRENT_TIMESTAMP()) ")
		.append("ORDER BY " + Profit.DATE + " DESC, " + ProfitIdentifier.SELL_TRANSACTION_ID + " DESC")
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
				Profit.TYPE_NAME,
				Profit.DATE,
				Profit.QUANTITY,
				Profit.GROSS_PROFIT,
				Profit.TAXES,
				Profit.NET_PROFIT,
				Profit.TOTAL_GROSS_PROFIT,
				Profit.TOTAL_TAXES,
				Profit.TOTAL_NET_PROFIT,
				Profit.PERCENTAL_GROSS_PROFIT,
				Profit.PERCENTAL_NET_PROFIT
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

}
