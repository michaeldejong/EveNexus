package nl.minicom.evenexus.gui.panels.journals;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nl.minicom.evenexus.core.Application;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.importers.ImportListener;
import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.tables.Table;
import nl.minicom.evenexus.gui.tables.columns.ColumnModel;
import nl.minicom.evenexus.gui.tables.columns.TableColumnSelectionFrame;
import nl.minicom.evenexus.gui.tables.columns.models.JournalColumnModel;
import nl.minicom.evenexus.gui.tables.datamodel.implementations.JournalTableDataModel;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;
import nl.minicom.evenexus.utils.SettingsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JournalsPanel extends TabPanel implements ImportListener {

	private static final long serialVersionUID = -4187071888216622511L;
	private static final Logger LOG = LoggerFactory.getLogger(JournalsPanel.class);
	
	private Table table;
	private final ColumnModel columnModel;
	private final Application application;
	
	public JournalsPanel(Application application) {
		super();
		
		this.application = application;
		this.columnModel = new JournalColumnModel(application.getSettingsManager());
		this.table = new Table(new JournalTableDataModel(application.getSettingsManager()), columnModel);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		JPanel toolBar = createTopMenu();
        
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGap(7)
        		.addGroup(layout.createParallelGroup(Alignment.LEADING)
       				.addComponent(scrollPane)
       				.addComponent(toolBar)
        		)
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
    	
    	application.getImportManager().addListener(Api.CHAR_WALLET_JOURNAL, this);
	}
	
	@Override
	public void onImportComplete() {
		reloadTab();
	}

	@Override
	public void reloadTab() {
		table.reload();
		LOG.debug("Journal panel reloaded!");
	}
	
	private JPanel createTopMenu() {		
		ToolBar toolBar = new ToolBar(application.getSettingsManager());
		toolBar.setBackground(Color.WHITE);
		
		JPanel periodSelectionField = toolBar.createPeriodSelectionField(table, SettingsManager.FILTER_JOURNAL_PERIOD);
		final ToolBarButton button = toolBar.createTableSelectColumnsButton(new TableColumnSelectionFrame(columnModel, table));
		
		button.setContentAreaFilled(false);
		button.setRolloverEnabled(false);
		
		button.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				button.setContentAreaFilled(false);
				button.setRolloverEnabled(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				button.setContentAreaFilled(true);
				button.setRolloverEnabled(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
        GroupLayout layout = new GroupLayout(toolBar);
        toolBar.setLayout(layout);
        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        	.addComponent(periodSelectionField)
    		.addGap(7)
    		.addComponent(button)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(periodSelectionField)
					.addComponent(button)
		        )
        	)
    	);
		
		return toolBar;
	}

}
