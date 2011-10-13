package nl.minicom.evenexus.gui.settings;


import java.awt.Color;
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
import nl.minicom.evenexus.gui.validation.StateRule;
import nl.minicom.evenexus.gui.validation.ValidationListener;
import nl.minicom.evenexus.gui.validation.ValidationRule;
import nl.minicom.evenexus.utils.SettingsManager;

public class ApiServerTab extends JPanel {
	
	private static final long serialVersionUID = 5594974133127416959L;

	private final SettingsManager settingsManager;
	private final ApiServerManager apiServerManager;
	
	@Inject
	public ApiServerTab(SettingsManager settingsManager, 
			final ApiServerManager apiServerManager) {
		
		this.settingsManager = settingsManager;
		this.apiServerManager = apiServerManager;
	}
	
	public void initialize() {
		setBackground(Color.WHITE);
		final JCheckBox userServerCheckBox = new JCheckBox("Use user-defined API server");
		userServerCheckBox.setBackground(Color.WHITE);
		
		final JLabel apiServerLabel = new JLabel("API Server");
		final JTextField apiServerField = new JTextField();
		final JButton apply = new JButton("Apply");
		
		userServerCheckBox.setSelected(settingsManager.loadBoolean(SettingsManager.USER_DEFINED_API_SERVER_ENABLED, false));
		apiServerField.setText(settingsManager.loadString(SettingsManager.USER_DEFINED_API_SERVER_HOST, ApiServerManager.DEFAULT_API_SERVER));
		
		apiServerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		apiServerLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		apply.setMaximumSize(new Dimension(60, 32));
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
    		.addGap(7)
    		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
    			.addComponent(userServerCheckBox)
				.addComponent(apiServerField)
				.addComponent(apiServerLabel)
				.addComponent(apply)
    		)
    		.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGap(6)
			.addComponent(userServerCheckBox)
    		.addGap(7)
    		.addComponent(apiServerLabel)
			.addComponent(apiServerField)
			.addGap(7)
			.addComponent(apply)
    		.addGap(7)
    	);
    	
    	final ValidationRule userDefinedApiServerEnabledRule = new ValidationRule() {			
			@Override
			public boolean isValid() {
				return userServerCheckBox.isSelected();
			}
		};
		
		final ValidationRule userDefinedApiServerHostRule = new ValidationRule() {			
			@Override
			public boolean isValid() {
				return ValidationRule.isNotEmpty(apiServerField.getText());
			}
		};
		
		new StateRule(userDefinedApiServerEnabledRule) {		
			@Override
			public void onValid(boolean isValid) {
				apiServerField.setEnabled(isValid);
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
		
		userServerCheckBox.addActionListener(new ValidationListener(userDefinedApiServerEnabledRule));
		apiServerField.addKeyListener(new ValidationListener(userDefinedApiServerHostRule));
		
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (userServerCheckBox.isSelected()) {
					String apiServer = apiServerField.getText();
					apiServerManager.setApiServer(apiServer);
				}
				else {
					apiServerManager.disableUserApiServer();
				}
			}
		});
    	
	}
}
