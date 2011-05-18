package nl.minicom.evenexus.gui.tables.columns;


import javax.swing.table.TableModel;

import nl.minicom.evenexus.gui.tables.Table;

public class TableColumnSelectionFrame extends ColumnSelectionFrame {

	private static final long serialVersionUID = 1L;
	
	private final TableModel tableModel;
	
	public TableColumnSelectionFrame(ColumnModel model, Table... tables) {
		super();
		this.tableModel = new TableColumnSelectionTableModel(model, tables);		
		createGUI();
	}

	public TableModel createTableModel() {
		return tableModel;
	}
	
}
