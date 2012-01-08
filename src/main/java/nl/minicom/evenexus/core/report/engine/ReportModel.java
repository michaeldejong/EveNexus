package nl.minicom.evenexus.core.report.engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
public class ReportModel {
	
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
		// Display type
		this.displayType = createModel(DisplayType.TABLE);
		
		// Report items
		this.reportItems = new LinkedHashMap<String, ReportItem>();
		
		// Report groupings
		this.reportGroups = new ArrayList<Model<ReportGroup>>();
		for (int i = 0; i < 3; i++) {
			Model<ReportGroup> groupModel = createModel(null);
			groupModel.disable();
			this.reportGroups.add(groupModel);
		}
		
		// Report filters
		this.endDate = createModel(endOfToday());
		this.startDate = createModel(oneMonthAgo());
	}
	
	private <T> Model<T> createModel(T value) {
		return new Model<T>(value);
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
	 * This method removes the {@link ReportItem} with the specified alias.
	 * 
	 * @param itemAlias
	 * 		The alias of the {@link ReportItem} to remove.
	 */
	public void removeItem(String itemAlias) {
		reportItems.remove(itemAlias);
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
	public List<ReportItem> getReportItems() {
		List<ReportItem> items = Lists.newArrayList(reportItems.values());
		Collections.sort(items, new Comparator<ReportItem>() {
			@Override
			public int compare(ReportItem arg0, ReportItem arg1) {
				return arg0.getUnit().ordinal() - arg1.getUnit().ordinal();
			}
		});
		
		return Collections.unmodifiableList(items);
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
	 * This method returns the ReportItem with the specified itemAlias.
	 * 
	 * @param itemAlias
	 * 		The alias of the {@link ReportItem}.
	 * 
	 * @return
	 * 		The {@link ReportItem} with the itemAlias, or NULL if none was found.
	 */
	public ReportItem getReportItem(String itemAlias) {
		return reportItems.get(itemAlias);
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
	
}
