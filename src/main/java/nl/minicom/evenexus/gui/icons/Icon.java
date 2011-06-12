package nl.minicom.evenexus.gui.icons;

import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Icon {

	private static final Logger LOG = LoggerFactory.getLogger(Icon.class);
	
	public static ImageIcon getIcon(String iconFileName) {
		try {
			return new ImageIcon(ImageIO.read(Icon.class.getResourceAsStream("/" + iconFileName)));
		}
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
		return null;
	}

	public static Image getImage(String iconFileName) {
		return getIcon(iconFileName).getImage();
	}
	
}
