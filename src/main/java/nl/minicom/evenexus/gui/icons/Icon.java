package nl.minicom.evenexus.gui.icons;

import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Icon {

	private static final Logger logger = LogManager.getRootLogger();
	
	public static ImageIcon getIcon(String iconFileName) {
		try {
			return new ImageIcon(ImageIO.read(Icon.class.getResourceAsStream("/" + iconFileName)));
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return null;
	}

	public static Image getImage(String iconFileName) {
		return getIcon(iconFileName).getImage();
	}
	
}
