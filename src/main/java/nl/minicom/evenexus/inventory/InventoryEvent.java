package nl.minicom.evenexus.inventory;

public class InventoryEvent {
	
	public static final String IDLE_MESSAGE = "Idle";
	public static final String RUNNING_MESSAGE = "Parsing transactions...";
	
	public static final InventoryEvent STARTING = new InventoryEvent(false, RUNNING_MESSAGE, 0.0);
	public static final InventoryEvent IDLE = new InventoryEvent(true, IDLE_MESSAGE, 0.0);
	
	private final String state;
	private final double progress;
	private final boolean finished;
	
	protected InventoryEvent(boolean finished, String state, double progress) {
		this.finished = finished;
		this.state = state;
		this.progress = progress;
	}

	public String getState() {
		return state;
	}

	public double getProgress() {
		return progress;
	}

	public boolean isFinished() {
		return finished;
	}

}
