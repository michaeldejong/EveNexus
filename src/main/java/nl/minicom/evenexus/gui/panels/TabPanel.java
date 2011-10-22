package nl.minicom.evenexus.gui.panels;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TabPanel extends JPanel {

	private static final long serialVersionUID = -4187071888426622511L;
	
	private static final Logger LOG = LoggerFactory.getLogger(TabPanel.class);
	
	public TabPanel() {
		setBackground(Color.WHITE);	
	}
	
	protected abstract void reloadContent();
	
	public void reloadTab() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					reloadContent();
				}
			});
		}
		catch (InvocationTargetException e) {
			LOG.error(e.getLocalizedMessage(), e);
			throw new RuntimeException(e);
		}
		catch (InterruptedException e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
	}
	
}
