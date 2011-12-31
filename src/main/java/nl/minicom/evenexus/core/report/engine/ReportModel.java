package nl.minicom.evenexus.core.report.engine;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;

import com.google.common.collect.Lists;

/**
 * The {@link ReportModel} object is responsible for describing a certain
 * report. The user can add/remove, items and groupings.
 *
 * @author Michael
 */
@Singleton
public class ReportModel implements ModelListener {
	
	private final List<WeakReference<ModelListener>> listeners;

	// Report items
	private final Map<String, ReportItem> reportItems;
	
	// Report groupings
	private final List<Model<ReportGroup>> reportGroups;
	
	// Report filters
	private final Model<Date> startDate;
	private final Model<Date> endDate;
	
	// Display type
	private final Model<DisplayType> displayType;
	
	/**
	 * This constructor creates a new {@link ReportModel} object.
	 */
	@Inject
	public ReportModel() {
		this.listeners = Lists.newArrayList();
		
		// Display type
		this.displayType = createModel(DisplayType.TABLE);
		
		// Report items
		this.reportItems = new LinkedHashMap<String, ReportItem>();
		
		// Report groupings
		this.reportGroups = new ArrayList<Model<ReportGroup>>();
		for (int i = 0; i < 3; i++) {
			Model<ReportGroup> groupModel = createModel(null);
			this.reportGroups.add(groupModel);
		}
		
		// Report filters
		this.endDate = createModel(endOfToday());
		this.startDate = createModel(oneMonthAgo());
	}
	
	private <T> Model<T> createModel(T value) {
		Model<T> model = new Model<T>(value);
		model.addListener(this);
		return model;
	}
	
	/**
	 * @return
	 * 		The {@link Model} containing the {@link DisplayType} of this {@link ReportModel}.
	 */
	public Model<DisplayType> getDisplayType() {
		return displayType;
	}
	
	/**
	 * The addItem() method will add the supplied {@link ReportItem} to this {@link ReportModel}.
	 * 
	 * @param item		The {@link ReportItem} to add.
	 */
	public void addItem(ReportItem item) {
		reportItems.put(item.getKey(), item);
	}
	
	/**
	 * The removeItem() method will remove the supplied {@link ReportItem}.
	 * 
	 * @param item		The {@link ReportItem} to remove.
	 */
	public void removeItem(ReportItem item) {
		reportItems.remove(item.getKey());
	}
	
	/**
	 * The addGroup() method will add the supplied {@link ReportGroup} to this {@link ReportModel}.
	 * 
	 * @param group		The {@link ReportGroup} to add.
	 */
	public void addGroup(ReportGroup group) {
		for (int i = 0; i < reportGroups.size(); i++) {
			Model<ReportGroup> model = reportGroups.get(i);
			if (!model.isSet()) {
				model.setValue(group);
				return;
			}
		}
	}
	
	/**
	 * The removeGroup() method will remove the supplied {@link ReportGroup}.
	 * 
	 * @param group		The {@link ReportGroup} to remove.
	 */
	public void removeGroup(ReportGroup group) {
		reportGroups.remove(group);
	}

	/**
	 * @return
	 * 		The {@link Model} managing the start date of the {@link ReportModel}.
	 */
	public Model<Date> getStartDate() {
		return startDate;
	}

	/**
	 * @return
	 * 		The {@link Model} managing the end date of the {@link ReportModel}.
	 */
	public Model<Date> getEndDate() {
		return endDate;
	}

	/**
	 * @return a {@link List} of {@link ReportItem} objects in this {@link ReportModel}.
	 */
	public Collection<ReportItem> getReportItems() {
		return Collections.unmodifiableCollection(reportItems.values());
	}

	/**
	 * @return a {@link List} of {@link ReportGroup} objects in this {@link ReportModel}.
	 */
	public List<ReportGroup> getReportGroups() {
		List<ReportGroup> groups = new ArrayList<ReportGroup>();
		for (Model<ReportGroup> model : reportGroups) {
			if (model.isSet()) {
				groups.add(model.getValue());
			}
		}
		return groups;
	}

	/**
	 * This method checks if this {@link ReportModel} has a {@link ReportItem} with the specified alias.
	 * 
	 * @param itemAlias
	 * 		The alias of the {@link ReportItem} we're looking for.
	 * 
	 * @return
	 * 		True if the {@link ReportItem} is present.
	 */
	public boolean hasItem(String itemAlias) {
		return reportItems.containsKey(itemAlias);
	}

	/**
	 * This method removes the {@link ReportItem} with the specified alias.
	 * 
	 * @param itemAlias
	 * 		The alias of the {@link ReportItem} to remove.
	 */
	public void removeItem(String itemAlias) {
		reportItems.remove(itemAlias);
	}

	/**
	 * @return
	 * 		The {@link Model} of the first {@link ReportGroup}.
	 */
	public Model<ReportGroup> getGrouping1() {
		return reportGroups.get(0);
	}

	/**
	 * @return
	 * 		The {@link Model} of the second {@link ReportGroup}.
	 */
	public Model<ReportGroup> getGrouping2() {
		return reportGroups.get(1);
	}

	/**
	 * @return
	 * 		The {@link Model} of the third {@link ReportGroup}.
	 */
	public Model<ReportGroup> getGrouping3() {
		return reportGroups.get(2);
	}

	/**
	 * @return
	 * 		True if this {@link ReportModel} is valid, or false if it is not.
	 */
	public boolean isValid() {
		if (reportItems.isEmpty()) {
			return false;
		}
		
		for (Model<ReportGroup> group : reportGroups) {
			if (group.isEnabled() && group.isSet()) {
				return true;
			}
		}
		
		return false;
	}

	private Date oneMonthAgo() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}

	private Date endOfToday() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}
	
	/**
	 * Adds a {@link ModelListener} to notify when changes occur to any {@link Model} in the {@link ReportModel}.
	 * 
	 * @param listener
	 * 		The {@link ModelListener} to add.
	 */
	public void addListener(ModelListener listener) {
		if (listener == this) {
			throw new IllegalStateException();
		}
		
		synchronized (listeners) {
			listeners.add(new WeakReference<ModelListener>(listener));
		}
	}

	@Override
	public void onValueChanged() {
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

	@Override
	public void onStateChanged() {
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
	
}
