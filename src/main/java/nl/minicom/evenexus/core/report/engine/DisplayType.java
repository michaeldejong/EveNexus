package nl.minicom.evenexus.core.report.engine;

import java.util.Collection;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup.Type;
import nl.minicom.evenexus.gui.icons.Icon;

/**
 * A {@link DisplayType} represents how we can format data in order
 * to present it in a graphic way to the user.
 *
 * @author Michael
 */
public enum DisplayType {

//	TABLE(1, Icon.TABLE_48, "Table", 
//			"Selecting this option will format your report in the form of a table.",
//			Type.DAY, Type.WEEK, Type.MONTH, Type.NAME), 
			
	BAR_CHART(2, Icon.BAR_CHART_48, "Bar chart", 
			"Selecting this option will format your report in the form of a bar chart.",
			Type.DAY, Type.WEEK, Type.MONTH, Type.NAME), 
			
	LINE_GRAPH(3, Icon.GRAPH_48, "Line graph", 
			"Selecting this option will format your report in the form of a line graph.",
			Type.DAY, Type.WEEK, Type.MONTH),
			
//	PIE(4, Icon.PIE_CHART_48, "Pie chart", 
//			"Selecting this option will format your report in the form of a pie chart.",
//			Type.NAME),
	
	;
	
	private final int id;
	private final Icon icon;
	private final String title;
	private final String description;
	private final Type[] supportedTypes;
	
	private DisplayType(int id, Icon icon, String title, String description, Type... types) {
		this.id = id;
		this.icon = icon;
		this.title = title;
		this.description = description;
		this.supportedTypes = types;
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
	
	public boolean supports(Type type) {
		for (Type supportedType : supportedTypes) {
			if (supportedType.equals(type)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean supportsAll(Collection<Type> types) {
		for (Type type : types) {
			if (!supports(type)) {
				return false; 
			}
		}
		return true;
	}
	
}
