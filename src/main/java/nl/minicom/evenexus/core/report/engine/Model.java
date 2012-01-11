package nl.minicom.evenexus.core.report.engine;

import java.util.Set;

import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ModificationListener;

import com.google.common.collect.Sets;


/**
 * <p>This class is a data container which can hold one parameterized value.
 * Other classes are free to register with this class as a listener as long
 * as they implement the {@link ModificationListener} interface.</p>
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
	private boolean enabled;
	private final Set<ModificationListener> modificationListeners;
	
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
		this(value, true);
	}
	
	/**
	 * This will construct a {@link Model} obejct with the supplied value as
	 * the initial value of the {@link Model}.
	 * 
	 * @param value		
	 * 		The initial value of this {@link Model}.
	 * 
	 * @param enabled
	 * 		If this {@link Model} should be enabled.
	 */
	public Model(T value, boolean enabled) {
		this.value = value;
		this.enabled = enabled;
		this.modificationListeners = Sets.newHashSet();
	}
	
	/**
	 * This method sets the enabled flag of the {@link Model}.
	 * 
	 * @param enabled
	 * 		True if the model is enabled.
	 */
	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
		for (ModificationListener listener : modificationListeners) {
			listener.onModification();
		}
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
		for (ModificationListener listener : modificationListeners) {
			listener.onModification();
		}
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
	
	/**
	 * This method adds a new {@link ModificationListener} to this {@link Model}.
	 * 
	 * @param listener
	 * 		The {@link ModificationListener} to add.
	 */
	public void addListener(ModificationListener listener) {
		modificationListeners.add(listener);
	}
	
	/**
	 * This method removes a {@link ModificationListener} to this {@link Model}.
	 * 
	 * @param listener
	 * 		The {@link ModificationListener} to remove.
	 */
	public void removeListener(ModificationListener listener) {
		modificationListeners.remove(listener);
	}

}
