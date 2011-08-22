package nl.minicom.evenexus.gui.panels.accounts;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.minicom.evenexus.gui.panels.accounts.dialogs.AddCharacterFrame;
import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.TableColumnSelectionFrame;
import nl.minicom.evenexus.gui.tables.columns.models.AccountColumnModel;
import nl.minicom.evenexus.gui.tables.datamodel.implementations.AccountTableDataModel;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;
import nl.minicom.evenexus.utils.SettingsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AccountsPanel extends JPanel {

	private static final long serialVersionUID = -4187071888216622511L;
	private static final Logger LOG = LoggerFactory.getLogger(AccountsPanel.class);
	
	private final SettingsManager settingsManager;
	private final AccountTableDataModel accountData;
	private final AccountColumnModel accountModel;
	private final Table table;
	
	private final ToolBarButton addCharacter;
	private final ToolBarButton deleteCharacter;
	
	@Inject
	public AccountsPanel(SettingsManager settingsManager,
			AccountTableDataModel accountData,
			AccountColumnModel accountModel,
			final Table table,
			final Provider<AddCharacterFrame> addCharacterFrameProvider) {

		this.settingsManager = settingsManager;
		this.accountData = accountData;
		this.accountModel = accountModel;
		this.table = table;
		
		addCharacter = new ToolBarButton("img/32/add.png", "Add new a character");
		deleteCharacter = new ToolBarButton("img/32/remove.png", "Delete a character");
		
		addCharacter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addCharacterFrameProvider.get().initialize();
			}
		});
		
		deleteCharacter.setEnabled(false);
		deleteCharacter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int row : table.getSelectedRows()) {
					table.delete(row);
				}
				reloadTab();
			}
		});
	}

	public void initialize() {
		accountModel.initialize();
		table.initialize(accountData, accountModel);
		
		setBackground(Color.WHITE);	
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        ToolBar toolBar = new ToolBar(settingsManager);
        ToolBarButton columnSelectButton = toolBar.createTableSelectColumnsButton(new TableColumnSelectionFrame(accountModel, table));
        
        GroupLayout layout = new GroupLayout(toolBar);
        toolBar.setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
    		.addComponent(addCharacter)
    		.addGap(3)
    		.addComponent(deleteCharacter)
    		.addGap(3)
    		.addComponent(columnSelectButton)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(addCharacter)
				.addComponent(deleteCharacter)
				.addComponent(columnSelectButton)
			)
    	);
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (table.getSelectedRowCount() == 0) {
					deleteCharacter.setEnabled(false);
				}
				else {
					deleteCharacter.setEnabled(true);
				}
			}
		});
        
        layout = new GroupLayout(this);
        setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGap(7)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(scrollPane)
					.addComponent(toolBar))
				.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addGap(5)
	    		.addComponent(toolBar)
	    		.addGap(7)
	    		.addComponent(scrollPane)
	    		.addGap(7)
    	);
	}

	public void reloadTab() {
		table.getSelectionModel().clearSelection();
		deleteCharacter.setEnabled(false);
		table.reload();
		LOG.info("Account panel reloaded!");
	}
}
