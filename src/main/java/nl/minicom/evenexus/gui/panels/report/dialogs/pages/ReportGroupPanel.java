package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import nl.minicom.evenexus.core.report.definition.ReportDefinition;
import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.engine.Model;
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.validation.StateRule;
import nl.minicom.evenexus.gui.validation.ValidationListener;
import nl.minicom.evenexus.gui.validation.ValidationRule;

public class ReportGroupPanel extends JPanel {

	private static final long serialVersionUID = 4204408757202631909L;
	
	private final ReportDefinition definition;
	private final Model<ReportGroup> model;
	private final JComboBox comboBox;
	private final JCheckBox checkBox;
	
	public ReportGroupPanel(ReportDefinition definition, Model<ReportGroup> model) {
		this.definition = definition;
		this.model = model;

		this.comboBox = createGroupingComboBox();
		this.checkBox = createGroupingCheckBox(comboBox);
		
		doLayouting();
	}
	
	public void setEnabled(boolean enabled) {
		checkBox.setEnabled(enabled);
	}

	public boolean isEnabled() {
		return checkBox.isEnabled();
	}
	
	public void setSelected(boolean selected) {
		checkBox.setSelected(selected);
	}

	public boolean isSelected() {
		return checkBox.isSelected();
	}

	public Model<ReportGroup> getModel() {
		return model;
	}

	private void doLayouting() {			
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);        
		layout.setHorizontalGroup(
		      	layout.createSequentialGroup()
		      			.addComponent(checkBox)
		      			.addGap(4)
		        		.addComponent(comboBox)
    	);
    	layout.setVerticalGroup(
	    		layout.createSequentialGroup()
	    				.addGroup(layout.createParallelGroup()
	    						.addComponent(checkBox)
	    						.addComponent(comboBox)
	    				)
    	);
	}

	private JCheckBox createGroupingCheckBox(final JComboBox child) {
		
		StateRule childRule = new StateRule() {
			@Override
			public void onValid(boolean isValid) {
				child.setEnabled(isValid);
				if (isValid) {
					model.setValue((ReportGroup) child.getSelectedItem());
				}
				else {
					model.setValue(null);
				}
			}
		};
		
		final JCheckBox checkBox = new JCheckBox();
		checkBox.addChangeListener(new ValidationListener(
				new ValidationRule(childRule) {
					@Override
					public boolean isValid() {
						return checkBox.isSelected() && checkBox.isEnabled();
					}					
				}
		));
		
		childRule.checkValidationRules();
				
		return checkBox;
	}
	
	private JComboBox createGroupingComboBox() {
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		for (final ReportGroup group : definition.getGroups()) {
			comboBoxModel.addElement(group);
		}

		final JComboBox comboBox = new JComboBox();
		comboBox.setMinimumSize(new Dimension(0, GuiConstants.COMBO_BOX_HEIGHT));
		comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.COMBO_BOX_HEIGHT));
		comboBox.setModel(comboBoxModel);
		
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.setValue((ReportGroup) comboBox.getSelectedItem());
			}
		});
		
		return comboBox;
	}

}
