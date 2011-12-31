package nl.minicom.evenexus.gui.utils.progresswindows;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import nl.minicom.evenexus.utils.ProgressListener;

/**
 * This class can be used to compile a list of <code>Action</code> objects which will be run
 * in sequence. The progress of these <code>Action</code>s, will be displayed in a 
 * <code>JLabel</code> and a <code>JProgressBar</code>. Note that <code>Action</code> objects
 * are allowed to 'spawn' sub<code>Action</code> objects which will be executed after their 
 * parent <code>Action</code>.
 * 
 * @author Michael
 */
public abstract class ProgressManager extends JDialog implements ProgressListener  {
	
	private static final long serialVersionUID = 4236172383272120320L;

	private final JLabel progressLabel;
	private final JProgressBar progressBar;
	
	private int totalSteps;
	private int currentSteps;
	
	/**
	 * Constructs a new {@link ProgressManager}.
	 */
	public ProgressManager() {
		this.progressBar = new JProgressBar();		
		this.progressLabel = new JLabel();
		
		this.totalSteps = 0;
		this.currentSteps = 0;
	}
	
	/**
	 * @return
	 * 		The {@link JProgressBar} present in this dialog.
	 */
	public final JProgressBar getProgressBar() {
		return progressBar;
	}
	
	/**
	 * @return
	 * 		The {@link JLabel} present in this dialog.
	 */
	public final JLabel getProgressLabel() {
		return progressLabel;
	}

	/**
	 * Creates some sort of display in the <code>JDialog</code> which is extended by this class.
	 * Note that the both the progress label and bar must be drawn to use this class.
	 */
	public abstract void buildGui();
	
	/**
	 * This method updates the dialog to a new state.
	 * 
	 * @param total
	 * 		The total amount of steps.
	 * 
	 * @param value
	 * 		The current step.
	 * 
	 * @param userMessage
	 * 		The message to display.
	 */
	public void update(int total, int value, String userMessage) {
		totalSteps = total;
		currentSteps = value;
		progressLabel.setText(userMessage);
		progressBar.setMaximum(total);
		progressBar.setValue(value);
	}
	
	/**
	 * This method updates the state of this dialog.
	 * 
	 * @param increment
	 * 		The amount of steps to increment.
	 * 
	 * @param userMessage
	 * 		The message to display.
	 */
	public void update(double increment, String userMessage) {
		int newTotal = totalSteps * 1000;
		int newValue = (int) ((currentSteps + increment) * 1000);
		
		progressLabel.setText(userMessage);
		progressBar.setMaximum(newTotal);
		progressBar.setValue(newValue);
	}
	
	/**
	 * @return
	 * 		The total amount of steps.
	 */
	public int getTotal() {
		return totalSteps;
	}
	
	/**
	 * @return
	 * 		The current step.
	 */
	public int getCurrent() {
		return currentSteps;
	}

}
