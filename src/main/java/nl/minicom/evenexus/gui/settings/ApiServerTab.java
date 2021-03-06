package nl.minicom.evenexus.gui.settings;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nl.minicom.evenexus.eveapi.ApiServerManager;
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.validation.StateRule;
import nl.minicom.evenexus.gui.validation.ValidationListener;
import nl.minicom.evenexus.gui.validation.ValidationRule;
import nl.minicom.evenexus.utils.SettingsManager;

/**
 * This tab allows the user to specify if he/she wishes to use a custom api server.
 *
 * @author michael
 */
public class ApiServerTab extends JPanel {
	
	private static final long serialVersionUID = 5594974133127416959L;

	private static final String CUSTOM_SERVER_ENABLED = SettingsManager.USER_DEFINED_API_SERVER_ENABLED;
	private static final String CUSTOM_SERVER_HOST = SettingsManager.USER_DEFINED_API_SERVER_HOST;
	
	private final SettingsManager settingsManager;
	private final ApiServerManager apiServerManager;
	
	/**
	 * This constructs a new {@link ApiServerTab} object.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 * 
	 * @param apiServerManager
	 * 		The {@link ApiServerManager}.
	 */
	@Inject
	public ApiServerTab(SettingsManager settingsManager, 
			final ApiServerManager apiServerManager) {
		
		this.settingsManager = settingsManager;
		this.apiServerManager = apiServerManager;
	}
	
	/**
	 * This method initializes this {@link ApiServerTab}.
	 */
	public void initialize() {
		setBackground(GuiConstants.getTabBackground());
		final JCheckBox checkBox = new JCheckBox("Use user-defined API server");
		checkBox.setBackground(GuiConstants.getTabBackground());
		
		final JLabel apiServerLabel = new JLabel("API Server");
		final JTextField server = new JTextField();
		final JButton apply = new JButton("Apply");
		
		checkBox.setSelected(settingsManager.loadBoolean(CUSTOM_SERVER_ENABLED, false));
		server.setText(settingsManager.loadString(CUSTOM_SERVER_HOST, ApiServerManager.DEFAULT_API_SERVER));
		
		server.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		apiServerLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		apply.setMaximumSize(new Dimension(60, 32));
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
    		.addGap(7)
    		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
    			.addComponent(checkBox)
				.addComponent(server)
				.addComponent(apiServerLabel)
				.addComponent(apply)
    		)
    		.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGap(6)
			.addComponent(checkBox)
    		.addGap(7)
    		.addComponent(apiServerLabel)
			.addComponent(server)
			.addGap(7)
			.addComponent(apply)
    		.addGap(7)
    	);
    	
    	final ValidationRule userDefinedApiServerEnabledRule = new ValidationRule() {			
			@Override
			public boolean isValid() {
				return checkBox.isSelected();
			}
		};
		
		final ValidationRule userDefinedApiServerHostRule = new ValidationRule() {			
			@Override
			public boolean isValid() {
				return ValidationRule.isNotEmpty(server.getText());
			}
		};
		
		new StateRule(userDefinedApiServerEnabledRule) {		
			@Override
			public void onValid(boolean isValid) {
				server.setEnabled(isValid);
			}
		};
		
		new StateRule(userDefinedApiServerEnabledRule, userDefinedApiServerHostRule) {
			@Override
			public void checkValidationRules() {
				if (userDefinedApiServerEnabledRule.isValid() && userDefinedApiServerHostRule.isValid()) {
					onValid(true);
				}
				else if (!userDefinedApiServerEnabledRule.isValid()) {
					onValid(true);
				}
				else {
					onValid(false);
				}
			}
			
			@Override
			public void onValid(boolean isValid) {
				apply.setEnabled(isValid);
			}
		};
		
		userDefinedApiServerEnabledRule.trigger();
		
		checkBox.addActionListener(new ValidationListener(userDefinedApiServerEnabledRule));
		server.addKeyListener(new ValidationListener(userDefinedApiServerHostRule));
		
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (checkBox.isSelected()) {
					String apiServer = server.getText();
					apiServerManager.setApiServer(apiServer);
				}
				else {
					apiServerManager.disableUserApiServer();
				}
			}
		});
    	
	}
}
