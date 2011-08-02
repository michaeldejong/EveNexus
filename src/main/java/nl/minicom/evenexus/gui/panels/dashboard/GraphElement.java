package nl.minicom.evenexus.gui.panels.dashboard;

import java.sql.SQLException;

import org.jfree.chart.renderer.xy.XYItemRenderer;

public interface GraphElement {
	
	void reload() throws SQLException;
	
	boolean isVisible();
	
	void setRenderer(XYItemRenderer renderer, int index);
	
	double getValue(int daysAgo);

	String getName();

	void setVisible(Boolean value);
	
}
