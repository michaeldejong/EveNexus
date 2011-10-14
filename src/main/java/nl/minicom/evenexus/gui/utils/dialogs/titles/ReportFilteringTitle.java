package nl.minicom.evenexus.gui.utils.dialogs.titles;

import nl.minicom.evenexus.gui.icons.Icon;



/**
 * This is a {@link DialogTitle} object which describes the title of the report filters panel.
 *
 * @author michael
 */
public class ReportFilteringTitle extends DialogTitle {

	/**
	 * This constructs a new {@link ReportFilteringTitle} object.
	 */
	public ReportFilteringTitle() {
		super("How should the data be filtered?", "Report on data collected by " + getApplicationName() + ".", 
				Icon.PIE_CHART_48, Icon.SEARCH_32);
	}
	
}
