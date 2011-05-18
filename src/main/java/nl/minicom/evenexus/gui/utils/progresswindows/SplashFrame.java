package nl.minicom.evenexus.gui.utils.progresswindows;


import java.awt.Color;
import java.awt.GraphicsEnvironment;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.gui.icons.Icon;


public class SplashFrame extends ProgressManager {

	private static final long serialVersionUID = -5496680715749455383L;
	
	public SplashFrame() {
		super();
		buildGui();
	}
	
	@Override
	public final void buildGui() {
		setTitle(getClass().getPackage().getSpecificationTitle());
//		setAlwaysOnTop(true);
		setIconImage(Icon.getImage("logo_128x128.png"));
		setUndecorated(true);
		setResizable(false);
		
		Gui.setLookAndFeel();
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		int xPosition = (int) (env.getMaximumWindowBounds().getWidth() - 487) / 2;
		int yPosition = (int) (env.getMaximumWindowBounds().getHeight() - 223) / 2;
		setBounds(xPosition, yPosition, 487, 223);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 487, 223);
		panel.setLayout(null);
		getContentPane().add(panel);
		
		JLabel logo = new JLabel(Icon.getIcon("splash.png"));
		logo.setBounds(0, 0, 487, 223);
		panel.add(logo);
		
		JLabel progressLabel = getProgressLabel();
		progressLabel.setFont(progressLabel.getFont().deriveFont(8));
		progressLabel.setForeground(Color.WHITE);
		progressLabel.setBounds(54, 140, 383, 20);
		progressLabel.setHorizontalAlignment(JLabel.RIGHT);
		progressLabel.setVerticalAlignment(JLabel.BOTTOM);
		logo.add(progressLabel);

		JProgressBar progressBar = getProgressBar();
		progressBar.setMinimum(0);
		progressBar.setBounds(54, 163, 383, 16);
		logo.add(progressBar);
	}
	
}
