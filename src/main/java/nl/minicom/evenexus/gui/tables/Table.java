package nl.minicom.evenexus.gui.tables;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import nl.minicom.evenexus.gui.tables.columns.Column;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.gui.tables.columns.listeners.TableColumnResizeModelListener;
import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.gui.tables.renderers.AlignLeftRenderer;
import nl.minicom.evenexus.gui.tables.renderers.AlignRightRenderer;
import nl.minicom.evenexus.gui.tables.renderers.CurrencyRenderer;
import nl.minicom.evenexus.gui.tables.renderers.DateTimeRenderer;
import nl.minicom.evenexus.gui.tables.renderers.IntegerRenderer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;


public class Table extends JTable {
	
	private static final long serialVersionUID = -5591796667411345474L;

	private static final Logger logger = LogManager.getRootLogger();

	public static final DefaultTableCellRenderer CURRENCY_RENDERER = new CurrencyRenderer();
	public static final DefaultTableCellRenderer LEFT_RENDERER = new AlignLeftRenderer();
	public static final DefaultTableCellRenderer RIGHT_RENDERER = new AlignRightRenderer();
	public static final DefaultTableCellRenderer DATE_TIME_RENDERER = new DateTimeRenderer();
	public static final DefaultTableCellRenderer INTEGER_RENDERER = new IntegerRenderer();

	private ScrollableResults result;
	private final ColumnModel columnModel;
	private final ITableDataModel tableDataModel;
	private final List<Map<String, Object>> data;
	
	public Table(ITableDataModel tableDataModel, ColumnModel columns) {
		super();
		this.data = new ArrayList<Map<String, Object>>();
		this.tableDataModel = tableDataModel;
		this.columnModel = columns;
		
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
	
	public final void updateColumns() {		
		for (int i = 0; i < columnModel.getSize(); i++) {
			Column column = columnModel.get(i);
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
		try {
			data.clear();
			String[] fields = tableDataModel.getFields();
			List<Object[]> values = tableDataModel.reload();
			for (Object[] value : values) {
				Map<String, Object> row = new TreeMap<String, Object>();
				for (int i = 0; i < columnModel.getSize(); i++) {
					Column column = columnModel.get(i);
					row.put(column.getName(), value[indexOf(fields, column.getColumn())]);
				}
				data.add(row);
			}
			
			updateColumns();
			revalidate();
			repaint();
		}
		catch (Exception e) {
			logger.error(e);
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
		try {
			result.setRowNumber(getSelectedRow());
		}
		catch (Exception e) {
			logger.error(e);
		}
		return result;
	}

	public void delete(int selectedIndex) {
		try {
			result.setRowNumber(selectedIndex);
			// TODO : remove...
		}
		catch (Exception e) {
			logger.error(e);
		}
	}
	
	private final TableModel createTableModel() {
		
		return new TableModel() {
			
			@Override
			public void setValueAt(Object arg0, int arg1, int arg2) {
			}
			
			@Override
			public void removeTableModelListener(TableModelListener arg0) {
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
			}
		};
	}

	public ITableDataModel getDataModel() {
		return tableDataModel;
	}

	public ColumnModel getColumns() {
		return columnModel;
	}
	
}
