package nl.minicom.evenexus.core.report.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;

/**
 * The {@link ReportModel} object is responsible for describing a certain
 * report. The user can add/remove, items and groupings.
 *
 * @author Michael
 */
public class ReportModel {

	// Report items
	private Map<String, ReportItem> reportItems;
	
	// Report groupings
	private List<Model<ReportGroup>> reportGroups;
	
	// Display type
	private Model<DisplayType> displayType;
	
	// Listeners
	private List<ReportModelListener> listeners;
	
	/**
	 * This constructor creates a new {@link ReportModel} object.
	 */
	public ReportModel() {
		this.displayType = new Model<DisplayType>(this, DisplayType.TABLE);
		
		// Report items
		this.reportItems = new LinkedHashMap<String, ReportItem>();
		
		// Report groupings
		this.reportGroups = new ArrayList<Model<ReportGroup>>();
		for (int i = 0; i < 3; i++) {
			this.reportGroups.add(new Model<ReportGroup>(this));
		}
		
		this.listeners = new ArrayList<ReportModelListener>();
	}
	
	public void addListener(ReportModelListener listener) {
		listeners.add(listener);
	}
	
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
			groups.add(model.getValue());
		}
		return groups;
	}

	public boolean hasItem(String itemAlias) {
		return reportItems.containsKey(itemAlias);
	}

	public void removeItem(String itemAlias) {
		reportItems.remove(itemAlias);
	}

	public Model<ReportGroup> getGrouping1() {
		return reportGroups.get(0);
	}

	public Model<ReportGroup> getGrouping2() {
		return reportGroups.get(1);
	}

	public Model<ReportGroup> getGrouping3() {
		return reportGroups.get(2);
	}

	public void onValueChanged() {
		for (ReportModelListener listener : listeners) {
			listener.onValueChanged();
		}
	}

	public boolean isValid() {
		return !reportItems.isEmpty();
	}
	
}
