package nl.minicom.evenexus.gui.panels.profit;

import java.awt.Color;
import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.GroupLayout;
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
		
		JPanel spacer = new JPanel();
		spacer.setMinimumSize(new Dimension(0, 0));
		spacer.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.FOLDED_STATUS_BAR));
		
		label = new JLabel();
		label.setMinimumSize(new Dimension(128, GuiConstants.PROGRESS_BAR_HEIGHT));
		label.setMaximumSize(new Dimension(128, GuiConstants.PROGRESS_BAR_HEIGHT));
		add(label);
		
		progress = new JProgressBar();
		progress.setMaximumSize(new Dimension(148, GuiConstants.PROGRESS_BAR_HEIGHT));
		progress.setMinimumSize(new Dimension(148, GuiConstants.PROGRESS_BAR_HEIGHT));
		progress.setForeground(new Color(0, 114, 186));
		progress.setBackground(Color.WHITE);
		add(progress);
		
		image = new JLabel();
		image.setMinimumSize(new Dimension(GuiConstants.PROGRESS_BAR_HEIGHT, GuiConstants.PROGRESS_BAR_HEIGHT));
		image.setMaximumSize(new Dimension(GuiConstants.PROGRESS_BAR_HEIGHT, GuiConstants.PROGRESS_BAR_HEIGHT));
		image.setIcon(Icon.getIcon("img/16/clock.png"));
		add(image);
		
		GroupLayout layout = new GroupLayout(this);
        setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
	        	.addComponent(spacer)
        		.addComponent(label)
        		.addGap(5)
        		.addComponent(progress)
        		.addGap(5)
        		.addComponent(image)
        		.addGap(5)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addGroup(layout.createParallelGroup()
	    				.addComponent(spacer)
	            		.addComponent(label)
	            		.addComponent(progress)
	            		.addComponent(image)
	    		)
    	);
		
    	setState(false, null, 0.0);
		
		inventoryManager.addListener(this);
	}

	@Override
	public void onUpdate(InventoryEvent event) {
		synchronized (this) {
			setState(!event.isFinished(), event.getState(), event.getProgress());
		}
	}
	
	private void setState(boolean enabled, String message, double percentage) {
		setVisible(enabled);
		
		if (enabled) {
			label.setForeground(Color.BLACK);
		}
		else {
			label.setForeground(Color.GRAY);
		}
		
		label.setText("Status: " + message);
		label.setVisible(enabled);
		
		progress.setMinimum(0);
		progress.setMaximum(1000);
		progress.setValue((int) percentage * 1000);
		
		image.setEnabled(enabled);
	}
	
}
