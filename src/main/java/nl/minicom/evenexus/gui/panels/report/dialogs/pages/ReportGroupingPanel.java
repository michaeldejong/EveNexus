package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import javax.inject.Inject;
import javax.swing.GroupLayout;
import javax.swing.JLabel;

import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.core.report.engine.ReportModelListener;
import nl.minicom.evenexus.gui.utils.dialogs.DialogTitle;

import com.google.inject.Provider;

public class ReportGroupingPanel extends ReportBuilderPage {

	private static final long serialVersionUID = 3066113966844699181L;

	private final Provider<ReportGroupPanel> reportGroupPanelProvider;
	
	private ReportModel model;

	@Inject
	public ReportGroupingPanel(Provider<ReportGroupPanel> reportGroupPanelProvider) {
		this.reportGroupPanelProvider = reportGroupPanelProvider;
	}
	
	public ReportGroupingPanel initialize(ReportModel model) {
		this.model = model;
		buildGui();
		
		return this;
	}

	private void buildGui() {
		JLabel group1Label = createBoldLabel("Grouping 1");
		JLabel group2Label = createBoldLabel("Grouping 2");
		JLabel group3Label = createBoldLabel("Grouping 3");
		
		final ReportGroupPanel group1Panel = reportGroupPanelProvider.get().initialize(model.getGrouping1());
		final ReportGroupPanel group2Panel = reportGroupPanelProvider.get().initialize(model.getGrouping2());
		final ReportGroupPanel group3Panel = reportGroupPanelProvider.get().initialize(model.getGrouping3());

		addReportGroupPanelLogic(group1Panel, group2Panel);
		addReportGroupPanelLogic(group2Panel, group3Panel);
		
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
	
	private void addReportGroupPanelLogic(final ReportGroupPanel parent, final ReportGroupPanel child) {
		model.addListener(new ReportModelListener() {
			@Override
			public void onValueChanged() {
				if (parent.isSelected()) {
					child.setEnabled(true);
				}
				else {
					child.setEnabled(false);
					child.setSelected(false);
				}
			}
		});
	}

	@Override
	public DialogTitle getTitle() {
		return DialogTitle.REPORT_GROUP_TITLE;
	}
	
}
