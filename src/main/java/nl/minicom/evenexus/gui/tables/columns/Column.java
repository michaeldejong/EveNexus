package nl.minicom.evenexus.gui.tables.columns;


import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.table.TableCellRenderer;

import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.utils.SettingsManager;


public class Column {
		
	private final SettingsManager settingsManager;
	private String name;
	private String column;
	private boolean visible;
	private String visibleSetting;
	private boolean removed;
	private TableCellRenderer renderer;
	private AbstractFormatter formatter;
	private int width;
	private String widthSetting;
	private Class<?> classReference;
	private int defaultWidth;
				
	public Column(SettingsManager settingsManager, String name, String column, String visibleSetting, boolean defaultVisible, String widthSetting, int defaultWidth) {
		this(settingsManager, name, column, visibleSetting, defaultVisible, widthSetting, defaultWidth, null, null);
	}
		
	public Column(SettingsManager settingsManager, String name, String column, String visibleSetting, boolean defaultVisible, String widthSetting, int defaultWidth, Class<?> classReference) {
		this(settingsManager, name, column, visibleSetting, defaultVisible, widthSetting, defaultWidth, classReference, Table.LEFT_RENDERER);
	}
		
	public Column(SettingsManager settingsManager, String name, String column, String visibleSetting, boolean defaultVisible, String widthSetting, int defaultWidth, Class<?> classReference, TableCellRenderer renderer) {
		this.name = name;
		this.settingsManager = settingsManager;
		this.column = column;
		this.renderer = renderer;
		this.widthSetting = widthSetting;
		this.visibleSetting = visibleSetting;
		this.classReference = classReference;
		this.defaultWidth = defaultWidth;
		
		if (widthSetting == null) {
			this.width = defaultWidth;
		}
		else {
			this.width = settingsManager.loadInt(widthSetting, defaultWidth);
		}
		
		if (visibleSetting == null) {
			this.visible = defaultVisible;
		}
		else {
			this.visible = settingsManager.loadBoolean(visibleSetting, defaultVisible);
		}
		
		this.removed = !visible;
	}

	public String getName() {
		return name;
	}
	
	public String getColumn() {
		return column;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
		settingsManager.saveObject(widthSetting, width);
	}

	public AbstractFormatter getFormatter() {
		return formatter;
	}

	public TableCellRenderer getRenderer() {
		return renderer;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		settingsManager.saveObject(visibleSetting, visible);
	}
	
	public Class<?> getValueClass() {
		return classReference;
	}

	public int getDefaultWidth() {
		return defaultWidth;
	}
}
