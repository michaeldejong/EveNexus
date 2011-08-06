package nl.minicom.evenexus.gui.panels.dashboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.interceptor.Transactional;
import nl.minicom.evenexus.utils.SettingsManager;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.jfree.chart.renderer.xy.XYItemRenderer;

public class ProfitGraphElement implements GraphElement {
	
	private static final String VISIBLE_SETTING = SettingsManager.DASHBOARD_GRAPH_PROFITS_VISIBLE;

	private final SettingsManager settingsManager;
	private final Database database;
	
	private final Map<Integer, Double> data;
	private boolean isVisible;
	
	@Inject
	public ProfitGraphElement(SettingsManager settingsManager, Database database) {
		this.settingsManager = settingsManager;
		this.database = database;
		
		this.data = new TreeMap<Integer, Double>();
		this.isVisible = settingsManager.loadBoolean(VISIBLE_SETTING, true);
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setVisible(Boolean value) {
		isVisible = value;
		settingsManager.saveObject(VISIBLE_SETTING, value);
	}

	@Override
	@Transactional
	public void reload() {
		Session session = database.getCurrentSession();
		// TODO refactor using objects and criteria api?
		SQLQuery query = session.createSQLQuery("SELECT SUM(total_net_profit) AS totalProfit, day FROM (SELECT total_net_profit, date, DAY_OF_YEAR(CURRENT_TIMESTAMP()) - DAY_OF_YEAR(date) AS day FROM profits WHERE date > DATEADD('DAY', ?, CURRENT_TIMESTAMP())) AS a GROUP BY day ORDER BY day ASC");
		query.setLong(0, -28);

		ScrollableResults result = query.scroll();
		if (result.first()) {
			data.clear();
			do {
				double profit = ((BigDecimal) result.get(0)).doubleValue();
				int daysAgo = (Integer) result.get(1);
				data.put(daysAgo, profit);
			}
			while (result.next());
		}
	}

	@Override
	public void setRenderer(XYItemRenderer renderer, int index) {
		renderer.setSeriesPaint(index, new Color(0, 200, 0));
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
		return "Profit";
	}

}
