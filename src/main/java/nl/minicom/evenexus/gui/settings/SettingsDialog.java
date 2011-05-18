package nl.minicom.evenexus.gui.settings;


import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import nl.minicom.evenexus.core.Application;
import nl.minicom.evenexus.gui.utils.dialogs.CustomDialog;
import nl.minicom.evenexus.gui.utils.dialogs.DialogTitle;

public class SettingsDialog extends CustomDialog {

	private static final long serialVersionUID = 8860569373371189189L;
	
	private final Application application;
	
	public SettingsDialog(Application application) {
		super(DialogTitle.SETTINGS, 420, 350);
		
		this.application = application;
		
		buildGui();
	}
	
	@Override
	public void createGui(JPanel guiPanel) {
		
		JTabbedPane pane = new JTabbedPane();
		pane.setFocusable(false);
		
		pane.addTab("Proxy", new ProxyTab(application.getProxyManager()));
		pane.addTab("API server", new ApiServerTab(application));
				
		GroupLayout layout = new GroupLayout(guiPanel);
		guiPanel.setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addComponent(pane)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
	    		.addComponent(pane)
    	);
	}
	
}
