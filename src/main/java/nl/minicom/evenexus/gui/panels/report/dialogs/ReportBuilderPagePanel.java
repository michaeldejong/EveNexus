package nl.minicom.evenexus.gui.panels.report.dialogs;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.GroupLayout;
import javax.swing.JPanel;

import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportBuilderPage;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportDisplayPanel;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportFiltersPanel;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportGroupingPanel;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportItemsPanel;

public class ReportBuilderPagePanel extends JPanel {

	private static final long serialVersionUID = 5860745283553056308L;

	private final Provider<ReportItemsPanel> reportItemsPanelProvider;
	private final Provider<ReportGroupingPanel> reportGroupingPanelProvider;
	private final Provider<ReportFiltersPanel> reportFiltersPanelProvider;
	private final Provider<ReportDisplayPanel> reportDisplayPanelProvider;
	
	private ReportBuilderDialog dialog;
	private ReportBuilderPage[] pages;
	private int currentIndex;
	
	@Inject
	public ReportBuilderPagePanel(
			Provider<ReportItemsPanel> reportItemsPanelProvider,
			Provider<ReportGroupingPanel> reportGroupingPanelProvider,
			Provider<ReportFiltersPanel> reportFiltersPanelProvider,
			Provider<ReportDisplayPanel> reportDisplayPanelProvider) {
		
		this.reportItemsPanelProvider = reportItemsPanelProvider;
		this.reportGroupingPanelProvider = reportGroupingPanelProvider;
		this.reportFiltersPanelProvider = reportFiltersPanelProvider;
		this.reportDisplayPanelProvider = reportDisplayPanelProvider;
		
		this.currentIndex = 0;
	}
	
	public ReportBuilderPagePanel initialize(ReportBuilderDialog dialog, ReportModel model) {
		this.dialog = dialog;
		this.pages = new ReportBuilderPage[] {
				reportItemsPanelProvider.get().initialize(model),
				reportGroupingPanelProvider.get().initialize(model),
				reportFiltersPanelProvider.get().initialize(model),
				reportDisplayPanelProvider.get().initialize(model)
		};
		
		showCurrentPage();
		
		return this;
	}
	
	public final boolean hasNextPage() {
		return currentIndex + 1 < pages.length;
	}
	
	public final boolean hasPreviousPage() {
		return currentIndex - 1 >= 0;
	}
	
	public final void showNextPage() {
		if (hasNextPage()) {
			removeCurrentPage();
			currentIndex++;
			showCurrentPage();
		}
	}
	
	public final void showPreviousPage() {
		if (hasPreviousPage()) {
			removeCurrentPage();
			currentIndex--;
			showCurrentPage();
		}
	}

	private void showCurrentPage() {
		ReportBuilderPage currentPage = pages[currentIndex];
		
		dialog.createTitle(currentPage.getTitle());
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);        
		layout.setHorizontalGroup(
		      	layout.createSequentialGroup()
		        		.addComponent(currentPage)
    	);
    	layout.setVerticalGroup(
	    		layout.createSequentialGroup()
	    				.addComponent(currentPage)
    	);
	}

	private void removeCurrentPage() {
		removeAll();
	}
	
}
