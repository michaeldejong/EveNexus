package nl.minicom.evenexus.gui.tables.columns;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import nl.minicom.evenexus.gui.panels.dashboard.LineGraphEngine;

/**
 * This dialog allows the user to define what elements to display in the {@link LineGraphEngine}.
 * 
 * @author michael
 */
public class GraphColumnSelectionFrame extends ColumnSelectionFrame {

	private static final long serialVersionUID = 1L;
	
	private final LineGraphEngine model;
	
	/**
	 * Constructs a new {@link GraphColumnSelectionFrame}.
	 * 
	 * @param model
	 * 		The {@link LineGraphEngine}.
	 */
	public GraphColumnSelectionFrame(LineGraphEngine model) {
		this.model = model;
		createGUI();
	}

	/**
	 * This method creates the new {@link TableModel}.
	 * 
	 * @return
	 * 		The constructed {@link TableModel}.
	 */
	protected TableModel createTableModel() {
		return new TableModel() {					
				@Override
				public void setValueAt(Object value, int row, int column) {
					model.getGraphElements().get(row).setVisible((Boolean) value);
					model.reload();
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
						return " " + model.getGraphElements().get(row).getName() + " ";
					}
					else if (column == 1) {
						return (Boolean) model.getGraphElements().get(row).isVisible();
					}
					return null;
				}
				
				@Override
				public int getRowCount() {
					return model.getGraphElements().size();
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
		};
	}
	
}
