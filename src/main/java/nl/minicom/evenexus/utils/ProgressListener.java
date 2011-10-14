package nl.minicom.evenexus.utils;

/**
 * The {@link ProgressListener} interface can be implemented in order to be able to 
 * receive updates on background tasks.
 * 
 * @author michael
 */
public interface ProgressListener {

	/**
	 * This method is called when an update is received.
	 * 
	 * @param total
	 * 		The total tasks.
	 * 
	 * @param value
	 * 		The current task.
	 * 
	 * @param userMessage
	 * 		The associated message.
	 */
	void update(int total, int value, String userMessage);
	
	/**
	 * This method is called when an update is received.
	 * 
	 * @param increment
	 * 		The increment in progress.
	 * 
	 * @param userMessage
	 * 		The associated message.
	 */
	void update(double increment, String userMessage);
	
	/**
	 * @return
	 * 		The current progress.
	 */
	int getCurrent();
	
	/**
	 * @return
	 * 		The total tasks.
	 */
	int getTotal();
	
}
