package nl.minicom.evenexus.eveapi;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	private static final String URL = "http://image.eveonline.com";

	public static BufferedImage loadCharacterImage(long characterId, int size) throws MalformedURLException, IOException {
		return loadImage("character", characterId, size, "jpg");
	}
	
	public static BufferedImage loadCorporationImage(long corporationId, int size) throws MalformedURLException, IOException {
		return loadImage("corporation", corporationId, size, "png");
	}
	
	public static BufferedImage loadAllianceImage(long allianceId, int size) throws MalformedURLException, IOException {
		return loadImage("alliance", allianceId, size, "png");
	}
	
	private static BufferedImage loadImage(String path, long id, int size, String extension) throws MalformedURLException, IOException {
		String fullPath = URL + "/" + path + "/" + 	id + "_256." + extension;
		
		BufferedImage image = ImageIO.read(new URL(fullPath));
		BufferedImage scaledImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = scaledImage.createGraphics();
		AffineTransform xform = AffineTransform.getScaleInstance(scaledImage.getWidth() / 256.0, scaledImage.getHeight() / 256.0);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(image, xform, null);
		graphics2D.dispose();
		
		return scaledImage;
	}
	
}
