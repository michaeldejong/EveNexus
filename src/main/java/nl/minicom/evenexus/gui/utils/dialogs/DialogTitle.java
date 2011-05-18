package nl.minicom.evenexus.gui.utils.dialogs;


import javax.swing.ImageIcon;

import nl.minicom.evenexus.gui.icons.Icon;


public class DialogTitle {

	public static final DialogTitle WARNING_TITLE = new DialogTitle("Warning", DialogTitle.class.getPackage().getSpecificationTitle() + " ran into a little trouble.", "warning_48x48.png");
	public static final DialogTitle ERROR_TITLE = new DialogTitle("Bug report", DialogTitle.class.getPackage().getSpecificationTitle() + " encountered a problem. Please help me improve " + DialogTitle.class.getPackage().getSpecificationTitle() + ", by reporting this bug.", "remove_48x48.png");
	public static final DialogTitle CHARACTER_ADD_TITLE = new DialogTitle("Add a new character", "Please enter your account's FULL API details here.", "user_add_48x48.png");
	public static final DialogTitle ABOUT_TITLE = new DialogTitle("About " + DialogTitle.class.getPackage().getSpecificationTitle(), "So, what do you want to know?", "logo_48x48.png");
	public static final DialogTitle DATABASE_EXPORT_TITLE = new DialogTitle("Export database", "Export transactions, journals, orders and other API details.", "database_next_48x48.png");
	public static final DialogTitle SETTINGS = new DialogTitle("EveNexus settings", "All user settings for EveNexus.", "process_48x48.png");
	public static final DialogTitle DATABASE_CONNECTION_TITLE = new DialogTitle("Cannot connect to database", "EveNexus has problems connecting to the database", "database_error_48x48.png");

	public static final DialogTitle REPORT_ITEM_TITLE = new DialogTitle("Select what data you would like to report on", "Report on data collected by EveNexus.", "pie_chart_48x48.png", "plus_32x32.png");
	public static final DialogTitle REPORT_GROUP_TITLE = new DialogTitle("How should the data be grouped?", "Report on data collected by EveNexus.", "pie_chart_48x48.png", "box_32x32.png");
	public static final DialogTitle REPORT_FILTER_TITLE = new DialogTitle("How should the data be filtered?", "Report on data collected by EveNexus.", "pie_chart_48x48.png", "search_32x32.png");
	public static final DialogTitle REPORT_DISPLAY_TITLE = new DialogTitle("How should the data be displayed?", "Report on data collected by EveNexus.", "pie_chart_48x48.png", "display_32x32.png");
		
	private final String title;
	private final String description;
	private final String iconFileName;
	private final String subIconFileName;
	
	public DialogTitle(String title, String description, String iconFileName) {
		this(title, description, iconFileName, null);
	}
	
	public DialogTitle(String title, String description, String iconFileName, String subIconFileName) {
		this.title = title;
		this.description = description;
		this.iconFileName = iconFileName;
		this.subIconFileName = subIconFileName;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
	
	public ImageIcon getIcon() {
		return Icon.getIcon(iconFileName);
	}
	
	public ImageIcon getSubIcon() {
		if (subIconFileName == null) {
			return null;
		}
		return Icon.getIcon(subIconFileName);
	}
	
}
