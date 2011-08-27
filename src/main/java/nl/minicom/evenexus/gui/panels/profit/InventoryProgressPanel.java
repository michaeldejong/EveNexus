package nl.minicom.evenexus.gui.panels.profit;

import java.awt.Color;
import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.inventory.InventoryEvent;
import nl.minicom.evenexus.inventory.InventoryListener;
import nl.minicom.evenexus.inventory.InventoryManager;

public class InventoryProgressPanel extends JPanel implements InventoryListener {

	private static final long serialVersionUID = -7958393732480777576L;

	private final JProgressBar progress;
	private final JLabel label;
	
	@Inject
	public InventoryProgressPanel(InventoryManager inventoryManager) {
		setLayout(null);
		setMaximumSize(new Dimension(128, 48));
		setMinimumSize(new Dimension(128, 48));
		setBackground(Color.WHITE);
		
		label = new JLabel();
		label.setBounds(0, 2, 128, GuiConstants.COMBO_BOX_HEIGHT);
		add(label);
		
		progress = new JProgressBar();
		progress.setBounds(0, 20, 125, 12);
		progress.setForeground(new Color(0, 114, 186));
		progress.setBackground(Color.WHITE);
		add(progress);
		
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
		
		progress.setMinimum(0);
		progress.setMaximum(1000);
		progress.setValue((int) percentage * 1000);
		progress.setEnabled(enabled);
	}
	
}
