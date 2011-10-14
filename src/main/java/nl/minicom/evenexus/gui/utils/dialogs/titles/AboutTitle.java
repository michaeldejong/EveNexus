package nl.minicom.evenexus.gui.utils.dialogs.titles;

import nl.minicom.evenexus.gui.icons.Icon;

/**
 * This is a {@link DialogTitle} object which describes the title of the about panel.
 *
 * @author michael
 */
public class AboutTitle extends DialogTitle {

	/**
	 * This constructs a new {@link AboutTitle} object.
	 */
	public AboutTitle() {
		super("About " + getApplicationName(), "So, what do you want to know?", Icon.LOGO_48);
	}
	
}
