package nl.minicom.evenexus.gui.tables.columns;


import javax.swing.table.TableModel;

import nl.minicom.evenexus.gui.tables.Table;

/**
 * This dialog allows the user to select what columns to show in the {@link Table}.
 * 
 * @author michael
 */
public class TableColumnSelectionFrame extends ColumnSelectionFrame {

	private static final long serialVersionUID = 1L;
	
	private final TableModel tableModel;
	
	/**
	 * Constructs a new {@link TableColumnSelectionFrame}.
	 * 
	 * @param model
	 * 		The {@link ColumnModel}.
	 * 
	 * @param tables
	 * 		The {@link Table}s.
	 */
	public TableColumnSelectionFrame(ColumnModel model, Table... tables) {
		this.tableModel = new TableColumnSelectionTableModel(model, tables);		
		createGUI();
	}

	/**
	 * @return
	 * 		The {@link TableModel} of this {@link TableColumnSelectionFrame}.
	 */
	public TableModel createTableModel() {
		return tableModel;
	}
	
}
