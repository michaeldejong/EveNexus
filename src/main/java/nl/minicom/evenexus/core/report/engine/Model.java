package nl.minicom.evenexus.core.report.engine;

import java.lang.ref.WeakReference;
import java.util.List;

import com.google.common.collect.Lists;

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
	
	private final List<WeakReference<ModelListener>> listeners;

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
		this.listeners = Lists.newArrayList();
	}
	
	/**
	 * This enables the {@link Model}.
	 */
	public final void enable() {
		this.enabled = true;
		onStateChanged();
	}
	
	/**
	 * This disables the {@link Model}.
	 */
	public final void disable() {
		this.enabled = false;
		onStateChanged();
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
		onValueChanged();
	}
	
	/**
	 * This method will trigger registered listeners. They will be
	 * notified of a change in value of this {@link Model}.
	 */
	private void onValueChanged() {
		synchronized (listeners) {
			int index = 0;
			while (index < listeners.size()) {
				WeakReference<ModelListener> reference = listeners.get(index);
				ModelListener listener = reference.get();
				if (listener == null) {
					listeners.remove(index);
				}
				else {
					listener.onValueChanged();
					index++;
				}
			}
		}
	}
	
	/**
	 * This method will trigger registered listeners. They will be
	 * notified of a change in value of this {@link Model}.
	 */
	private void onStateChanged() {
		synchronized (listeners) {
			int index = 0;
			while (index < listeners.size()) {
				WeakReference<ModelListener> reference = listeners.get(index);
				ModelListener listener = reference.get();
				if (listener == null) {
					listeners.remove(index);
				}
				else {
					listener.onStateChanged();
					index++;
				}
			}
		}
	}
	
	/**
	 * Registers a new {@link ModelListener} with this {@link Model}.
	 * 
	 * @param listener
	 * 		The {@link ModelListener} to notify on change.
	 */
	public void addListener(ModelListener listener) {
		synchronized (listeners) {
			listeners.add(new WeakReference<ModelListener>(listener));
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

}
