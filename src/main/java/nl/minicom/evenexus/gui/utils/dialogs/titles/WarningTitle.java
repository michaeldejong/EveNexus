package nl.minicom.evenexus.gui.utils.dialogs.titles;

import nl.minicom.evenexus.gui.icons.Icon;

/**
 * This is a general warning {@link DialogTitle} object.
 *
 * @author michael
 */
public class WarningTitle extends DialogTitle {

	/**
	 * This constructs a new {@link WarningTitle} object.
	 */
	public WarningTitle() {
		super("Warning", getApplicationName() + " ran into a little trouble.", Icon.WARNING_48);
	}
	
}
