package nl.minicom.evenexus.gui.panels.dashboard;

import java.sql.SQLException;

import org.jfree.chart.renderer.xy.XYItemRenderer;

public interface GraphElement {
	
	public void reload() throws SQLException;
	
	public boolean isVisible();
	
	public void setRenderer(XYItemRenderer renderer, int index);
	
	public double getValue(int daysAgo);

	public String getName();

	public void setVisible(Boolean value);
	
}
