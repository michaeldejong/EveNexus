package nl.minicom.evenexus.core.report.engine;

/**
 * A {@link DisplayType} represents how we can format data in order
 * to present it in a graphic way to the user.
 *
 * @author Michael
 */
public enum DisplayType {

	TABLE(1, "table_48x48.png", "Table", "Selecting this option will format your report in the form of a table."), 
	GRAPH(2, "chart_48x48.png", "Graph", "Selecting this option will format your report in the form of a graph."), 
	PIE(3, "pie_chart_48x48.png", "Pie chart", "Selecting this option will format your report in the form of a pie chart.");
	
	private final int id;
	private final String icon;
	private final String title;
	private final String description;
	
	private DisplayType(int id, String icon, String title, String description) {
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
	 * @return 	Returns a {@link String} containing the path to an icon.
	 */
	public String getIcon() {
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
