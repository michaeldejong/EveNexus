package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.minicom.evenexus.core.report.engine.Model;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.utils.dialogs.titles.DialogTitle;
import nl.minicom.evenexus.gui.utils.dialogs.titles.ReportFilteringTitle;
import nl.minicom.evenexus.gui.validation.StateRule;
import nl.minicom.evenexus.gui.validation.ValidationListener;
import nl.minicom.evenexus.gui.validation.ValidationRule;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.MapRegion;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * This class represents the filter dialog when creating a report.
 * 
 * @author michael
 */
public class ReportFiltersPage extends ReportWizardPage {

	private static final long serialVersionUID = 3066113966844699181L;

	private final Database database;
	private final ReportModel model;
	
	/**
	 * This constructs a new {@link ReportFiltersPage} object.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 * 
	 * @param model
	 * 		The {@link ReportModel}.
	 */
	@Inject
	public ReportFiltersPage(Database database, ReportModel model) {
		super(model);
		this.database = database;
		this.model = model;
	}

	/**
	 * This method builds the gui, allowing the user to define the filters.
	 */
	@Override
	public void buildGui() {
		JLabel timeLabel = GuiConstants.createBoldLabel("Time filters");
		JLabel locationLabel = GuiConstants.createBoldLabel("Location filters");
		JLabel characterLabel = GuiConstants.createBoldLabel("Character filters");
		
		JSpinner fromDateSpinner = createDateSpinner(model.getStartDate());
		JCheckBox fromBox = createCheckBox("From: ", fromDateSpinner);

		JSpinner toSpinner = createDateSpinner(model.getEndDate());
		JCheckBox toBox = createCheckBox("To: ", toSpinner);
		
		JComboBox regionComboBox = createRegionComboBox();
		JCheckBox regionBox = createCheckBox("Region: ", regionComboBox);
		
		JComboBox characterComboBox = createCharacterComboBox();
		JCheckBox characterBox = createCheckBox("Character: ", characterComboBox);
		if (characterComboBox.getModel().getSize() == 0) {
			characterBox.setEnabled(false);
			characterComboBox.setEnabled(false);
		}
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);        
		layout.setHorizontalGroup(
		      	layout.createSequentialGroup()
		      			.addGroup(layout.createParallelGroup()
		      					.addComponent(timeLabel)
		      					.addComponent(fromBox)
		      					.addComponent(toBox)
		      					.addComponent(locationLabel)
		      					.addComponent(regionBox)
		      					.addComponent(characterLabel)
		      					.addComponent(characterBox)
		      			)
		      			.addGroup(layout.createParallelGroup()
		      					.addComponent(fromDateSpinner)
		      					.addComponent(toSpinner)
		      					.addComponent(regionComboBox)
		      					.addComponent(characterComboBox)
		      			)
    	);
    	layout.setVerticalGroup(
	    		layout.createSequentialGroup()
						.addComponent(timeLabel)
						.addGap(4)
						.addGroup(layout.createParallelGroup()
			      					.addComponent(fromBox)
			      					.addComponent(fromDateSpinner)
						)
	    				.addGap(4)
	    				.addGroup(layout.createParallelGroup()
   		      					.addComponent(toBox)
   		      					.addComponent(toSpinner)
	    				)
	    				.addGap(4)
						.addComponent(locationLabel)
						.addGap(4)
	    				.addGroup(layout.createParallelGroup()
	    						.addComponent(regionBox)
	    						.addComponent(regionComboBox)
	    				)
	    				.addGap(4)
						.addComponent(characterLabel)
						.addGap(4)
	    				.addGroup(layout.createParallelGroup()
	    						.addComponent(characterBox)
	    						.addComponent(characterComboBox)
	    				)	    				
    	);
	}

	private JComboBox createRegionComboBox() {
		JComboBox comboBox = new JComboBox();
		comboBox.setMinimumSize(new Dimension(120, GuiConstants.COMBO_BOX_HEIGHT));
		comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.COMBO_BOX_HEIGHT));
		
		DefaultComboBoxModel model = new DefaultComboBoxModel();		
		List<MapRegion> regions = listMapRegions();
		for (MapRegion region : regions) {
			if (!region.getRegionName().equalsIgnoreCase("unknown")) {
				model.addElement(" " + region.getRegionName());
			}
		}
		
		comboBox.setModel(model);
		return comboBox;
	}

	/**
	 * @return
	 * 		All {@link MapRegion} objects.
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	List<MapRegion> listMapRegions() {
		Session session = database.getCurrentSession();
		return session.createCriteria(MapRegion.class)
				.addOrder(Order.asc(MapRegion.REGION_NAME))
				.list();
	}

	/**
	 * @return
	 * 		All {@link ApiKey} objects.
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	List<ApiKey> listCharacters() {
		Session session = database.getCurrentSession();
		return session.createCriteria(ApiKey.class)
				.addOrder(Order.asc(ApiKey.CHARACTER_NAME))
				.list();
	}

	private JComboBox createCharacterComboBox() {
		JComboBox comboBox = new JComboBox();
		comboBox.setMinimumSize(new Dimension(120, GuiConstants.COMBO_BOX_HEIGHT));
		comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.COMBO_BOX_HEIGHT));
		
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		List<ApiKey> apiKeys = listCharacters();
		for (ApiKey apiKey : apiKeys) {
			model.addElement(" " + apiKey.getCharacterName());
		}
		
		comboBox.setModel(model);
		
		return comboBox;
	}

	private JCheckBox createCheckBox(String value, final JComponent... dependentComponent) {
		final JCheckBox checkBox = new JCheckBox(value);
		checkBox.setMinimumSize(new Dimension(120, 0));
		checkBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.SPINNER_HEIGHT));
		
		StateRule[] stateRules = new StateRule[dependentComponent.length];
		for (int i = 0; i < dependentComponent.length; i++) {
			final JComponent dependent = dependentComponent[i];
			stateRules[i] = new StateRule() {
				@Override
				public void onValid(boolean isValid) {
					dependent.setEnabled(isValid);
				}
			};
		}
		
		ValidationRule isCheckBoxSelectedRule = new ValidationRule(stateRules) {
			@Override
			public boolean isValid() {
				return checkBox.isSelected();
			}
		};
		
		isCheckBoxSelectedRule.trigger();
		
		checkBox.addChangeListener(new ValidationListener(isCheckBoxSelectedRule));
		
		return checkBox;
	}

	private JSpinner createDateSpinner(final Model<Date> dateValue) {
		final SpinnerDateModel model = new SpinnerDateModel(dateValue.getValue(), null, null, Calendar.DAY_OF_YEAR);
		JSpinner spinner = new JSpinner(model);
		spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm:ss dd/MM/yyyy "));
		spinner.setMinimumSize(new Dimension(120, GuiConstants.SPINNER_HEIGHT));
		spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.SPINNER_HEIGHT));
		
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				dateValue.setValue(model.getDate());
			}
		});
		
		return spinner;
	}

	@Override
	public DialogTitle getTitle() {
		return new ReportFilteringTitle();
	}

	@Override
	public boolean allowNext() {
		return true;
	}

	@Override
	public boolean allowPrevious() {
		return true;
	}

	@Override
	public void removeListeners() {
		// TODO Auto-generated method stub
		
	}
	
}
