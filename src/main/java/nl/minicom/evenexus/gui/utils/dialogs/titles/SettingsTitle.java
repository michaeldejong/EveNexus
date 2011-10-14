package nl.minicom.evenexus.gui.utils.dialogs.titles;

import nl.minicom.evenexus.gui.icons.Icon;

/**
 * This is a {@link DialogTitle} object which describes the title of the settings dialog.
 *
 * @author michael
 */
public class SettingsTitle extends DialogTitle {

	/**
	 * This constructs a new {@link SettingsTitle} object.
	 */
	public SettingsTitle() {
		super(getApplicationName() + " settings", "All user settings for " + getApplicationName() + ".", 
				Icon.SETTINGS_48);
	}
	
}
