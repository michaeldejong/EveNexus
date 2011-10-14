package nl.minicom.evenexus.gui.utils.dialogs.titles;

import nl.minicom.evenexus.gui.icons.Icon;

/**
 * This is a bug report {@link DialogTitle} object.
 *
 * @author michael
 */
public class BugReportTitle extends DialogTitle {

	/**
	 * This constructs a new {@link BugReportTitle} object.
	 */
	public BugReportTitle() {
		super("Bug report", getApplicationName() + " encountered a problem. Please help improve "
				+ getApplicationName() + ", by reporting this bug.", Icon.BUG_REPORT_48);
	}
	
}
