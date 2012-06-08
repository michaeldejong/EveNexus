package nl.minicom.evenexus.core.report.engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.definition.components.ReportGroup.Type;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ModificationListener;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * The {@link ReportModel} object is responsible for describing a certain
 * report. The user can add/remove, items and groupings.
 *
 * @author Michael
 */
@Singleton
public class ReportModel implements ModificationListener { 
	
	private final Set<ItemListener> itemListeners;
	private final Set<GroupListener> groupListeners;
	private final Set<ModificationListener> modificationListeners;
	
	private final Map<String, ReportItem> reportItems;
	private final List<Model<ReportGroup>> reportGroups;

	private final Model<Date> startDate;
	private final Model<Date> endDate;

	private final Model<DisplayType> displayType;
	
	/**
	 * This constructor creates a new {@link ReportModel} object.
	 */
	@Inject
	public ReportModel() {
		this.itemListeners = Sets.newHashSet();
		this.groupListeners = Sets.newHashSet();
		this.modificationListeners = Sets.newHashSet();
		this.displayType = createModel(DisplayType.values()[0]);
		this.reportItems = new LinkedHashMap<String, ReportItem>();
		this.reportGroups = new ArrayList<Model<ReportGroup>>();

		this.endDate = createModel(endOfToday());
		this.startDate = createModel(oneMonthAgo());

		for (int i = 0; i < 3; i++) {
			Model<ReportGroup> groupModel = createModel(null);
			groupModel.setEnabled(false);
			reportGroups.add(groupModel);
			groupModel.addListener(new ModificationListener() {
				@Override
				public void onModification() {
					for (GroupListener listener : groupListeners) {
						listener.onReportGroupsModified();
					}
				}
			});
		}
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
		
		for (ItemListener listener : itemListeners) {
			listener.onReportItemAdded(item);
		}
		onModification();
	}
	
	/**
	 * The removeItem() method will remove the supplied {@link ReportItem}.
	 * 
	 * @param item		The {@link ReportItem} to remove.
	 */
	public void removeItem(ReportItem item) {
		removeItem(item.getKey());
	}

	/**
	 * This method removes the {@link ReportItem} with the specified alias.
	 * 
	 * @param itemAlias
	 * 		The alias of the {@link ReportItem} to remove.
	 */
	public void removeItem(String itemAlias) {
		ReportItem item = reportItems.remove(itemAlias);
		
		for (ItemListener listener : itemListeners) {
			listener.onReportItemRemoved(item);
		}
		onModification();
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
			if (model.isSet() && model.isEnabled()) {
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

	public interface ItemListener {
		void onReportItemAdded(ReportItem item);
		void onReportItemRemoved(ReportItem item);
	}

	public interface GroupListener {
		void onReportGroupsModified();
	}

	public void addListener(ItemListener listener) {
		itemListeners.add(listener);
	}
	
	public void addListener(GroupListener listener) {
		groupListeners.add(listener);
	}

	public void addListener(ModificationListener listener) {
		modificationListeners.add(listener);
	}

	public void removeListener(ItemListener listener) {
		itemListeners.remove(listener);
	}
	
	public void removeListener(GroupListener listener) {
		groupListeners.remove(listener);
	}

	public void removeListener(ModificationListener listener) {
		modificationListeners.remove(listener);
	}

	@Override
	public void onModification() {
		for (ModificationListener listener : modificationListeners) {
			listener.onModification();
		}
	}

	public Collection<Type> getDisplayTypes() {
		Set<Type> displayedTypes = Sets.newHashSet();
		for (Model<ReportGroup> model : reportGroups) {
			if (model.isEnabled() && model.isSet()) {
				displayedTypes.add(model.getValue().getType());
			}
		}
		return displayedTypes;
	}
	
}
