package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nl.minicom.evenexus.core.report.definition.ReportDefinition;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.utils.dialogs.DialogTitle;
import nl.minicom.evenexus.i18n.Translator;

public class ReportItemsPanel extends ReportBuilderPage {

	private static final long serialVersionUID = 3066113966844699181L;

	private final ReportDefinition definition;
	private final Translator translator;
	
	private ReportModel model;

	@Inject
	public ReportItemsPanel(ReportDefinition definition, Translator translator) {
		this.definition = definition;
		this.translator = translator;
	}

	public ReportBuilderPage initialize(ReportModel model) {
		this.model = model;
		buildGui();

		return this;
	}

	private void buildGui() {		
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
			checkBox.setText(" " + keyTranslation + " ");
			checkBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String itemAlias = item.getKey();
					if (model.hasItem(itemAlias)) {
						model.removeItem(itemAlias);
						checkBox.setSelected(false);
					}
					else {
						model.addItem(item);
						checkBox.setSelected(true);
					}
				}
			});
			
			checkBox.setBounds(6, y, 340, 18);
			checkBox.setBackground(Color.WHITE);
			itemPanel.add(checkBox);
			y += 21;
		}
	}

	@Override
	public DialogTitle getTitle() {
		return DialogTitle.REPORT_ITEM_TITLE;
	}
	
}
