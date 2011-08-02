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
	
	public ProgressManager() {
		this.progressBar = new JProgressBar();		
		this.progressLabel = new JLabel();
		
		this.totalSteps = 0;
		this.currentSteps = 0;
	}
	
	public final JProgressBar getProgressBar() {
		return progressBar;
	}
	
	public final JLabel getProgressLabel() {
		return progressLabel;
	}

	/**
	 * Creates some sort of display in the <code>JDialog</code> which is extended by this class.
	 * Note that the both the progress label and bar must be drawn to use this class.
	 */
	public abstract void buildGui();
	
		
	public void update(int total, int value, String userMessage) {
		totalSteps = total;
		currentSteps = value;
		progressLabel.setText(userMessage);
		progressBar.setMaximum(total);
		progressBar.setValue(value);
	}
	
		
	public void update(double increment, String userMessage) {
		int newTotal = totalSteps * 1000;
		int newValue = (int) ((currentSteps + increment) * 1000);
		
		progressLabel.setText(userMessage);
		progressBar.setMaximum(newTotal);
		progressBar.setValue(newValue);
	}
	
	public int getTotal() {
		return totalSteps;
	}
	
	public int getCurrent() {
		return currentSteps;
	}

}
