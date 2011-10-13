package nl.minicom.evenexus.gui.tables.columns;


import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.table.TableCellRenderer;

import nl.minicom.evenexus.utils.SettingsManager;

/**
 * This class represents a {@link Column} in a table.
 * 
 * @author michael
 */
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
	
	/**
	 * This constructs a new {@link Column} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 * 
	 * @param name
	 * 		The name of this {@link Column}.
	 * 
	 * @param column
	 * 		The identifier of this {@link Column}.
	 * 
	 * @param visibleSetting
	 * 		The setting name which identifies the visibility setting.
	 * 
	 * @param defaultVisible
	 * 		True if this {@link Column} is visible by default.
	 * 
	 * @param widthSetting
	 * 		The setting name which identifies the width setting.
	 * 
	 * @param defaultWidth
	 * 		The default width of this {@link Column}.
	 */	
	public Column(
			SettingsManager settingsManager, 
			String name, 
			String column, 
			String visibleSetting, 
			boolean defaultVisible, 
			String widthSetting, 
			int defaultWidth) {
		
		this(
				settingsManager, 
				name, 
				column, 
				visibleSetting, 
				defaultVisible, 
				widthSetting, 
				defaultWidth, 
				null, 
				null
		);
	}
	
	/**
	 * This constructs a new {@link Column} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 * 
	 * @param name
	 * 		The name of this {@link Column}.
	 * 
	 * @param column
	 * 		The identifier of this {@link Column}.
	 * 
	 * @param visibleSetting
	 * 		The setting name which identifies the visibility setting.
	 * 
	 * @param defaultVisible
	 * 		True if this {@link Column} is visible by default.
	 * 
	 * @param widthSetting
	 * 		The setting name which identifies the width setting.
	 * 
	 * @param defaultWidth
	 * 		The default width of this {@link Column}.
	 * 
	 * @param classReference
	 * 		The class representing the values in this {@link Column}.
	 */
	public Column(
			SettingsManager settingsManager, 
			String name, 
			String column, 
			String visibleSetting, 
			boolean defaultVisible, 
			String widthSetting, 
			int defaultWidth, 
			Class<?> classReference) {
		
		this(
				settingsManager, 
				name, 
				column, 
				visibleSetting, 
				defaultVisible, 
				widthSetting, 
				defaultWidth, 
				classReference, 
				ColumnModel.ALIGN_LEFT
		);
	}
	
	/**
	 * This constructs a new {@link Column} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 * 
	 * @param name
	 * 		The name of this {@link Column}.
	 * 
	 * @param column
	 * 		The identifier of this {@link Column}.
	 * 
	 * @param visibleSetting
	 * 		The setting name which identifies the visibility setting.
	 * 
	 * @param defaultVisible
	 * 		True if this {@link Column} is visible by default.
	 * 
	 * @param widthSetting
	 * 		The setting name which identifies the width setting.
	 * 
	 * @param defaultWidth
	 * 		The default width of this {@link Column}.
	 * 
	 * @param classReference
	 * 		The class representing the values in this {@link Column}.
	 * 
	 * @param renderer
	 * 		The renderer which will be used to render the values.
	 */
	public Column(
			SettingsManager settingsManager, 
			String name, 
			String column, 
			String visibleSetting, 
			boolean defaultVisible, 
			String widthSetting, 
			int defaultWidth, 
			Class<?> classReference, 
			TableCellRenderer renderer) {
		
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

	/**
	 * @return
	 * 		The name of the {@link Column}.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return
	 * 		The {@link String} identifier of this {@link Column}.
	 */
	public String getColumn() {
		return column;
	}
	
	/**
	 * @return
	 * 		The width of this {@link Column}.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * This method sets the width of this {@link Column}.
	 * 
	 * @param width
	 * 		The new width of this {@link Column}.
	 */
	public void setWidth(int width) {
		this.width = width;
		settingsManager.saveObject(widthSetting, width);
	}

	/**
	 * @return
	 * 		The {@link AbstractFormatter} which formats this {@link Column}.
	 */
	public AbstractFormatter getFormatter() {
		return formatter;
	}

	/**
	 * @return
	 * 		The {@link TableCellRenderer} which renders this {@link Column}.
	 */
	public TableCellRenderer getRenderer() {
		return renderer;
	}

	/**
	 * @return
	 * 		True if this {@link Column} is visible.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @return
	 * 		True if this {@link Column} has been removed.
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * This method sets this {@link Column} to be removed or not.
	 * 
	 * @param removed
	 * 		True if this {@link Column} should be removed or not.
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	/**
	 * This method sets the visibility.
	 * 
	 * @param visible
	 * 		True if this {@link Column} should be visible or false if it should not.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
		settingsManager.saveObject(visibleSetting, visible);
	}
	
	/**
	 * @return
	 * 		The {@link Class} type of the contained values.
	 */
	public Class<?> getValueClass() {
		return classReference;
	}

	/**
	 * @return
	 * 		The default with of this {@link Column}.
	 */
	public int getDefaultWidth() {
		return defaultWidth;
	}
}
