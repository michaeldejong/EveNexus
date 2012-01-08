package nl.minicom.evenexus.core.report.engine;


/**
 * <p>This class is a data container which can hold one parameterized value.
 * Other classes are free to register with this class as a listener as long
 * as they implement the {@link ModelListener} interface.</p>
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
	private boolean enabled = false;
	
	/**
	 * This will construct a {@link Model} object with NULL as an initial value.
	 */
	public Model() {
		this(null);
	}
	
	/**
	 * This will construct a {@link Model} obejct with the supplied value as
	 * the initial value of the {@link Model}.
	 * 
	 * @param value		
	 * 		The initial value of this {@link Model}.
	 */
	public Model(T value) {
		this.value = value;
	}
	
	/**
	 * This enables the {@link Model}.
	 */
	public final void enable() {
		this.enabled = true;
	}
	
	/**
	 * This disables the {@link Model}.
	 */
	public final void disable() {
		this.enabled = false;
	}
	
	/**
	 * @return
	 * 		True if the {@link Model} is enabled.
	 */
	public final boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * This method will change the current value of this {@link Model} to
	 * what every value has been supplied. NULL is allowed.
	 * 
	 * @param value		The new value of this {@link Model}.
	 */
	public final void setValue(T value) {
		this.value = value;
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
