package nl.minicom.evenexus.gui.utils.progresswindows;


import java.awt.GraphicsEnvironment;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.gui.icons.Icon;


public class DefaultProgressFrame extends ProgressManager {

	private static final long serialVersionUID = -5496680715749455383L;
	
	public DefaultProgressFrame() {
		super();
		buildGui();
	}
	
	@Override
	public final void buildGui() {
		setTitle(getClass().getPackage().getSpecificationTitle());
		setIconImage(Icon.getImage(Icon.LOGO));
		setAlwaysOnTop(true);
		
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		int xPosition = (int) (env.getMaximumWindowBounds().getWidth() - 450) / 2;
		int yPosition = (int) (env.getMaximumWindowBounds().getHeight() - 82) / 2;
		setBounds(xPosition, yPosition, 450, 82);
		
		Gui.setLookAndFeel();
	
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 450, 82);
		panel.setLayout(null);
		getContentPane().add(panel);
		
		JLabel progressLabel = getProgressLabel();
		progressLabel.setFont(progressLabel.getFont().deriveFont(8));
		progressLabel.setBounds(20, 5, 410, 20);
		progressLabel.setHorizontalAlignment(JLabel.RIGHT);
		progressLabel.setVerticalAlignment(JLabel.BOTTOM);
		progressLabel.setVisible(false);
		panel.add(progressLabel);
	
		JProgressBar progressBar = getProgressBar();
		progressBar.setMinimum(0);
		progressBar.setBounds(20, 28, 410, 16);
		progressBar.setVisible(false);
		panel.add(progressBar);
	}
	
}
