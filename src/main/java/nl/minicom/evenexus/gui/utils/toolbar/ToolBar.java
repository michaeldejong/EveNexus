package nl.minicom.evenexus.gui.utils.toolbar;


import java.awt.Color;
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
import nl.minicom.evenexus.gui.panels.dashboard.LineGraphEngine;
import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.ColumnSelectionFrame;
import nl.minicom.evenexus.gui.tables.datamodel.IPeriodFilter;
import nl.minicom.evenexus.gui.tables.datamodel.ITypeNameFilter;
import nl.minicom.evenexus.utils.SettingsManager;


public class ToolBar extends JPanel {

	private static final long serialVersionUID = 8807977497143764318L;
	
	private final SettingsManager settingsManager;

	public ToolBar(SettingsManager settingsManager) {
		super();
		
		this.settingsManager = settingsManager;
		
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 51));
		setMinimumSize(new Dimension(0, 51));
		setBackground(Color.WHITE);
	}

	public JPanel createTypeNameSearchField(final Table... tables) {
		JPanel filterPanel = new JPanel();
		filterPanel.setBackground(Color.WHITE);
		filterPanel.setMinimumSize(new Dimension(145, 51));
		filterPanel.setMaximumSize(new Dimension(145, 51));
		filterPanel.setLayout(null);
	
		JLabel itemName = new JLabel("Item name");
		itemName.setBounds(0, 2, 140, GuiConstants.TEXT_FIELD_HEIGHT);
		filterPanel.add(itemName);
		
		final JTextField itemField = new JTextField();
		itemField.setBounds(0, 21, 140, GuiConstants.TEXT_FIELD_HEIGHT);
		filterPanel.add(itemField);
		itemField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) { }
			
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
			public void keyPressed(KeyEvent arg0) { }
		});
		
		return filterPanel;
	}
	
	public JPanel createPeriodSelectionField(final Table table, final String selectedPeriodSetting) {
		JPanel filterPanel = new JPanel();
		filterPanel.setBackground(Color.WHITE);
		filterPanel.setMinimumSize(new Dimension(130, 51));
		filterPanel.setMaximumSize(new Dimension(130, 51));
		filterPanel.setLayout(null);
	
		JLabel itemName = new JLabel("Show only the last");
		itemName.setBounds(0, 2, 125, GuiConstants.COMBO_BOX_HEIGHT);
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
		dateField.setBounds(0, 21, 125, GuiConstants.COMBO_BOX_HEIGHT);
		
		for (int i = 0; i < dateValueList.size(); i++) {
			if (dateValueList.get(i).equals(settingsManager.loadInt(selectedPeriodSetting, 2))) {
				dateField.setSelectedIndex(i);
				continue;
			}
		}
		
		filterPanel.add(dateField);
		dateField.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (table.getDataModel() instanceof IPeriodFilter) {
					Integer value = dateValueList.get(dateField.getSelectedIndex());
					settingsManager.saveObject(selectedPeriodSetting, value);
					((IPeriodFilter) table.getDataModel()).setPeriod(value);
					table.reload();
				}
			}
		});
		
		return filterPanel;
	}
	
	public JPanel createPeriodSelectionField(final LineGraphEngine chartPanel, final SettingsManager settingsManager, final String selectedPeriodSetting) {
		JPanel filterPanel = new JPanel();
		filterPanel.setBackground(Color.WHITE);
		filterPanel.setMinimumSize(new Dimension(130, 51));
		filterPanel.setMaximumSize(new Dimension(130, 51));
		filterPanel.setLayout(null);
	
		JLabel itemName = new JLabel("Show only the last");
		itemName.setBounds(0, 2, 125, 23);
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
		dateField.setBounds(0, 21, 125, 23);
		
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

	public ToolBarButton createTableSelectColumnsButton(final ColumnSelectionFrame columnSelectionFrame) {
		ToolBarButton button = new ToolBarButton("add_column_32x32.png", "Show / hide columns");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				columnSelectionFrame.setVisible();
			}
		});
		return button;
	}
	
}
