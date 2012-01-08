package nl.minicom.evenexus.core.report.engine;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.collect.Lists;

/**
 * The {@link Dataset} class is a simple data holder object, which 
 * basically creates a sparse table. It uses {@link TreeMap} objects
 * and {@link TreeMap} objects for quick and easy insertion 
 * and retrieval of values.
 * 
 * @author Michael
 */
public class Dataset {
	
	private final Map<String, GroupEntry> values;
	private final boolean orderBySize;
	
	/**
	 * This constructor creates an empty {@link Dataset} object.
	 */
	public Dataset(boolean orderBySize) {
		this(new TreeSet<String>(), orderBySize);
	}
	
	/**
	 * This constructor creates a {@link Dataset} object with predefined groups entries.
	 * 
	 * @param groupKeys
	 * 		The {@link Set} of predefined group entries. 
	 */
	public Dataset(Set<String> groupKeys, boolean orderBySize) {
		this.values = new TreeMap<String, GroupEntry>();
		this.orderBySize = orderBySize;
		
		for (String group : groupKeys) {
			values.put(group, new GroupEntry());
		}
	}

	/**
	 * @return		
	 * 		The horizontal size.
	 */
	public int groupSize() {
		return values.size();
	}
	
	/**
	 * @return	A {@link List} of {@link String} keys.
	 */
	public List<String> groupSet() {
		List<String> keys = Lists.newArrayList(values.keySet());
		
		if (orderBySize) {
			Collections.sort(keys, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					BigDecimal value1 = values.get(o1).getTotal();
					BigDecimal value2 = values.get(o2).getTotal();
					return value2.compareTo(value1);
				}
			});
		}
		
		return Collections.unmodifiableList(keys);
	}
	
	/**
	 * This method inserts a value at a specified location in the {@link Dataset}.
	 * 
	 * @param item		The key for which to add the value.
	 * @param group	The dataset index to add the value for.
	 * @param value		The value to insert.
	 */
	public void setValue(String item, String group, BigDecimal value) {
		GroupEntry entry = values.get(group);
		if (entry == null) {
			entry = new GroupEntry();
			values.put(group, entry);
		}
		entry.setValue(item, value);
	}
	
	/**
	 * This method returns the value of a specified location in the {@link Dataset}.
	 * 
	 * @param item		The key for which to retrieve the value.
	 * @param group		The dataset index to retrieve the value for.
	 * @return			The value at the specified location.
	 */
	public BigDecimal getValue(String item, String group) {
		GroupEntry map = values.get(group);
		if (map == null) {
			return BigDecimal.ZERO;
		}
		return map.getValue(item);
	}
	
	private class GroupEntry {
		
		private final Map<String, BigDecimal> values;
		
		private BigDecimal total = BigDecimal.ZERO;
		
		private GroupEntry() {
			this.values = new TreeMap<String, BigDecimal>();
		}
		
		public void setValue(String item, BigDecimal value) {
			removeItem(item);
			addItem(item, value);
		}
		
		private void removeItem(String item) {
			BigDecimal removed = values.remove(item);
			if (removed != null) {
				total = total.subtract(removed);
			}
		}
		
		private void addItem(String item, BigDecimal value) {
			values.put(item, value);
			total = total.add(value);
		}
		
		public BigDecimal getTotal() {
			return total;
		}
		
		public BigDecimal getValue(String item) {
			BigDecimal value = values.get(item);
			if (value == null) {
				return BigDecimal.ZERO;
			}
			return value;
		}
		
	}

}
