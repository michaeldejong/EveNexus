package nl.minicom.evenexus.core.report.engine;


/**
 * <p>This class is a data container which can hold one parameterized value.
 * Other classes are free to register with this class as a listener as long
 * as they implement the {@link ReportModelListener} interface.</p>
 * 
 * <p>When ever the method setValue() of the {@link Model} is called (irrelevant)
 * of the supplied value, this class will trigger all listeners.</p>
 * 
 * <p>This class is not immutable and not thread-safe!</p>
 *
 * @author Michael
 * @param <T>
 */
public class Model<T> {
	
	private T value;
	private final ReportModel reportModel;
	
	/**
	 * This will construct a {@link Model} object with NULL as an initial value.
	 * @param reportModel	the new {@link ReportModel} to which this {@link Model} belongs.
	 */
	public Model(ReportModel reportModel) {
		this(reportModel, null);
	}
	
	/**
	 * This will construct a {@link Model} obejct with the supplied value as
	 * the initial value of the {@link Model}.
	 * 
	 * @param reportModel
	 * 		The {@link ReportModel}.
	 * 
	 * @param value		
	 * 		The initial value of this {@link Model}.
	 */
	public Model(ReportModel reportModel, T value) {
		this.value = value;
		this.reportModel = reportModel;
	}
	
	/**
	 * This method will change the current value of this {@link Model} to
	 * what every value has been supplied. NULL is allowed.
	 * 
	 * @param value		The new value of this {@link Model}.
	 */
	public final void setValue(T value) {
		this.value = value;
		onValueChanged();
	}
		
	/**
	 * This method will trigger registered listeners. They will be
	 * notified of a change in value of this {@link Model}.
	 */
	private void onValueChanged() {
		reportModel.onValueChanged();
	}
	
	/**
	 * @return the current value of this {@link Model}.
	 */
	public final T getValue() {
		return value;
	}

	/**
	 * @return true iff the value of this {@link Model} is not NULL.
	 */
	public final boolean isSet() {
		return value != null;
	}

}
