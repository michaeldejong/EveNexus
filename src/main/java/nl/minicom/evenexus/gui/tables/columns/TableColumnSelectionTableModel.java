package nl.minicom.evenexus.gui.tables.columns;


import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import nl.minicom.evenexus.gui.tables.Table;

public class TableColumnSelectionTableModel implements TableModel {
	
	private final ColumnModel model;
	private final Table[] tables;
	
	public TableColumnSelectionTableModel(ColumnModel model, Table[] tables) {
		this.model = model;
		this.tables = tables;
	}

	public void setValueAt(Object value, int row, int column) {
		model.get(row).setVisible((Boolean) value);
		model.synchronize();
		for (Table table : tables) {
			table.reload();
		}
	}
	
	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		// Do nothing.
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 1) {
			return true;
		}
		return false;
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		if (column == 0) {
			return " " + model.get(row).getName() + " ";
		}
		else if (column == 1) {
			return (Boolean) model.get(row).isVisible();
		}
		return null;
	}
	
	@Override
	public int getRowCount() {
		return model.getSize();
	}
	
	@Override
	public String getColumnName(int column) {
		if (column == 0) {
			return "Column";
		}
		else if (column == 1) {
			return "Visible";
		}
		return "";
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}
	
	@Override
	public Class<?> getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	}
	
	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// Do nothing.
	}
	
}
