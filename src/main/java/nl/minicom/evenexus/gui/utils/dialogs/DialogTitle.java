package nl.minicom.evenexus.gui.utils.dialogs;


import javax.swing.ImageIcon;

import nl.minicom.evenexus.gui.icons.Icon;


public class DialogTitle {

	public static final DialogTitle WARNING_TITLE = new DialogTitle("Warning", DialogTitle.class.getPackage().getSpecificationTitle() + " ran into a little trouble.", "img/48/warning.png");
	public static final DialogTitle ERROR_TITLE = new DialogTitle("Bug report", DialogTitle.class.getPackage().getSpecificationTitle() + " encountered a problem. Please help me improve " + DialogTitle.class.getPackage().getSpecificationTitle() + ", by reporting this bug.", "img/48/remove.png");
	public static final DialogTitle CHARACTER_ADD_TITLE = new DialogTitle("Add a new character", "Please enter your account's FULL API details here.", "img/48/user_add.png");
	public static final DialogTitle ABOUT_TITLE = new DialogTitle("About " + DialogTitle.class.getPackage().getSpecificationTitle(), "So, what do you want to know?", "img/48/logo.png");
	public static final DialogTitle DATABASE_EXPORT_TITLE = new DialogTitle("Export database", "Export transactions, journals, orders and other API details.", "img/48/database_next.png");
	public static final DialogTitle SETTINGS = new DialogTitle("EveNexus settings", "All user settings for EveNexus.", "img/48/process.png");
	public static final DialogTitle DATABASE_CONNECTION_TITLE = new DialogTitle("Cannot connect to database", "EveNexus has problems connecting to the database", "img/48/database_error.png");

	public static final DialogTitle REPORT_ITEM_TITLE = new DialogTitle("Select what data you would like to report on", "Report on data collected by EveNexus.", "img/48/pie_chart.png", "img/32/plus.png");
	public static final DialogTitle REPORT_GROUP_TITLE = new DialogTitle("How should the data be grouped?", "Report on data collected by EveNexus.", "img/48/pie_chart.png", "img/32/box.png");
	public static final DialogTitle REPORT_FILTER_TITLE = new DialogTitle("How should the data be filtered?", "Report on data collected by EveNexus.", "img/48/pie_chart.png", "img/32/search.png");
	public static final DialogTitle REPORT_DISPLAY_TITLE = new DialogTitle("How should the data be displayed?", "Report on data collected by EveNexus.", "img/48/pie_chart.png", "img/32/display.png");
		
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
