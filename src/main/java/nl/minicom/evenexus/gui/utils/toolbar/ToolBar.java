package nl.minicom.evenexus.gui.utils.toolbar;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.icons.Icon;
import nl.minicom.evenexus.gui.panels.dashboard.LineGraphEngine;
import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.ColumnSelectionFrame;
import nl.minicom.evenexus.gui.tables.datamodel.IPeriodFilter;
import nl.minicom.evenexus.gui.tables.datamodel.ITypeNameFilter;
import nl.minicom.evenexus.utils.SettingsManager;


/**
 * This class is responsible for everything related to the toolbar at the top of each tab.
 * 
 * @author michael
 */
public class ToolBar extends JPanel {

	private static final long serialVersionUID = 8807977497143764318L;
	
	private final SettingsManager settingsManager;

	/**
	 * This contructs a new {@link ToolBar} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 */
	public ToolBar(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
		
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 51));
		setMinimumSize(new Dimension(0, 51));
		setBackground(GuiConstants.getTabBackground());
	}

	/**
	 * This method creates a {@link JTextField} allowing the user to filter on a specific item name.
	 * 
	 * @param tables
	 * 		The table to filter.
	 * 
	 * @return
	 * 		A {@link JPanel} containing the {@link JTextField}.
	 */
	public JPanel createTypeNameSearchField(final Table... tables) {
		JPanel filterPanel = new JPanel();
		filterPanel.setBackground(GuiConstants.getTabBackground());
		filterPanel.setMinimumSize(new Dimension(256, 51));
		filterPanel.setMaximumSize(new Dimension(256, 51));
		filterPanel.setLayout(null);
	
		JLabel itemName = new JLabel("Item name");
		itemName.setBounds(0, 2, 251, GuiConstants.TEXT_FIELD_HEIGHT);
		filterPanel.add(itemName);
		
		final JTextField itemField = new JTextField();
		itemField.setBounds(0, 21, 251, GuiConstants.TEXT_FIELD_HEIGHT);
		filterPanel.add(itemField);
		itemField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				// Do nothing.
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				for (Table table : tables) {
					if (table.getDataModel() instanceof ITypeNameFilter) {
						((ITypeNameFilter) table.getDataModel()).setTypeName(itemField.getText());
						table.reload();
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// Do nothing.
			}
		});
		
		return filterPanel;
	}
	
	/**
	 * This method creates a new combobox which filters the specified table.
	 * 
	 * @param table
	 * 		The table to filter.
	 * 
	 * @param selectedPeriodSetting
	 * 		The setting describing the table's period filter.
	 * 
	 * @return
	 * 		A {@link JPanel} containing the constructed combobox.
	 */
	public JPanel createPeriodField(final Table table, final String selectedPeriodSetting) {
		JPanel filterPanel = new JPanel();
		filterPanel.setBackground(GuiConstants.getTabBackground());
		filterPanel.setMinimumSize(new Dimension(206, 51));
		filterPanel.setMaximumSize(new Dimension(206, 51));
		filterPanel.setLayout(null);
	
		JLabel itemName = new JLabel("Show only the last");
		itemName.setBounds(0, 2, 201, GuiConstants.COMBO_BOX_HEIGHT);
		filterPanel.add(itemName);
		
		Vector<String> dateList = new Vector<String>();
		dateList.add("24 hours");
		dateList.add(" 7 days");
		dateList.add("14 days");
		dateList.add("28 days");
		
		final Vector<Integer> dateValueList = new Vector<Integer>();
		dateValueList.add(IPeriodFilter.DAY);
		dateValueList.add(IPeriodFilter.WEEK);
		dateValueList.add(IPeriodFilter.TWO_WEEKS);
		dateValueList.add(IPeriodFilter.FOUR_WEEKS);
		
		final JComboBox dateField = new JComboBox(dateList);
		dateField.setBounds(0, 21, 201, GuiConstants.COMBO_BOX_HEIGHT);
		
		int days = settingsManager.loadInt(selectedPeriodSetting, 14);
		((IPeriodFilter) table.getDataModel()).setPeriod(days);

		for (int i = 0; i < dateValueList.size(); i++) {
			if (dateValueList.get(i).equals(days)) {
				dateField.setSelectedIndex(i);
				continue;
			}
		}
		
		filterPanel.add(dateField);
		dateField.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (table.getDataModel() instanceof IPeriodFilter) {
					Integer days = dateValueList.get(dateField.getSelectedIndex());
					settingsManager.saveObject(selectedPeriodSetting, days);
					((IPeriodFilter) table.getDataModel()).setPeriod(days);
					table.reload();
				}
			}
		});
		
		return filterPanel;
	}
	
	/**
	 * @return
	 * 		A new {@link JPanel} object which acts as a spacer.
	 */
	public final JPanel createSpacer() {
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(0, 51));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 51));
		panel.setBackground(GuiConstants.getTabBackground());
		return panel;
	}
	
	/**
	 * This method creates a combobox allowing the user to define a period of interest in the {@link LineGraphEngine}.
	 * 
	 * @param chartPanel
	 * 		The {@link LineGraphEngine} to filter.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 * 
	 * @param selectedPeriodSetting
	 * 		The setting name regarding period selection of the supplied chartPanel.
	 * 
	 * @return
	 * 		A {@link JPanel} containing the combobox.
	 */
	public final JPanel createPeriodSelectionField(final LineGraphEngine chartPanel, 
			final SettingsManager settingsManager, final String selectedPeriodSetting) {
		
		JPanel filterPanel = new JPanel();
		filterPanel.setBackground(GuiConstants.getTabBackground());
		filterPanel.setMinimumSize(new Dimension(206, 51));
		filterPanel.setMaximumSize(new Dimension(206, 51));
		filterPanel.setLayout(null);
	
		JLabel itemName = new JLabel("Show only the last");
		itemName.setBounds(0, 2, 201, 23);
		filterPanel.add(itemName);
		
		Vector<String> dateList = new Vector<String>();
		dateList.add("1 week");
		dateList.add("2 weeks");
		dateList.add("4 weeks");
		dateList.add("8 weeks");
		
		final Vector<Integer> dateValueList = new Vector<Integer>();
		dateValueList.add(IPeriodFilter.WEEK);
		dateValueList.add(IPeriodFilter.TWO_WEEKS);
		dateValueList.add(IPeriodFilter.FOUR_WEEKS);
		dateValueList.add(IPeriodFilter.EIGHT_WEEKS);
		
		final JComboBox dateField = new JComboBox(dateList);
		dateField.setBounds(0, 21, 201, 23);
		
		for (int i = 0; i < dateValueList.size(); i++) {
			if (dateValueList.get(i).equals(settingsManager.loadInt(selectedPeriodSetting, 14))) {
				dateField.setSelectedIndex(i);
				continue;
			}
		}
		
		filterPanel.add(dateField);
		dateField.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				Integer value = dateValueList.get(dateField.getSelectedIndex());
				settingsManager.saveObject(selectedPeriodSetting, value);
				chartPanel.setPeriod(value);
				chartPanel.reload();
			}
		});
		
		return filterPanel;
	}

	/**
	 * This method creates a new {@link ToolBarButton} which allows the user to filter columns on a table.
	 * 
	 * @param columnSelectionFrame
	 * 		The {@link ColumnSelectionFrame} to display.
	 * 
	 * @return
	 * 		A {@link ToolBarButton} which opens the specified {@link ColumnSelectionFrame}.
	 */
	public final ToolBarButton createTableSelectColumnsButton(final ColumnSelectionFrame columnSelectionFrame) {
		ToolBarButton button = new ToolBarButton(Icon.ADD_COLUMN_32, "Show / hide columns");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				columnSelectionFrame.setVisible();
			}
		});
		return button;
	}
	
}
