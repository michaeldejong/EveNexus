package nl.minicom.evenexus.core.report.engine;

/**
 * The {@link ReportModelListener} class is responsible for triggering change events 
 * between multiple {@link Model} objects.
 *
 * @author Michael
 */
public interface ReportModelListener {

	/**
	 * Will be triggered when the value is changed.
	 */
	void onValueChanged();
	
}
