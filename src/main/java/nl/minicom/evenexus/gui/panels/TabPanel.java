package nl.minicom.evenexus.gui.panels;

import java.awt.Color;

import javax.swing.JPanel;

public abstract class TabPanel extends JPanel {

	private static final long serialVersionUID = -4187071888426622511L;
		
	
	public TabPanel() {
		setBackground(Color.WHITE);	
	}
	
	public abstract void reloadTab();
	
}
