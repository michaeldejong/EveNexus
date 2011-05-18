package nl.minicom.evenexus.gui.validation;

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
	
}
