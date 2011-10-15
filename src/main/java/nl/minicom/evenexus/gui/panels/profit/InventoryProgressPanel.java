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

/**
 * This {@link JPanel} displays a progress bar when there are threads busy in the background 
 * with matching transactions.
 * 
 * @author michael
 */
public class InventoryProgressPanel extends JPanel implements InventoryListener {

	private static final long serialVersionUID = -7958393732480777576L;

	private final JProgressBar progress;
	private final JLabel label;
	private final JLabel image;
	
	/**
	 * This constructs a new {@link InventoryProgressPanel} object.
	 * 
	 * @param inventoryManager
	 * 		The {@link InventoryManager}.
	 */
	@Inject
	public InventoryProgressPanel(InventoryManager inventoryManager) {
		setLayout(null);
		setMinimumSize(new Dimension(0, GuiConstants.FOLDED_STATUS_BAR));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.FOLDED_STATUS_BAR));
		setBackground(GuiConstants.APPLICATION_BACKGROUND_COLOR);
		
		label = new JLabel();
		label.setMinimumSize(new Dimension(0, GuiConstants.PROGRESS_BAR_HEIGHT));
		label.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.PROGRESS_BAR_HEIGHT));
		add(label);
		
		progress = new JProgressBar();
		progress.setMaximumSize(new Dimension(200, GuiConstants.PROGRESS_BAR_HEIGHT));
		progress.setMinimumSize(new Dimension(200, GuiConstants.PROGRESS_BAR_HEIGHT));
		progress.setForeground(new Color(0, 114, 186));
		progress.setBackground(Color.WHITE);
		add(progress);
		
		image = new JLabel();
		image.setMinimumSize(new Dimension(GuiConstants.PROGRESS_BAR_HEIGHT, GuiConstants.PROGRESS_BAR_HEIGHT));
		image.setMaximumSize(new Dimension(GuiConstants.PROGRESS_BAR_HEIGHT, GuiConstants.PROGRESS_BAR_HEIGHT));
		image.setIcon(Icon.getIcon(Icon.CLOCK_16));
		add(image);
		
		GroupLayout layout = new GroupLayout(this);
        setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGap(5)
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
	
	private void setState(boolean isBusy, String message, double percentage) {
		setVisible(isBusy);
		
		if (isBusy) {
			label.setForeground(Color.BLACK);
		}
		else {
			label.setForeground(Color.GRAY);
		}
		
		label.setText("Status: " + message);
		label.setVisible(isBusy);
		
		progress.setMinimum(0);
		progress.setMaximum(1000);
		progress.setValue((int) percentage * 1000);
		
		image.setEnabled(isBusy);
	}
	
}
