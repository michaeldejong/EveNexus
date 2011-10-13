package nl.minicom.evenexus.eveapi;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * This class is responsible for retrieving and resizing images from the EVE Online server.
 *
 * @author michael
 */
public final class ImageLoader {
	
	private static final String URL = "http://image.eveonline.com";

	/**
	 * Retrieves the specified character image from the EVE Online server.
	 * 
	 * @param characterId
	 * 		The id of the character.
	 * 
	 * @param size
	 * 		The requested size.
	 * 
	 * @return
	 * 		The requested image.
	 * 
	 * @throws IOException
	 * 		If the image could not be retrieved.
	 */
	public static BufferedImage loadCharacterImage(long characterId, int size) throws IOException {
		return loadImage("character", characterId, size, "jpg");
	}
	
	/**
	 * Retrieves the specified corporation image from the EVE Online server.
	 * 
	 * @param corporationId
	 * 		The id of the corporation.
	 * 
	 * @param size
	 * 		The requested size.
	 * 
	 * @return
	 * 		The retrieved image.
	 * 
	 * @throws IOException
	 * 		If the image could not be retrieved.
	 */
	public static BufferedImage loadCorporationImage(long corporationId, int size) throws IOException {
		return loadImage("corporation", corporationId, size, "png");
	}
	
	/**
	 * Retrieves the specified alliance image from the EVE Online server.
	 * 
	 * @param allianceId
	 * 		The id of the alliance.
	 * 
	 * @param size
	 * 		The requested size.
	 * 
	 * @return
	 * 		The retrieved image.
	 * 
	 * @throws IOException
	 * 		If the image could not be retrieved.
	 */
	public static BufferedImage loadAllianceImage(long allianceId, int size) throws IOException {
		return loadImage("alliance", allianceId, size, "png");
	}
	
	private static BufferedImage loadImage(String path, long id, int size, String extension) throws IOException {
		String fullPath = URL + "/" + path + "/" + 	id + "_256." + extension;
		
		BufferedImage image = ImageIO.read(new URL(fullPath));
		BufferedImage scaledImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = scaledImage.createGraphics();

		double widthFactor = scaledImage.getWidth() / 256.0;
		double heightFactor = scaledImage.getHeight() / 256.0;
		AffineTransform xform = AffineTransform.getScaleInstance(widthFactor, heightFactor);
		
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(image, xform, null);
		graphics2D.dispose();
		
		return scaledImage;
	}
	
	private ImageLoader() {
		// Prevent instantiation.
	}
}
