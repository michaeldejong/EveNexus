package nl.minicom.evenexus.gui.tables.columns;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.renderers.AlignLeftRenderer;
import nl.minicom.evenexus.gui.tables.renderers.AlignRightRenderer;
import nl.minicom.evenexus.gui.tables.renderers.CurrencyRenderer;
import nl.minicom.evenexus.gui.tables.renderers.DateTimeRenderer;
import nl.minicom.evenexus.gui.tables.renderers.IntegerRenderer;

public class ColumnModel {
	
	@Inject public static AlignLeftRenderer ALIGN_LEFT;
	@Inject public static AlignRightRenderer ALIGN_RIGHT;
	@Inject public static CurrencyRenderer CURRENCY;
	@Inject public static DateTimeRenderer DATE_TIME;
	@Inject public static IntegerRenderer INTEGER;
	
	private final List<Column> columns;
	private final List<Column> visibleColumns;
	
	public ColumnModel() {
		this.columns = new ArrayList<Column>();
		this.visibleColumns = new ArrayList<Column>();
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
			if (c == column) {
				return i;
			}
		}
		return -1;
	}
	
}
