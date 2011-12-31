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

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.UIManager;

import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.gui.icons.Icon;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for drawing the tray in the OS menu.
 * 
 * @author michael
 */
public class Tray {
	
	private static final Logger LOG = LoggerFactory.getLogger(Tray.class);

	private final TrayIcon icon;
	private final SystemTray tray;
	private final BugReportDialog dialog;

	private Gui gui = null;
	
	/**
	 * Constructs a new {@link Tray} object.
	 * 
	 * @param dialog
	 * 		The {@link BugReportDialog}.
	 */
	@Inject
	public Tray(BugReportDialog dialog) {
		this.dialog = dialog;

		Image image = Icon.getImage(Icon.LOGO_48);
		Image scaled = image.getScaledInstance(16, 16, BufferedImage.TYPE_INT_RGB);
		
		Package parent = getClass().getPackage();
		icon = new TrayIcon(scaled, parent.getSpecificationTitle() + " " + parent.getSpecificationVersion());
		tray = SystemTray.getSystemTray();
	}

	/**
	 * This method sets the reference to the {@link Gui}.
	 * 
	 * @param gui
	 * 		The {@link Gui}.
	 */
	public void setGui(Gui gui) {
		this.gui = gui;
	}
	
	/**
	 * This method creates and displays the tray.
	 */
	public void createTray() {
		if (SystemTray.isSupported()) {
			ActionListener restoreListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					tray.remove(icon);
					if (gui != null) {
						gui.setVisible(true);
						gui.setState(JFrame.NORMAL);
						gui.toFront();
					}
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
				LOG.error(e.getLocalizedMessage(), e);
				dialog.setVisible(true);
			}
		}
	}
	
}
