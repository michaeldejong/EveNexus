package nl.minicom.evenexus.utils;

public interface ProgressListener {

	public void update(int total, int value, String userMessage);
	
	public void update(double increment, String userMessage);
	
	public int getCurrent();
	
	public int getTotal();
	
}
