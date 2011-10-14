package nl.minicom.evenexus.gui.utils.dialogs.titles;


import javax.swing.ImageIcon;

import nl.minicom.evenexus.gui.icons.Icon;


/**
 * This class represents the header of a dialog window.
 *
 * @author michael
 */
public class DialogTitle {

	/**
	 * @return
	 * 		The name of the application.
	 */
	static String getApplicationName() {
		return DialogTitle.class.getPackage().getSpecificationTitle();
	}
	
	private final String title;
	private final String description;
	private final Icon iconFileName;
	private final Icon subIconFileName;
	
	/**
	 * This constructs a new {@link DialogTitle} object.
	 * 
	 * @param title
	 * 		The title to display.
	 * 
	 * @param description
	 * 		The description to display.
	 * 
	 * @param iconFileName
	 * 		The main icon to display.
	 */
	public DialogTitle(String title, String description, Icon iconFileName) {
		this(title, description, iconFileName, null);
	}
	
	/**
	 * This constructs a new {@link DialogTitle} object.
	 * 
	 * @param title
	 * 		The title to display.
	 * 
	 * @param description
	 * 		The description to display.
	 * 
	 * @param iconFileName
	 * 		The main icon to display.
	 * 
	 * @param subIconFileName
	 * 		The sub icon to display.
	 */
	public DialogTitle(String title, String description, Icon iconFileName, Icon subIconFileName) {
		this.title = title;
		this.description = description;
		this.iconFileName = iconFileName;
		this.subIconFileName = subIconFileName;
	}

	/**
	 * @return
	 * 		The title to display.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return
	 * 		The description to display.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return
	 * 		The icon to display.
	 */
	public ImageIcon getIcon() {
		return Icon.getIcon(iconFileName);
	}
	
	/**
	 * @return
	 * 		The sub icon to display.
	 */
	public ImageIcon getSubIcon() {
		if (subIconFileName == null) {
			return null;
		}
		return Icon.getIcon(subIconFileName);
	}
	
}
