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
		.append("type_name as typeName, ")
		.append("date, ")
		.append("quantity, ")
		.append("gross_profit AS value, ")
		.append("taxes, ")
		.append("net_profit AS profit, ")
		.append("total_gross_profit AS totalValue, ")
		.append("total_taxes AS totalTaxes, ")
		.append("total_net_profit AS totalProfit, ")
		.append("percental_gross_profit AS percentalValue, ")
		.append("percental_net_profit AS percentalProfit ")
		.append("FROM profits ")
		.append("WHERE LCASE(type_name) LIKE ? ")
		.append("AND date > DATEADD('DAY', ?, CURRENT_TIMESTAMP()) ")
		.append("ORDER BY date DESC, sell_Transaction_Id DESC")
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
		return new String[] {"typeName", "date", "quantity", "value", "taxes", "profit",
				"totalValue", "totalTaxes", "totalProfit", "percentalValue", "percentalProfit" };
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
