package nl.minicom.evenexus.gui.panels.dashboard;

import java.sql.SQLException;

import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 * This interface allows us to define an element, which can be drawn in the graphing engine.
 * 
 * @author michael
 */
public interface GraphElement {
	
	/**
	 * This method reloads the {@link GraphElement}.
	 * 
	 * @throws SQLException
	 * 		If something went wrong while retrieving the data from the database.
	 */
	void reload() throws SQLException;
	
	/**
	 * @return
	 * 		True if this element should be visible.
	 */
	boolean isVisible();
	
	/**
	 * This method sets the renderer on a specific index.
	 * 
	 * @param renderer
	 * 		The {@link XYItemRenderer}.
	 * 
	 * @param index
	 * 		The index of the renderer.
	 */
	void setRenderer(XYItemRenderer renderer, int index);
	
	/**
	 * This method returns the value of the element at the specified time.
	 * 
	 * @param daysAgo
	 * 		The amount of days ago.
	 * 
	 * @return
	 * 		The value of the element at that time.
	 */
	double getValue(int daysAgo);

	/**
	 * @return
	 * 		The name of the element.
	 */
	String getName();

	/**
	 * This method sets if this element should be visible or not.
	 * 
	 * @param value
	 * 		True if it is visible, or false otherwise.
	 */
	void setVisible(boolean value);
	
}
