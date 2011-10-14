package nl.minicom.evenexus.gui.utils;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * This class is a specialized {@link JPanel} which draws a gradient on the background.
 * 
 * @author michael
 */
public class GradientPanel extends JPanel {

	private static final long serialVersionUID = 5663902178922129568L;

	public static final Color DEFAULT_START_COLOR = new Color(210, 210, 210);
	public static final Color DEFAULT_END_COLOR = Color.WHITE;

	private Color startColor;
	private Color endColor;

	/**
	 * This constructs a new {@link GradientPanel} object.
	 */
	public GradientPanel() {
		this(DEFAULT_START_COLOR, DEFAULT_END_COLOR);
	}
	
	/**
	 * This constructs a new {@link GradientPanel} object.
	 * 
	 * @param startColor
	 * 		The start {@link Color}.
	 * 
	 * @param endColor
	 * 		The end {@link Color}.
	 */
	public GradientPanel(Color startColor , Color endColor) {
		super();
		this.startColor = startColor;
		this.endColor = endColor;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int panelHeight = getHeight();
		int panelWidth = getWidth();
		GradientPaint gradientPaint = new GradientPaint(0, panelHeight, startColor, 10, 10, endColor);
		if (g instanceof Graphics2D) {
			Graphics2D graphics2D = (Graphics2D) g;
			graphics2D.setPaint(gradientPaint);
			graphics2D.fillRect(0, 0, panelWidth, panelHeight);
		}
	}
}

