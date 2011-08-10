package nl.minicom.evenexus.gui.panels.report.dialogs;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.GroupLayout;

import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;
import nl.minicom.evenexus.utils.SettingsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReportPanel extends TabPanel {

	private static final long serialVersionUID = -4187071888216622511L;
	private static final Logger LOG = LoggerFactory.getLogger(ReportPanel.class);
	
	private final SettingsManager settingsManager;
	private final Provider<ReportBuilderDialog> dialogProvider;
	
	@Inject
	public ReportPanel(SettingsManager settingsManager, 
			Provider<ReportBuilderDialog> dialogProvider) {
		
		this.settingsManager = settingsManager;
		this.dialogProvider = dialogProvider;
	}
	
	public void initialize() {
		ToolBarButton createReport = new ToolBarButton("img/32/pie_chart.png", "Create a new report");
        createReport.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		dialogProvider.get().initialize();
        	}
        });
        
        ToolBar toolBar = new ToolBar(settingsManager);
        GroupLayout layout = new GroupLayout(toolBar);
        toolBar.setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
    		.addComponent(createReport)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(createReport)
			)
    	);
        
        layout = new GroupLayout(this);
        setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGap(7)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(toolBar))
				.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addGap(5)
	    		.addComponent(toolBar)
	    		.addGap(7)
    	);
	}

	@Override
	public void reloadTab() {
		LOG.debug("Report panel reloaded!");
	}
	
}
