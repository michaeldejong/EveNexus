package nl.minicom.evenexus.core.report.engine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * The {@link Dataset} class is a simple data holder object, which 
 * basically creates a sparse table. It uses {@link TreeMap} objects
 * and {@link LinkedHashMap} objects for quick and easy insertion 
 * and retrieval of values.
 *
 * @author Michael
 */
public class Dataset {
	
	private final Map<String, Map<Integer, Number>> values;
	
	/**
	 * This constructor creates an empty {@link Dataset} object.
	 */
	public Dataset() {
		this.values = new LinkedHashMap<String, Map<Integer, Number>>();
	}
	
	/**
	 * @return	The vertical size: The amount of keys in the {@link Dataset}.
	 */
	public int size() {
		return values.size();
	}

	/**
	 * @param key	The key to get the size for.
	 * @return		The horizontal size for a specified key.
	 */
	public int size(String key) {
		Map<Integer, Number> map = values.get(key);
		if (map == null || map.isEmpty()) {
			return 0;
		}
		
		return map.size();
	}
	
	/**
	 * @return	A {@link Set} of {@link String} keys.
	 */
	public Set<String> keySet() {
		return values.keySet();
	}
	
	/**
	 * This method inserts a value at a specified location in the {@link Dataset}.
	 * 
	 * @param key		The key for which to add the value.
	 * @param dataset	The dataset index to add the value for.
	 * @param value		The value to insert.
	 */
	public void setValue(String key, int dataset, Number value) {
		ensureMapExists(key).put(dataset, value);
	}
	
	/**
	 * This method returns the value of a specified location in the {@link Dataset}.
	 * 
	 * @param key		The key for which to retrieve the value.
	 * @param dataset	The dataset index to retrieve the value for.
	 * @return			The value at the specified location.
	 */
	public Number getValue(String key, int dataset) {
		Map<Integer, Number> map = values.get(key);
		if (map == null || map.isEmpty()) {
			return 0;
		}
		
		Number value = map.get(dataset);
		if (value == null) {
			return 0;
		}
		
		return value;
	}

	/**
	 * Ensures that there is a {@link Map} object to insert into.
	 * This method will create a new {@link Map} if it does not yet 
	 * exist. Ergo, this should only be used for preparing insertions.
	 * 
	 * @param key	The key to look for.
	 * @return		The {@link Map} which either existed at the key location or is newly created.
	 */
	private Map<Integer, Number> ensureMapExists(String key) {
		Map<Integer, Number> map = values.get(key);
		if (map == null) {
			map = new TreeMap<Integer, Number>();
			values.put(key, map);
		}
		return map;
	}
	
	
}
