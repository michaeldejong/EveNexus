package nl.minicom.evenexus.gui.panels.report.dialogs;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;

import nl.minicom.evenexus.core.Application;
import nl.minicom.evenexus.gui.panels.TabPanel;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBar;
import nl.minicom.evenexus.gui.utils.toolbar.ToolBarButton;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class ReportPanel extends TabPanel {

	private static final long serialVersionUID = -4187071888216622511L;
	private static final Logger logger = LogManager.getRootLogger();
	
	private ToolBarButton createReport;
	
	public ReportPanel(final Application application) {
		super();	
		
        createReport = new ToolBarButton("pie_chart_32x32.png", "Create a new report");
        createReport.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		new ReportBuilderDialog();
        	}
        });
        
        ToolBar toolBar = new ToolBar(application.getSettingsManager());
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
		logger.debug("Report panel reloaded!");
	}
}
