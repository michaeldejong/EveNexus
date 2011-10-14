package nl.minicom.evenexus.gui.utils.dialogs.titles;

import nl.minicom.evenexus.gui.icons.Icon;


/**
 * This is a {@link DialogTitle} object which describes the action of exporting the database.
 *
 * @author michael
 */
public class ExportDatabaseTitle extends DialogTitle {

	/**
	 * This constructs a new {@link ExportDatabaseTitle} object.
	 */
	public ExportDatabaseTitle() {
		super("Export database", "Export transactions, journals, orders and other API details.", 
				Icon.EXPORT_DATABASE_48);
	}
	
}
