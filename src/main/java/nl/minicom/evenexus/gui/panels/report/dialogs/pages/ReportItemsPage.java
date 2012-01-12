package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nl.minicom.evenexus.core.report.definition.ReportDefinition;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.core.report.engine.ReportModel.ItemListener;
import nl.minicom.evenexus.gui.utils.dialogs.titles.DialogTitle;
import nl.minicom.evenexus.gui.utils.dialogs.titles.ReportItemTitle;
import nl.minicom.evenexus.i18n.Translator;

import com.google.common.collect.Maps;

/**
 * This page allows the user to define on what report items he or she wishes to report.
 *
 * @author michael
 */
public class ReportItemsPage extends ReportWizardPage implements ItemListener {

	private static final long serialVersionUID = 3066113966844699181L;

	private final Map<ReportItem, JCheckBox> mapping;
	private final ReportDefinition definition;
	private final Translator translator;
	private final ReportModel model;

	/**
	 * This constructs a new {@link ReportItemsPage}.
	 * 
	 * @param definition
	 * 		The {@link ReportDefinition} containing all available items.
	 * 
	 * @param model
	 * 		The {@link ReportModel} describing the report the uses wishes to execute.
	 * 
	 * @param translator
	 * 		The {@link Translator}, which translates the items to the user's language.
	 */
	@Inject
	public ReportItemsPage(ReportDefinition definition, ReportModel model, Translator translator) {
		super(model);
		this.mapping = Maps.newHashMap();
		this.definition = definition;
		this.translator = translator;
		this.model = model;
		
		model.addListener(this);
	}

	/**
	 * This method builds the items panel.
	 */
	@Override
	public void buildGui() {
		JPanel itemPanel = new JPanel();
		itemPanel.setLayout(null);
		itemPanel.setBackground(Color.WHITE);
		
		JScrollPane scrollPane = new JScrollPane(itemPanel);
		scrollPane.setMinimumSize(new Dimension(0, 0));
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);        
		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(scrollPane));
    	layout.setVerticalGroup(layout.createSequentialGroup().addComponent(scrollPane));
		
		int y = 6;
		for (final ReportItem item : definition.getItems()) {
			String keyTranslation = translator.translate(item.getKey());
			
			final JCheckBox checkBox = new JCheckBox();
			boolean selected = model.hasItem(item.getKey());
			checkBox.setSelected(selected);
			checkBox.setText(" " + keyTranslation + " ");
			checkBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String itemAlias = item.getKey();
					if (model.hasItem(itemAlias)) {
						model.removeItem(itemAlias);
					}
					else {
						model.addItem(item);
					}
				}
			});
			
			checkBox.setBounds(6, y, 340, 18);
			checkBox.setBackground(Color.WHITE);
			mapping.put(item, checkBox);
			
			itemPanel.add(checkBox);
			y += 21;
		}
	}

	@Override
	public DialogTitle getTitle() {
		return new ReportItemTitle();
	}

	@Override
	public boolean allowPrevious() {
		return false;
	}

	/**
	 * This method removes all listeners from the {@link ReportModel}.
	 */
	@Override
	public void removeListeners() {
		model.removeListener(this);
	}
	
	/**
	 * @return
	 *		This method returns true if the user is allowed to go to the next page.
	 */
	@Override
	public boolean allowNext() {
		return !model.getReportItems().isEmpty();
	}

	@Override
	public void onReportItemAdded(ReportItem item) {
		mapping.get(item).setSelected(true);
		
	}

	@Override
	public void onReportItemRemoved(ReportItem item) {
		mapping.get(item).setSelected(false);
	}
	
}
