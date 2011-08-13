package nl.minicom.evenexus.gui.panels.dashboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.WalletTransaction;
import nl.minicom.evenexus.utils.SettingsManager;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.jfree.chart.renderer.xy.XYItemRenderer;

public class TaxesGraphElement implements GraphElement {
	
	private static final String VISIBLE_SETTING = SettingsManager.DASHBOARD_GRAPH_TAXES_VISIBLE;

	private final SettingsManager settingsManager;
	private final Database database;
	private final Map<Integer, Double> data;
	
	@Inject
	public TaxesGraphElement(SettingsManager settingsManager, Database database) {
		this.data = new TreeMap<Integer, Double>();
		this.settingsManager = settingsManager;
		this.database = database;
	}
	
	@Override
	public boolean isVisible() {
		return settingsManager.loadBoolean(VISIBLE_SETTING, true);
	}

	@Override
	public void setVisible(Boolean value) {
		settingsManager.saveObject(VISIBLE_SETTING, value);
	}

	@Override
	public void reload() {
		Session session = database.getCurrentSession();
		
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ");
		queryBuilder.append("	ABS(SUM(quantity * taxes)), ");
		queryBuilder.append("	day ");
		queryBuilder.append("FROM (");
		queryBuilder.append("	SELECT ");
		queryBuilder.append("		" + WalletTransaction.QUANTITY + " AS quantity, ");
		queryBuilder.append("		" + WalletTransaction.TAXES + " AS taxes, ");
		queryBuilder.append("		DAY_OF_YEAR(CURRENT_TIMESTAMP()) - DAY_OF_YEAR(" + WalletTransaction.TRANSACTION_DATE_TIME + ") AS day ");
		queryBuilder.append("	FROM transactions ");
		queryBuilder.append("	WHERE " + WalletTransaction.TRANSACTION_DATE_TIME + " > DATEADD('DAY', ?, CURRENT_TIMESTAMP()) ");
		queryBuilder.append(") ");
		queryBuilder.append("GROUP BY day ");
		queryBuilder.append("ORDER BY day ASC");
		
		SQLQuery query = session.createSQLQuery(queryBuilder.toString());
		query.setLong(0, -28);

		ScrollableResults result = query.scroll();
		if (result.first()) {
			data.clear();
			do {
				double taxes = ((BigDecimal) result.get(0)).doubleValue();
				int daysAgo = (Integer) result.get(1);
				data.put(daysAgo, taxes);
			}
			while (result.next());
		}
	}

	@Override
	public void setRenderer(XYItemRenderer renderer, int index) {
		renderer.setSeriesPaint(index, new Color(150, 150, 150));
		renderer.setSeriesShape(index, new Ellipse2D.Double(-2, -2, 4, 4));
		renderer.setSeriesStroke(index, new BasicStroke(2f));
	}

	@Override
	public double getValue(int daysAgo) {
		if (data.containsKey(daysAgo)) {
			return data.get(daysAgo);
		}
		return 0.0;
	}

	@Override
	public String getName() {
		return "Taxes";
	}

}
