package nl.minicom.evenexus.inventory;

/**
 * The {@link InventoryEvent} is a data container object and is passed to an {@link InventoryListener}
 * on certain events, while matching transactions.
 * 
 * @author michael
 */
public class InventoryEvent {
	
	public static final String IDLE_MESSAGE = "Idle";
	public static final String RUNNING_MESSAGE = "Parsing transactions...";
	
	public static final InventoryEvent STARTING = new InventoryEvent(false, RUNNING_MESSAGE, 0.0);
	public static final InventoryEvent IDLE = new InventoryEvent(true, IDLE_MESSAGE, 0.0);
	
	private final String state;
	private final double progress;
	private final boolean finished;
	
	/**
	 * This constructs a new {@link InventoryEvent} object.
	 * 
	 * @param finished
	 * 		True if the matching has completed.
	 * 
	 * @param state
	 * 		A {@link String} message describing the state.
	 * 
	 * @param progress
	 * 		The total progress (0.0 to 1.0).
	 */
	InventoryEvent(boolean finished, String state, double progress) {
		this.finished = finished;
		this.state = state;
		this.progress = progress;
	}

	/**
	 * @return
	 * 		The state for this {@link InventoryEvent}.
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return
	 * 		The progress for this {@link InventoryEvent}.
	 */
	public double getProgress() {
		return progress;
	}

	/**
	 * @return
	 * 		True if the matching has been completed, or false if it has not.
	 */
	public boolean isFinished() {
		return finished;
	}

}
