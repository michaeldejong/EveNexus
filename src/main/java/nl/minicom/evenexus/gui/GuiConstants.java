package nl.minicom.evenexus.gui;

import java.awt.Color;

public class GuiConstants {

	public static final int BUTTON_HEIGHT = 32;
	
	public static final int TEXT_FIELD_HEIGHT = 23;
	
	public static final int COMBO_BOX_HEIGHT = 23;

	public static final int SPINNER_HEIGHT = 22;

	public static final int PROGRESS_BAR_HEIGHT = 20;
	
	public static final int FOLDED_STATUS_BAR = 28;
	
	public static final Color APPLICATION_BACKGROUND_COLOR = new Color(237, 237, 237);
	
	public static Color getTabBackground() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			return new Color(228, 228, 228);
		}
		return Color.WHITE;
	}
	
}
