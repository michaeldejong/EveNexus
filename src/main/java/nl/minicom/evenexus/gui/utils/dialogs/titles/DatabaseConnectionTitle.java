package nl.minicom.evenexus.gui.utils.dialogs.titles;

import nl.minicom.evenexus.gui.icons.Icon;


/**
 * This is a {@link DialogTitle} object which describes an error while connecting to the database.
 *
 * @author michael
 */
public class DatabaseConnectionTitle extends DialogTitle {

	/**
	 * This constructs a new {@link DatabaseConnectionTitle} object.
	 */
	public DatabaseConnectionTitle() {
		super("Cannot connect to database", getApplicationName() + " has problems connecting to the database", 
				Icon.DATABASE_ERROR_48);
	}
	
}
