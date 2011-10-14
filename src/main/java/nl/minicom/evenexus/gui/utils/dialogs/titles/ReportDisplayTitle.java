package nl.minicom.evenexus.gui.utils.dialogs.titles;

import nl.minicom.evenexus.gui.icons.Icon;



/**
 * This is a {@link DialogTitle} object which describes the title of the report display panel.
 *
 * @author michael
 */
public class ReportDisplayTitle extends DialogTitle {

	/**
	 * This constructs a new {@link ReportDisplayTitle} object.
	 */
	public ReportDisplayTitle() {
		super("How should the data be displayed?", "Report on data collected by " + getApplicationName() + ".", 
				Icon.PIE_CHART_48, Icon.DISPLAY_32);
	}
	
}
