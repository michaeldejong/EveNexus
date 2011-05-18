package nl.minicom.evenexus.gui.utils;


import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.UIManager;

import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.gui.icons.Icon;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class Tray {
	
	private static final Logger logger = LogManager.getRootLogger();

	private final Gui gui;
	private final TrayIcon icon;
	private final SystemTray tray;
	
	public Tray(Gui gui) {
		this.gui = gui;

		Image image = Icon.getImage("logo_48x48.png");
		Image scaled = image.getScaledInstance(16, 16, BufferedImage.TYPE_INT_RGB);
		
		Package parent = getClass().getPackage();
		icon = new TrayIcon(scaled, parent.getSpecificationTitle() + " " + parent.getSpecificationVersion());
		tray = SystemTray.getSystemTray();
	}
	
	public void createTray() {
		if (SystemTray.isSupported()) {
			ActionListener restoreListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					tray.remove(icon);
					gui.setVisible(true);
					gui.setState(JFrame.NORMAL);
					gui.toFront();
				}
			};

			ActionListener closeListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					tray.remove(icon);
					System.exit(0);
				}
			};
			
			icon.addActionListener(restoreListener);
						
			PopupMenu menu = new PopupMenu(getClass().getPackage().getSpecificationTitle());
			menu.setFont(UIManager.getFont("Label.font"));
			
			MenuItem open = new MenuItem("Open " + getClass().getPackage().getSpecificationTitle());
			open.addActionListener(restoreListener);
			menu.add(open);

			menu.addSeparator();
			
			MenuItem exit = new MenuItem("Exit");
			exit.addActionListener(closeListener);
			menu.add(exit);
						
			icon.setPopupMenu(menu);
			
			try {
				tray.add(icon);
			}
			catch (AWTException e) {
				logger.error(e);
			}
		}
	}
	
}
