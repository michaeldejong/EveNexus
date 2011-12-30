package nl.minicom.evenexus.core.report.engine;

/**
 * The {@link ModelListener} class is responsible for triggering change events 
 * between multiple {@link Model} objects.
 *
 * @author Michael
 */
public interface ModelListener {

	/**
	 * Will be triggered when the value is changed.
	 */
	void onValueChanged();
	
	
	/**
	 * Will be triggered when the model is enabled or disabled.
	 */
	void onStateChanged();
	
}
