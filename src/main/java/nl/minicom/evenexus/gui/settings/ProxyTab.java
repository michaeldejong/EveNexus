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

import nl.minicom.evenexus.gui.validation.StateRule;
import nl.minicom.evenexus.gui.validation.ValidationListener;
import nl.minicom.evenexus.gui.validation.ValidationRule;
import nl.minicom.evenexus.utils.ProxyManager;


public class ProxyTab extends JPanel {
	
	private static final long serialVersionUID = 5594974133127416959L;
	
	private final ProxyManager proxyManager;
	
	@Inject
	public ProxyTab(final ProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}
	
	public void initialize() {
		setBackground(Color.WHITE);
		final JCheckBox proxyEnabledBox = new JCheckBox("Enable proxy settings");
		proxyEnabledBox.setBackground(Color.WHITE);
		
		final JLabel proxyServerLabel = new JLabel("Proxy server");
		final JLabel proxyPortLabel = new JLabel("Proxy port");
		final JTextField proxyServerField = new JTextField();
		final JTextField proxyPortField = new JTextField();
		final JButton apply = new JButton("Apply");
		
		Object proxyEnabledSetting = System.getProperties().get("proxySet");
		if (proxyEnabledSetting != null) {
			proxyEnabledBox.setSelected(Boolean.parseBoolean(proxyEnabledSetting.toString()));
		}
		
		Object proxyServerSetting = System.getProperties().get("proxyHost");
		if (proxyServerSetting != null) {
			proxyServerField.setText(proxyServerSetting.toString());
		}
		
		Object proxyPortSetting = System.getProperties().get("proxyPort");
		if (proxyPortSetting != null) {
			proxyPortField.setText(proxyPortSetting.toString());
		}
		
		proxyServerLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		proxyServerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		proxyPortLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		proxyPortField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
		apply.setMaximumSize(new Dimension(60, 32));
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);        
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
    		.addGap(7)
    		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
    			.addComponent(proxyEnabledBox)
				.addComponent(proxyServerLabel)
				.addComponent(proxyServerField)
				.addComponent(proxyPortLabel)
				.addComponent(proxyPortField)
				.addComponent(apply)
    		)
    		.addGap(7)
    	);
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGap(6)
    		.addComponent(proxyEnabledBox)
    		.addGap(7)
    		.addComponent(proxyServerLabel)
			.addComponent(proxyServerField)
			.addGap(7)
			.addComponent(proxyPortLabel)
			.addComponent(proxyPortField)
			.addGap(7)
			.addComponent(apply)
    		.addGap(7)
    	);
    	
    	final ValidationRule proxyEnabledRule = new ValidationRule() {			
			@Override
			public boolean isValid() {
				return proxyEnabledBox.isSelected();
			}
		};
		
		final ValidationRule proxyHostRule = new ValidationRule() {			
			@Override
			public boolean isValid() {
				return ValidationRule.isNotEmpty(proxyServerField.getText());
			}
		};
		
		final ValidationRule proxyPortRule = new ValidationRule() {			
			@Override
			public boolean isValid() {
				return 	ValidationRule.isNotEmpty(proxyPortField.getText()) && 
						ValidationRule.isBoundDigit(proxyPortField.getText(), 1, 65535);
			}
		};
		
		new StateRule(proxyEnabledRule) {		
			@Override
			public void onValid(boolean isValid) {
				proxyServerField.setEnabled(isValid);
				proxyPortField.setEnabled(isValid);
			}
		};
		
		new StateRule(proxyEnabledRule, proxyHostRule, proxyPortRule) {
			@Override
			public void checkValidationRules() {
				if (proxyEnabledRule.isValid() && proxyHostRule.isValid() && proxyPortRule.isValid()) {
					onValid(true);
				}
				else if (!proxyEnabledRule.isValid()) {
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
		
		proxyEnabledRule.trigger();
		
		proxyEnabledBox.addActionListener(new ValidationListener(proxyEnabledRule));
		proxyServerField.addKeyListener(new ValidationListener(proxyHostRule));
		proxyPortField.addKeyListener(new ValidationListener(proxyPortRule));
    	
    	apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (proxyEnabledBox.isSelected()) {
					String proxyServer = proxyServerField.getText();
					int proxyPort = Integer.parseInt(proxyPortField.getText());
					proxyManager.setProxyHostAndPort(proxyServer, proxyPort);
				}
				else {
					proxyManager.disableProxy();
				}
			}
		});
    	
	}
}
