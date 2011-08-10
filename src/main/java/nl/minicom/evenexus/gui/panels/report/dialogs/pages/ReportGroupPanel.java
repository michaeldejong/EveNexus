package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
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
import nl.minicom.evenexus.i18n.Translator;

public class ReportGroupPanel extends JPanel {

	private static final long serialVersionUID = 4204408757202631909L;
	
	private final ReportDefinition definition;
	private final Translator translator;
	
	private Model<ReportGroup> model;
	private Map<String, ReportGroup> groupMapping;
	private JComboBox comboBox;
	private JCheckBox checkBox;
	
	@Inject
	public ReportGroupPanel(ReportDefinition definition, Translator translator) {
		this.definition = definition;
		this.translator = translator;
	}
	
	public ReportGroupPanel initialize(Model<ReportGroup> model) {
		this.model = model;
		this.groupMapping = createGroupMapping();
		this.comboBox = createGroupingComboBox();
		this.checkBox = createGroupingCheckBox(comboBox);
		doLayouting();
		
		return this;
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
					String selectedName = child.getSelectedItem().toString();
					ReportGroup selectedGroup = groupMapping.get(selectedName);
					model.setValue(selectedGroup);
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
	
	private Map<String, ReportGroup> createGroupMapping() {
		final Map<String, ReportGroup> choices = new LinkedHashMap<String, ReportGroup>();
		for (ReportGroup group : definition.getGroups()) {
			String groupingName = translator.translate(group.getKey());
			choices.put(groupingName, group);
		}
		return choices;
	}
	
	private JComboBox createGroupingComboBox() {
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		for (String name : groupMapping.keySet()) {
			comboBoxModel.addElement(name);
		}

		final JComboBox comboBox = new JComboBox();
		comboBox.setMinimumSize(new Dimension(0, GuiConstants.COMBO_BOX_HEIGHT));
		comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.COMBO_BOX_HEIGHT));
		comboBox.setModel(comboBoxModel);
		
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = comboBox.getSelectedItem().toString();
				ReportGroup reportGroup = groupMapping.get(name);
				
				model.setValue(reportGroup);
			}
		});
		
		return comboBox;
	}

}
