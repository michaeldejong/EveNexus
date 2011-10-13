package nl.minicom.evenexus.gui.utils.dialogs;


import javax.swing.ImageIcon;

import nl.minicom.evenexus.gui.icons.Icon;


public class DialogTitle {

	private static final String APPLICATION_NAME = DialogTitle.class.getPackage().getSpecificationTitle();
	public static final DialogTitle WARNING_TITLE = new DialogTitle("Warning", APPLICATION_NAME + " ran into a little trouble.", "img/48/warning.png");
	public static final DialogTitle BUG_REPORT = new DialogTitle("Bug report", APPLICATION_NAME + " encountered a problem. Please help me improve " + APPLICATION_NAME + ", by reporting this bug.", "img/48/bug_report.png");
	public static final DialogTitle CHARACTER_ADD_TITLE = new DialogTitle("Add a new character", "Please enter your account's FULL API details here.", "img/48/user_add.png");
	public static final DialogTitle ABOUT_TITLE = new DialogTitle("About " + APPLICATION_NAME, "So, what do you want to know?", "img/48/logo.png");
	public static final DialogTitle DATABASE_EXPORT_TITLE = new DialogTitle("Export database", "Export transactions, journals, orders and other API details.", "img/48/database_next.png");
	public static final DialogTitle SETTINGS = new DialogTitle("EveNexus settings", "All user settings for EveNexus.", "img/48/process.png");
	public static final DialogTitle DATABASE_CONNECTION_TITLE = new DialogTitle("Cannot connect to database", APPLICATION_NAME + " has problems connecting to the database", "img/48/database_error.png");

	private static final String REPORT_SUB_TITLE = "Report on data collected by EveNexus.";
	public static final DialogTitle REPORT_ITEM_TITLE = new DialogTitle("What data you would like to report on?", REPORT_SUB_TITLE, "img/48/pie_chart.png", "img/32/plus.png");
	public static final DialogTitle REPORT_GROUP_TITLE = new DialogTitle("How should the data be grouped?", REPORT_SUB_TITLE, "img/48/pie_chart.png", "img/32/box.png");
	public static final DialogTitle REPORT_FILTER_TITLE = new DialogTitle("How should the data be filtered?", REPORT_SUB_TITLE, "img/48/pie_chart.png", "img/32/search.png");
	public static final DialogTitle REPORT_DISPLAY_TITLE = new DialogTitle("How should the data be displayed?", REPORT_SUB_TITLE, "img/48/pie_chart.png", "img/32/display.png");
		
	private final String title;
	private final String description;
	private final String iconFileName;
	private final String subIconFileName;
	
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
	public DialogTitle(String title, String description, String iconFileName) {
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
	public DialogTitle(String title, String description, String iconFileName, String subIconFileName) {
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
