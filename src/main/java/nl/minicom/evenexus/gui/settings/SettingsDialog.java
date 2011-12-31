package nl.minicom.evenexus.gui.settings;


import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import nl.minicom.evenexus.gui.utils.dialogs.CustomDialog;
import nl.minicom.evenexus.gui.utils.dialogs.titles.SettingsTitle;

/**
 * This dialog contains several tabs, where the user can change the settings of EveNexus.
 * 
 * @author michael
 */
public class SettingsDialog extends CustomDialog {

	private static final long serialVersionUID = 8860569373371189189L;
	
	private final ProxyTab proxyTab;
	private final ApiServerTab apiServerTab;
	
	/**
	 * Constructs a new {@link SettingsDialog} object.
	 * 
	 * @param proxyTab
	 * 		The {@link ProxyTab}.
	 * 
	 * @param apiServerTab
	 * 		The {@link ApiServerTab}.
	 */
	@Inject
	public SettingsDialog(ProxyTab proxyTab,
			ApiServerTab apiServerTab) {
		
		super(new SettingsTitle(), 420, 350);
		this.proxyTab = proxyTab;
		this.apiServerTab = apiServerTab;
	}
	
	/**
	 * This method initializes the {@link SettingsDialog}.
	 */
	public void initialize() {
		proxyTab.initialize();
		apiServerTab.initialize();
		buildGui();
		setVisible(true);
	}
	
	@Override
	public void createGui(JPanel guiPanel) {
		JTabbedPane pane = new JTabbedPane();
		pane.setFocusable(false);
		
		pane.addTab("Proxy", proxyTab);
		pane.addTab("API server", apiServerTab);
				
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
