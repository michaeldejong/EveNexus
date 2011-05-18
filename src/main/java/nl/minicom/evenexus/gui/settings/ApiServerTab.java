package nl.minicom.evenexus.gui.settings;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nl.minicom.evenexus.core.Application;
import nl.minicom.evenexus.eveapi.ApiServerManager;
import nl.minicom.evenexus.gui.validation.StateRule;
import nl.minicom.evenexus.gui.validation.ValidationListener;
import nl.minicom.evenexus.gui.validation.ValidationRule;
import nl.minicom.evenexus.utils.SettingsManager;

public class ApiServerTab extends JPanel {
	
	private static final long serialVersionUID = 5594974133127416959L;

	public ApiServerTab(final Application application) {		
		
		setBackground(Color.WHITE);
		final JCheckBox userDefinedUserApiServer = new JCheckBox("Use user-defined API server");
		userDefinedUserApiServer.setBackground(Color.WHITE);
		
		final JLabel apiServerLabel = new JLabel("API Server");
		final JTextField apiServerField = new JTextField();
		final JButton apply = new JButton("Apply");
		
		Object userDefinedUserApiServerSetting = application.getSettingsManager().loadBoolean(SettingsManager.USER_DEFINED_API_SERVER_ENABLED, false);
		if (userDefinedUserApiServerSetting != null) {
			userDefinedUserApiServer.setSelected(Boolean.parseBoolean(userDefinedUserApiServerSetting.toString()));
		}
		
		Object proxyServerSetting = application.getSettingsManager().loadString(SettingsManager.USER_DEFINED_API_SERVER_HOST, ApiServerManager.DEFAULT_API_SERVER);
		if (proxyServerSetting != null) {
			apiServerField.setText(proxyServerSetting.toString());
		}		
		
		apiServerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		apiServerLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		apply.setMaximumSize(new Dimension(60, 32));
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
    		.addGap(7)
    		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
    			.addComponent(userDefinedUserApiServer)
				.addComponent(apiServerField)
				.addComponent(apiServerLabel)
				.addComponent(apply)
    		)
    		.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGap(6)
			.addComponent(userDefinedUserApiServer)
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
				return userDefinedUserApiServer.isSelected();
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
		
		userDefinedUserApiServer.addActionListener(new ValidationListener(userDefinedApiServerEnabledRule));
		apiServerField.addKeyListener(new ValidationListener(userDefinedApiServerHostRule));
		
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (userDefinedUserApiServer.isSelected()) {
					String apiServer = apiServerField.getText();
					application.getApiServerManager().setApiServer(apiServer);
				}
				else {
					application.getApiServerManager().disableUserApiServer();
				}
			}
		});
    	
	}
}
