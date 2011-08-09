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
	
	@Inject
	public ColumnModel(SettingsManager settingsManager) {
		this.columns = new ArrayList<Column>();
		this.visibleColumns = new ArrayList<Column>();
		this.settingsManager = settingsManager;
	}
	
	public abstract void initialize();
	
	protected SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	public void add(Column column) {
		columns.add(column);
		synchronize();
	}
	
	public void synchronize() {
		visibleColumns.clear();
		for (Column column : columns) {
			if (column.isVisible()) {
				visibleColumns.add(column);
			}
		}
	}

	public Column get(int i) {
		return columns.get(i);
	}

	public Column getVisible(String name) {
		for (Column column : columns) {
			if (column.getName().equals(name)) {
				return column;
			}
		}
		return null;
	}
	
	public Column getVisible(int index) {
		return visibleColumns.get(index);
	}

	public int getSize() {
		return columns.size();
	}
	
	public int getVisibleSize() {
		return visibleColumns.size();
	}

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
