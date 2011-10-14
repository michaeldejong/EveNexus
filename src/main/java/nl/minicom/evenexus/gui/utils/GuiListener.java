package nl.minicom.evenexus.gui.utils;


import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.inject.Inject;
import javax.swing.JFrame;

import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.utils.SettingsManager;


/**
 * This class listens to window events of the main application, and saves the values of 
 * these events to the database, in order to be able to load and display them on start-up.
 * 
 * @author michael
 */
public class GuiListener implements ComponentListener, WindowStateListener, WindowListener {
	
	private final Tray tray;
	private final SettingsManager settingsManager;
	
	/**
	 * This constructs a new {@link GuiListener} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 * 
	 * @param tray
	 * 		The {@link Tray} object.
	 */
	@Inject
	public GuiListener(SettingsManager settingsManager, Tray tray) {
		this.tray = tray;
		this.settingsManager = settingsManager;
	}

	/**
	 * This method sets a reference to the {@link Gui} object.
	 * 
	 * @param gui
	 * 		The {@link Gui}.
	 */
	public void setGui(Gui gui) {
		tray.setGui(gui);
	}
	
	// WindowListener
	
	@Override
	public void windowOpened(WindowEvent arg0) {
		// Do nothing.
	}
	
	@Override
	public void windowIconified(WindowEvent arg0) {
		// Do nothing.
	}
	
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// Do nothing.
	}
	
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// Do nothing.
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		// Do nothing.
	}
	
	@Override
	public void windowClosed(WindowEvent arg0) {
		System.exit(0);
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// Do nothing.
	}
	
	// ComponentListener

	@Override
	public void componentShown(ComponentEvent arg0) {
		// Do nothing.
	}
	
	@Override
	public void componentResized(ComponentEvent arg0) {
		JFrame parent = (JFrame) arg0.getComponent();
		if (parent.getExtendedState() == JFrame.NORMAL) {
			settingsManager.saveObject(SettingsManager.APPLICATION_WIDTH, parent.getWidth());
			settingsManager.saveObject(SettingsManager.APPLICATION_HEIGHT, parent.getHeight());
		}
	}
	
	@Override
	public void componentMoved(ComponentEvent arg0) {
		JFrame parent = (JFrame) arg0.getComponent();
		if (parent.getExtendedState() == JFrame.NORMAL && parent.getX() > 0) {
			settingsManager.saveObject(SettingsManager.APPLICATION_X, parent.getX());
			settingsManager.saveObject(SettingsManager.APPLICATION_Y, parent.getY());
		}
	}
	
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// Do nothing.
	}
	
	// WindowStateListener
	
	@Override
	public void windowStateChanged(WindowEvent arg0) {
		JFrame parent = (JFrame) arg0.getComponent();
		boolean isMaximized = parent.getExtendedState() == JFrame.MAXIMIZED_BOTH;
		settingsManager.saveObject(SettingsManager.APPLICATION_MAXIMIZED, isMaximized);
		
		if (arg0.getNewState() == JFrame.ICONIFIED) {
			parent.setVisible(false);
			tray.createTray();
		}
	}

}
