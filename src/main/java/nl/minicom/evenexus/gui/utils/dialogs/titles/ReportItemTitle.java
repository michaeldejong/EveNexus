package nl.minicom.evenexus.gui.utils.dialogs.titles;

import nl.minicom.evenexus.gui.icons.Icon;



/**
 * This is a {@link DialogTitle} object which describes the title of the report items panel.
 *
 * @author michael
 */
public class ReportItemTitle extends DialogTitle {

	/**
	 * This constructs a new {@link ReportItemTitle} object.
	 */
	public ReportItemTitle() {
		super("What data you would like to report on?", "Report on data collected by " + getApplicationName() + ".", 
				Icon.PIE_CHART_48, Icon.ADD_32);
	}
	
}
