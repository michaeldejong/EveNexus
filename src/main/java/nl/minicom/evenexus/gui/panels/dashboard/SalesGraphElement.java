package nl.minicom.evenexus.gui.panels.dashboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.utils.SettingsManager;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.jfree.chart.renderer.xy.XYItemRenderer;

public class SalesGraphElement implements GraphElement {
	
	private static final String visibleSetting = SettingsManager.DASHBOARD_GRAPH_SALES_VISIBLE;

	private final SettingsManager settingsManager;
	private final Map<Integer, Double> data;
	private boolean isVisible;
	
	public SalesGraphElement(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
		data = new TreeMap<Integer, Double>();
		isVisible = settingsManager.loadBoolean(visibleSetting, true);
	}
	
	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setVisible(Boolean value) {
		isVisible = value;
		settingsManager.saveObject(visibleSetting, value);
	}

	@Override
	public void reload() throws SQLException {
		new Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				SQLQuery query = session.createSQLQuery("SELECT ABS(SUM(quantity * (price + taxes))) AS totalSales, day FROM (SELECT quantity, price, taxes, DAY_OF_YEAR(CURRENT_TIMESTAMP()) - DAY_OF_YEAR(transactionDateTime) AS day FROM transactions WHERE price > 0.00 AND transactionDateTime > DATEADD('DAY', ?, CURRENT_TIMESTAMP())) AS a GROUP BY day ORDER BY day ASC");
				query.setLong(0, -28);

				ScrollableResults result = query.scroll();
				if (result.first()) {
					data.clear();
					do {
						double sales = ((BigDecimal) result.get(0)).doubleValue();
						int daysAgo = (Integer) result.get(1);
						data.put(daysAgo, sales);
					}
					while (result.next());
				}
				return null;
			}
		}.doQuery();
	}

	@Override
	public void setRenderer(XYItemRenderer renderer, int index) {
		renderer.setSeriesPaint(index, new Color(0, 0, 200));
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
		return "Sales";
	}

}
