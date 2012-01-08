package nl.minicom.evenexus.core.report.engine;

import nl.minicom.evenexus.gui.icons.Icon;

/**
 * A {@link DisplayType} represents how we can format data in order
 * to present it in a graphic way to the user.
 *
 * @author Michael
 */
public enum DisplayType {

	TABLE(1, Icon.TABLE_48, "Table", 
			"Selecting this option will format your report in the form of a table."), 
			
	BAR_CHART(2, Icon.BAR_CHART_48, "Bar chart", 
			"Selecting this option will format your report in the form of a bar chart."), 
			
	LINE_GRAPH(3, Icon.GRAPH_48, "Line graph", 
			"Selecting this option will format your report in the form of a line graph."),
			
	PIE(4, Icon.PIE_CHART_48, "Pie chart", 
			"Selecting this option will format your report in the form of a pie chart.");
	
	private final int id;
	private final Icon icon;
	private final String title;
	private final String description;
	
	private DisplayType(int id, Icon icon, String title, String description) {
		this.id = id;
		this.icon = icon;
		this.title = title;
		this.description = description;
	}
	
	/**
	 * @return	Returns an unique identifying integer.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return 	Returns an {@link Icon} containing the path to an icon.
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * @return	Returns a title for this {@link DisplayType}.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return	Returns a {@link String} description of this {@link DisplayType}.
	 */
	public String getDescription() {
		return description;
	}
	
}
