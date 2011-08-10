package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.GuiConstants;
import nl.minicom.evenexus.gui.utils.dialogs.DialogTitle;
import nl.minicom.evenexus.gui.validation.StateRule;
import nl.minicom.evenexus.gui.validation.ValidationListener;
import nl.minicom.evenexus.gui.validation.ValidationRule;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.MapRegion;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

public class ReportFiltersPanel extends ReportBuilderPage {

	private static final long serialVersionUID = 3066113966844699181L;

	private final Database database;
	
	private ReportModel model; 

	@Inject
	public ReportFiltersPanel(Database database) {
		this.database = database;
	}

	public ReportBuilderPage initialize(ReportModel model) {
		this.model = model;
		buildGui();

		return this;
	}

	private void buildGui() {
		JLabel timeLabel = createBoldLabel("Time filters");
		JLabel locationLabel = createBoldLabel("Location filters");
		JLabel characterLabel = createBoldLabel("Character filters");
		
		Date bottomLimit = createLowerLimitCalendar();
		Date currentBottomLimit = createCurrentLowerLimitCalendar();
		Date upperLimit = createUpperLimitCalendar();
		
		JSpinner fromDateSpinner = createDateSpinner(bottomLimit, currentBottomLimit, upperLimit);
		JCheckBox fromBox = createCheckBox("From: ", fromDateSpinner);

		JSpinner toSpinner = createDateSpinner(bottomLimit, upperLimit, upperLimit);
		JCheckBox toBox = createCheckBox("To: ", toSpinner);
		
		JComboBox regionComboBox = createRegionComboBox();
		JCheckBox regionBox = createCheckBox("Region: ", regionComboBox);
		
		JComboBox systemComboBox = createSystemComboBox();
		JCheckBox systemBox = createCheckBox("System: ", systemComboBox);
		
		JComboBox characterComboBox = createCharacterComboBox();
		JCheckBox characterBox = createCheckBox("Character: ", characterComboBox);
		
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
		      					.addComponent(systemBox)
		      					.addComponent(characterLabel)
		      					.addComponent(characterBox)
		      			)
		      			.addGroup(layout.createParallelGroup()
		      					.addComponent(fromDateSpinner)
		      					.addComponent(toSpinner)
		      					.addComponent(regionComboBox)
		      					.addComponent(systemComboBox)
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
	    				.addGroup(layout.createParallelGroup()
	    						.addComponent(systemBox)
	    						.addComponent(systemComboBox)
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

	@Transactional
	@SuppressWarnings("unchecked")
	protected List<MapRegion> listMapRegions() {
		Session session = database.getCurrentSession();
		return session.createCriteria(MapRegion.class)
				.addOrder(Order.asc("regionName"))
				.list();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	protected List<ApiKey> listCharacters() {
		Session session = database.getCurrentSession();
		return session.createCriteria(ApiKey.class)
				.addOrder(Order.asc("name"))
				.list();
	}

	private JComboBox createSystemComboBox() {
		JComboBox comboBox = new JComboBox();
		comboBox.setMinimumSize(new Dimension(120, GuiConstants.COMBO_BOX_HEIGHT));
		comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.COMBO_BOX_HEIGHT));
		
		List<MapRegion> regions = listMapRegions();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for (MapRegion region : regions) {
			if (!region.getRegionName().equalsIgnoreCase("unknown")) {
				model.addElement(" " + region.getRegionName());
			}
		}
		
		comboBox.setModel(model);
		return comboBox;
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

	private JSpinner createDateSpinner(Date bottomLimit, Date currentBottomLimit, Date upperLimit) {
		JSpinner spinner = new JSpinner(new SpinnerDateModel(currentBottomLimit, bottomLimit, upperLimit, Calendar.DAY_OF_YEAR));
		spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm:ss dd/MM/yyyy "));
		spinner.setMinimumSize(new Dimension(120, GuiConstants.SPINNER_HEIGHT));
		spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, GuiConstants.SPINNER_HEIGHT));
		return spinner;
	}

	private Date createLowerLimitCalendar() {
		return new Date(0L);
	}

	private Date createCurrentLowerLimitCalendar() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}

	private Date createUpperLimitCalendar() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}

	@Override
	public DialogTitle getTitle() {
		return DialogTitle.REPORT_FILTER_TITLE;
	}
	
}
