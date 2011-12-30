package nl.minicom.evenexus.gui.tables.columns.listeners;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;

public class TableColumnResizeModelListener implements TableColumnModelListener {

	private final ColumnModel model;
	
	public TableColumnResizeModelListener(ColumnModel model) {
		this.model = model;
	}
	
	@Override
	public void columnMarginChanged(ChangeEvent e) {
		TableColumnModel tableColumnModel = (TableColumnModel) e.getSource();
		for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
			TableColumn c = tableColumnModel.getColumn(i);
			Column column = model.getColumnWithName((String) c.getHeaderValue());
			column.setWidth(c.getWidth());
		}
	}
	
	@Override
	public void columnAdded(TableColumnModelEvent e) {
		// Do nothing.
	}
	
	@Override
	public void columnSelectionChanged(ListSelectionEvent e) {
		// Do nothing.
	}
	
	@Override
	public void columnRemoved(TableColumnModelEvent e) {
		// Do nothing.
	}
	
	@Override
	public void columnMoved(TableColumnModelEvent e) {
		// Do nothing.
	}

}
