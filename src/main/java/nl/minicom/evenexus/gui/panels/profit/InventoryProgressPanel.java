package nl.minicom.evenexus.gui.panels.profit;

import java.awt.Color;
import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.icons.Icon;
import nl.minicom.evenexus.inventory.InventoryEvent;
import nl.minicom.evenexus.inventory.InventoryListener;
import nl.minicom.evenexus.inventory.InventoryManager;

public class InventoryProgressPanel extends JPanel implements InventoryListener {

	private static final long serialVersionUID = -7958393732480777576L;

	private final JProgressBar progress;
	private final JLabel label;
	private final JLabel image;
	
	@Inject
	public InventoryProgressPanel(InventoryManager inventoryManager) {
		setLayout(null);
		setMinimumSize(new Dimension(0, GuiConstants.FOLDED_STATUS_BAR));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.FOLDED_STATUS_BAR));
		setBackground(GuiConstants.APPLICATION_BACKGROUND_COLOR);
		
		label = new JLabel();
		label.setBounds(4, 4, 128, GuiConstants.PROGRESS_BAR_HEIGHT);
		add(label);
		
		progress = new JProgressBar();
		progress.setBounds(132, 4, 125, GuiConstants.PROGRESS_BAR_HEIGHT);
		progress.setForeground(new Color(0, 114, 186));
		progress.setBackground(Color.WHITE);
		add(progress);
		
		image = new JLabel();
		image.setBounds(260, 6, 16, 16);
		image.setIcon(Icon.getIcon("img/16/clock.png"));
		add(image);
		
		onUpdate(InventoryEvent.IDLE);
		
		inventoryManager.addListener(this);
	}

	@Override
	public synchronized void onUpdate(InventoryEvent event) {
		setState(!event.isFinished(), event.getState(), event.getProgress());
	}
	
	private void setState(boolean enabled, String message, double percentage) {
		label.setText("Status: " + message);
		label.setForeground(enabled ? Color.BLACK : Color.GRAY);
		label.setVisible(enabled);
		
		progress.setMinimum(0);
		progress.setMaximum(1000);
		progress.setValue((int) percentage * 1000);
		
		image.setEnabled(enabled);
	}
	
}
