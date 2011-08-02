package nl.minicom.evenexus.utils;

public interface ProgressListener {

	void update(int total, int value, String userMessage);
	
	void update(double increment, String userMessage);
	
	int getCurrent();
	
	int getTotal();
	
}
