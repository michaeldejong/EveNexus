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

/**
 * This {@link GraphElement} defines all the taxes paid by all the characters.
 * 
 * @author michael
 */
public class TaxesGraphElement implements GraphElement {
	
	private static final String VISIBLE_SETTING = SettingsManager.DASHBOARD_GRAPH_VISIBLE;

	private final SettingsManager settingsManager;
	private final Database database;
	private final Map<Integer, Double> data;
	
	/**
	 * Constructs a new {@link TaxesGraphElement}.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 */
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
	public void setVisible(boolean value) {
		settingsManager.saveObject(VISIBLE_SETTING, value);
	}

	@Override
	public void reload() {
		Session session = database.getCurrentSession();
		String dateField = WalletTransaction.TRANSACTION_DATE_TIME;
		
		StringBuilder queryBuilder = new StringBuilder()
		.append("SELECT ")
		.append("	ABS(SUM(quantity * taxes)), ")
		.append("	day ")
		.append("FROM (")
		.append("	SELECT ")
		.append("		" + WalletTransaction.QUANTITY + " AS quantity, ")
		.append("		" + WalletTransaction.TAXES + " AS taxes, ")
		.append("		DAY_OF_YEAR(CURRENT_TIMESTAMP()) - DAY_OF_YEAR(" + dateField + ") AS day ")
		.append("	FROM transactions ")
		.append("	WHERE " + dateField + " > DATEADD('DAY', ?, CURRENT_TIMESTAMP()) ")
		.append(") ")
		.append("GROUP BY day ")
		.append("ORDER BY day ASC");
		
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
