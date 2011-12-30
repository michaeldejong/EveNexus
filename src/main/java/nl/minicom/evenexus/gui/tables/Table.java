package nl.minicom.evenexus.gui.tables;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.gui.tables.columns.listeners.TableColumnResizeModelListener;
import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;

import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Table extends JTable {
	
	private static final long serialVersionUID = -5591796667411345474L;

	private static final Logger LOG = LoggerFactory.getLogger(Table.class);

	private final List<Map<String, Object>> data;
	private final BugReportDialog dialog;
	
	private ScrollableResults result;
	private ColumnModel columnModel;
	private ITableDataModel tableDataModel;
	
	private volatile boolean isInitialized = false;
	
	@Inject
	public Table(BugReportDialog dialog) {
		this.data = new ArrayList<Map<String, Object>>();
		this.dialog = dialog;
	}
	
	public void initialize(ITableDataModel tableDataModel, ColumnModel columns) {
		synchronized (this) {
			if (isInitialized) {
				throw new IllegalStateException("Table was already initialized!");
			}
			
			this.tableDataModel = tableDataModel;
			this.columnModel = columns;
			this.isInitialized = true;
			
			setRowHeight(21);		
			getTableHeader().setReorderingAllowed(false);
			setFillsViewportHeight(true);
			setSelectionBackground(new Color(51, 153, 255));
			setGridColor(new Color(223, 223, 223));
			
			reload();
			setModel(createTableModel());		
			updateColumns();
			setAutoCreateRowSorter(true);
			
			getColumnModel().addColumnModelListener(new TableColumnResizeModelListener(columnModel));
		}
	}
	
	private void updateColumns() {		
		for (int i = 0; i < columnModel.getSize(); i++) {
			Column column = columnModel.getColumnAtIndex(i);
			if (column.isVisible() && isRemoved(column)) {
				int newIndex = columnModel.getVisibleIndex(column);
				
				TableColumn newColumn = new TableColumn();
				newColumn.setHeaderValue(column.getName());
				addColumn(newColumn);				
				moveColumn(getColumnCount() - 1, newIndex);
				
				updateColumnIndices();
				
				if (column.getRenderer() != null) {
					newColumn.setCellRenderer(column.getRenderer());
				}
				if (column.getWidth() > 0) {
					newColumn.setPreferredWidth(column.getWidth());
				}
			}
			else if (column.isVisible() && !isRemoved(column) && getColumnCount() > 0) {
				if (column.getRenderer() != null) {
					getColumn(column.getName()).setCellRenderer(column.getRenderer());
				}
				if (column.getWidth() > 0) {
					getColumn(column.getName()).setPreferredWidth(column.getWidth());
				}
			}
			else if (!column.isVisible() && !isRemoved(column) && getColumnCount() > 0) {
				getColumnModel().removeColumn(getColumn(column.getName()));				
				updateColumnIndices();
			}
		}
	}

	private boolean isRemoved(Column column) {
		for (int i = 0; i < getColumnCount(); i++) {
			if (getColumnModel().getColumn(i).getHeaderValue().equals(column.getName())) {
				return false;
			}
		}
		return true;
	}

	private void updateColumnIndices() {
		int count = 0;
		Enumeration<TableColumn> columnEnum = getColumnModel().getColumns();
		while (columnEnum.hasMoreElements()) {
			TableColumn c = columnEnum.nextElement();
			c.setModelIndex(count);
			count++;
		}
	}
	
	public final void reload() {
		synchronized (this) {
			if (!isInitialized) {
				return;
			}
			
			try {
				data.clear();
				String[] fields = tableDataModel.getFields();
				List<Object[]> values = tableDataModel.reload();
				for (Object[] value : values) {
					Map<String, Object> row = new TreeMap<String, Object>();
					for (int i = 0; i < columnModel.getSize(); i++) {
						Column column = columnModel.getColumnAtIndex(i);
						int index = indexOf(fields, column.getColumn());
						if (index >= 0) {
							row.put(column.getName(), value[index]);
						}
					}
					data.add(row);
				}
				
				updateColumns();
				revalidate();
				repaint();
			}
			catch (Exception e) {
				LOG.error(e.getLocalizedMessage(), e);
				dialog.setVisible(true);
			}
		}
	}
	
	private int indexOf(String[] fields, String fieldName) {
		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			if (field.equalsIgnoreCase(fieldName)) {
				return i;
			}
		}
		return -1;
	}
	
	public final ScrollableResults getSelectedResultRow() {
		synchronized (this) {
			try {
				result.setRowNumber(getSelectedRow());
			}
			catch (Exception e) {
				LOG.error(e.getLocalizedMessage(), e);
				dialog.setVisible(true);
			}
			return result;
		}
	}

	public void delete(int selectedIndex) {
		synchronized (this) {
			try {
				result.setRowNumber(selectedIndex);
				// TODO : remove...
			}
			catch (Exception e) {
				LOG.error(e.getLocalizedMessage(), e);
				dialog.setVisible(true);
			}
		}
	}
	
	private TableModel createTableModel() {
		
		return new TableModel() {
			
			@Override
			public void setValueAt(Object arg0, int arg1, int arg2) {
				// Do nothing.
			}
			
			@Override
			public void removeTableModelListener(TableModelListener arg0) {
				// Do nothing.
			}
			
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}
			
			@Override
			public Object getValueAt(int row, int columnIndex) {
				if (data == null || data.size() <= row) {
					return "";
				}
				
				Map<String, Object> rowMap = data.get(row);
				if (rowMap == null || rowMap.size() <= columnIndex || columnModel.getVisibleSize() <= columnIndex) {
					return "";
				}
				
				Column column = columnModel.getVisible(columnIndex);
				if (column == null || column.getName() == null) {
					return "";
				}
				
				return rowMap.get(column.getName());
			}
			
			@Override
			public int getRowCount() {
				return data.size();
			}
			
			@Override
			public String getColumnName(int columnIndex) {
				return columnModel.getVisible(columnIndex).getName();
			}
			
			@Override
			public int getColumnCount() {
				return columnModel.getVisibleSize();
			}
			
			@Override
			public Class<?> getColumnClass(int column) {
				if (getRowCount() == 0) {
					return String.class;
				}
				return columnModel.getVisible(column).getValueClass();
			}
			
			@Override
			public void addTableModelListener(TableModelListener arg0) {
				// Do nothing.
			}
		};
	}

	public ITableDataModel getDataModel() {
		synchronized (this) {
			return tableDataModel;
		}
	}

	public ColumnModel getColumns() {
		synchronized (this) {
			return columnModel;
		}
	}
	
}
