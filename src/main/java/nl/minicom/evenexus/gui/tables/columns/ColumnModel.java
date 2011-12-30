package nl.minicom.evenexus.gui.tables.columns;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.renderers.AlignLeftRenderer;
import nl.minicom.evenexus.gui.tables.renderers.AlignRightRenderer;
import nl.minicom.evenexus.gui.tables.renderers.CurrencyRenderer;
import nl.minicom.evenexus.gui.tables.renderers.DateTimeRenderer;
import nl.minicom.evenexus.gui.tables.renderers.IntegerRenderer;
import nl.minicom.evenexus.gui.tables.renderers.PercentageRenderer;
import nl.minicom.evenexus.utils.SettingsManager;

/**
 * This class defines a set of columns which can be used in a Table.
 *
 * @author michael
 */
public abstract class ColumnModel {
	
	public static final AlignLeftRenderer ALIGN_LEFT = new AlignLeftRenderer();
	public static final AlignRightRenderer ALIGN_RIGHT = new AlignRightRenderer();
	public static final CurrencyRenderer CURRENCY = new CurrencyRenderer();
	public static final DateTimeRenderer DATE_TIME = new DateTimeRenderer();
	public static final IntegerRenderer INTEGER = new IntegerRenderer();
	public static final PercentageRenderer PERCENTAGE = new PercentageRenderer();
		
	private final List<Column> columns;
	private final List<Column> visibleColumns;
	private final SettingsManager settingsManager;
	
	/**
	 * Constructs a new {@link ColumnModel} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 */
	@Inject
	public ColumnModel(SettingsManager settingsManager) {
		this.columns = new ArrayList<Column>();
		this.visibleColumns = new ArrayList<Column>();
		this.settingsManager = settingsManager;
	}
	
	/**
	 * This method initializes the {@link ColumnModel}.
	 */
	public abstract void initialize();
	
	/**
	 * @return
	 * 		The {@link SettingsManager} object.
	 */
	protected SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	/**
	 * This method adds a new {@link Column} to this {@link ColumnModel}.
	 * 
	 * @param column
	 * 		The {@link Column} to add.
	 */
	public void add(Column column) {
		columns.add(column);
		synchronize();
	}
	
	/**
	 * This method synchronizes the visible columns.
	 */
	public void synchronize() {
		visibleColumns.clear();
		for (Column column : columns) {
			if (column.isVisible()) {
				visibleColumns.add(column);
			}
		}
	}

	/**
	 * This method returns the {@link Column} with the specified index.
	 * 
	 * @param index
	 * 		The index of the {@link Column} to fetch.
	 * 
	 * @return
	 * 		The {@link Column} on the specified index, or NULL if none was found.
	 */
	public Column getColumnAtIndex(int index) {
		return columns.get(index);
	}

	/**
	 * This method returns the {@link Column} with the specified name.
	 * 
	 * @param name
	 * 		The name to look for.
	 * 
	 * @return
	 * 		The {@link Column} with the specified name, or NULL if none was found.
	 */
	public Column getColumnWithName(String name) {
		for (Column column : columns) {
			if (column.getName().equals(name)) {
				return column;
			}
		}
		return null;
	}
	
	/**
	 * This method returns the visibile {@link Column} on the specified index.
	 * 
	 * @param index
	 * 		The index of the visible {@link Column} to fetch.
	 * 
	 * @return
	 * 		The visible {@link Column} on the specified index, or NULL if none was found.
	 */
	public Column getVisible(int index) {
		return visibleColumns.get(index);
	}

	/**
	 * @return
	 * 		The amount of {@link Column}s present in this {@link ColumnModel}.
	 */
	public int getSize() {
		return columns.size();
	}
	
	/**
	 * @return
	 * 		The amount of visible {@link Column}s in this {@link ColumnModel}.
	 */
	public int getVisibleSize() {
		return visibleColumns.size();
	}

	/**
	 * This method returns the index of the provided visible {@link Column}.
	 * 
	 * @param column
	 * 		The {@link Column} to perform the index lookup for.
	 * 
	 * @return
	 * 		The index of the {@link Column}, or -1 if it could not be found.
	 */
	public int getVisibleIndex(Column column) {
		synchronize();
		for (int i = 0; i < visibleColumns.size(); i++) {
			Column c = visibleColumns.get(i);
			if (c.equals(column)) {
				return i;
			}
		}
		return -1;
	}
	
}
