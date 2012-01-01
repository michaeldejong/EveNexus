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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.minicom.evenexus.core.report.definition.ReportDefinition;
import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.engine.Model;
import nl.minicom.evenexus.core.report.engine.ModelListener;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.utils.dialogs.titles.DialogTitle;
import nl.minicom.evenexus.gui.utils.dialogs.titles.ReportGroupingTitle;
import nl.minicom.evenexus.i18n.Translator;

/**
 * The {@link ReportGroupingPage} allows the user to define how
 * to group the data he or she wishes to query.
 *
 * @author michael
 */
public class ReportGroupingPage extends ReportWizardPage {

	private static final long serialVersionUID = 3066113966844699181L;

	private final ReportModel model;
	private final ReportDefinition definition;
	private final Translator translator;

	/**
	 * Constructs an ew {@link ReportGroupingPage}.
	 * 		
	 * @param model
	 * 		The {@link ReportModel}.
	 * 
	 * @param definition
	 * 		The {@link ReportDefinition}.
	 * 
	 * @param translator
	 * 		The {@link Translator}.
	 */
	@Inject
	public ReportGroupingPage(ReportModel model, ReportDefinition definition, Translator translator) {
		this.model = model;
		this.definition = definition;
		this.translator = translator;
	}
	
	/**
	 * This method builds the gui allowing the user to select groupings.
	 */
	@Override
	public void buildGui() {
		JLabel group1Label = GuiConstants.createBoldLabel("Grouping 1");
		JLabel group2Label = GuiConstants.createBoldLabel("Grouping 2");
		JLabel group3Label = GuiConstants.createBoldLabel("Grouping 3");
		
		final ReportGroupPanel group1Panel = new ReportGroupPanel().buildGui(model.getGrouping1());
		final ReportGroupPanel group2Panel = new ReportGroupPanel().buildGui(model.getGrouping2());
		final ReportGroupPanel group3Panel = new ReportGroupPanel().buildGui(model.getGrouping3());

		addReportGroupPanelLogic(group1Panel, group2Panel, model.getGrouping1(), model.getGrouping2());
		addReportGroupPanelLogic(group2Panel, group3Panel, model.getGrouping2(), model.getGrouping3());
		
		group1Panel.setEnabled(true);
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);        
		layout.setHorizontalGroup(
		      	layout.createSequentialGroup()
		      			.addGroup(layout.createParallelGroup()
		        				.addComponent(group1Label)
		        				.addComponent(group1Panel)
		        				.addComponent(group2Label)
		        				.addComponent(group2Panel)
		        				.addComponent(group3Label)
		        				.addComponent(group3Panel)
		        		)
    	);
    	layout.setVerticalGroup(
	    		layout.createSequentialGroup()
	    				.addComponent(group1Label)
	    				.addGap(4)
	    				.addComponent(group1Panel)
	    				.addGap(4)
        				.addComponent(group2Label)
        				.addGap(4)
        				.addComponent(group2Panel)
        				.addGap(4)
        				.addComponent(group3Label)
        				.addGap(4)
        				.addComponent(group3Panel)
    	);
	}
	
	private void addReportGroupPanelLogic(final ReportGroupPanel parent, final ReportGroupPanel child, 
			final Model<ReportGroup> parentModel, final Model<ReportGroup> childModel) {
		
		parentModel.addListener(new ModelListener() {
			@Override
			public void onValueChanged() {
				// Do nothing.
			}

			@Override
			public void onStateChanged() {
				if (parentModel.isEnabled()) {
					child.setEnabled(true);
				}
				else {
					child.setEnabled(false);
					childModel.disable();
				}
			}
		});
	}

	@Override
	public DialogTitle getTitle() {
		return new ReportGroupingTitle();
	}
	
	/**
	 * @return
	 *		This method returns true if the user is allowed to go to the next page.
	 */
	public boolean allowNext() {
		return !model.getReportGroups().isEmpty();
	}
	
	/**
	 * The {@link ReportGroupingPage} allows the user to define how to group the 
	 * results in the report.
	 *
	 * @author michael
	 */
	public class ReportGroupPanel extends JPanel {

		private static final long serialVersionUID = 4204408757202631909L;
		
		private Model<ReportGroup> model;
		private Map<String, ReportGroup> groupMapping;
		private JComboBox comboBox;
		private JCheckBox checkBox;

		/**
		 * This method builds the gui for a specific {@link Model} of a {@link ReportGroup}.
		 * 
		 * @param model
		 * 		The {@link Model} to read from, and write changes to.
		 * 
		 * @return
		 * 		this.
		 */
		public ReportGroupPanel buildGui(Model<ReportGroup> model) {
			this.model = model;
			this.groupMapping = createGroupMapping();
			this.comboBox = createGroupingComboBox();
			this.checkBox = createGroupingCheckBox(comboBox);
			doLayouting();
			return this;
		}
		
		/**
		 * This method enables this {@link ReportGroupingPage}.
		 * 
		 * @param enabled
		 * 		True if the panel needs to be enabled.
		 */
		public void setEnabled(boolean enabled) {
			checkBox.setEnabled(enabled);
			if (enabled) {
				model.enable();
			}
			else {
				model.disable();
			}
		}

		/**
		 * @return
		 * 		The {@link Model} on which this panel is based.
		 */
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
			model.addListener(new ModelListener() {
				@Override
				public void onValueChanged() {
					// Do nothing.
				}

				@Override
				public void onStateChanged() {
					if (model.isEnabled()) {
						String selectedName = child.getSelectedItem().toString();
						ReportGroup selectedGroup = groupMapping.get(selectedName);
						model.setValue(selectedGroup);
					}
					else {
						model.setValue(null);
					}
				}
			});
			
			final JCheckBox checkBox = new JCheckBox();
			checkBox.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					if (checkBox.isEnabled() && checkBox.isSelected()) {
						model.enable();
					}
					else {
						model.disable();
					}
				}
			});
			
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
}
