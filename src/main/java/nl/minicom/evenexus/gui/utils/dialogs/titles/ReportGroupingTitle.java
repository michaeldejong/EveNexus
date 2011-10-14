package nl.minicom.evenexus.gui.utils.dialogs.titles;

import nl.minicom.evenexus.gui.icons.Icon;



/**
 * This is a {@link DialogTitle} object which describes the title of the report groupings panel.
 *
 * @author michael
 */
public class ReportGroupingTitle extends DialogTitle {

	/**
	 * This constructs a new {@link ReportGroupingTitle} object.
	 */
	public ReportGroupingTitle() {
		super("How should the data be grouped?", "Report on data collected by " + getApplicationName() + ".", 
				Icon.PIE_CHART_48, Icon.PACKAGE_32);
	}
	
}
