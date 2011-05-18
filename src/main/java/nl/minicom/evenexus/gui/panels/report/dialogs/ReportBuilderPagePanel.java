package nl.minicom.evenexus.gui.panels.report.dialogs;

import javax.swing.GroupLayout;
import javax.swing.JPanel;

import nl.minicom.evenexus.core.report.definition.ReportDefinition;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportBuilderPage;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportDisplayPanel;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportFiltersPanel;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportGroupingPanel;
import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportItemsPanel;

public class ReportBuilderPagePanel extends JPanel {

	private static final long serialVersionUID = 5860745283553056308L;

	private final ReportBuilderDialog dialog;
	private final ReportBuilderPage[] pages;
	private int currentIndex;
	
	public ReportBuilderPagePanel(ReportBuilderDialog dialog, ReportDefinition definition, ReportModel model) {
		this.dialog = dialog;
		this.currentIndex = 0;
		this.pages = new ReportBuilderPage[] {
				new ReportItemsPanel(definition, model),
				new ReportGroupingPanel(definition, model),
				new ReportFiltersPanel(definition, model),
				new ReportDisplayPanel(definition, model)
		};
		
		showCurrentPage();
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

	private final void showCurrentPage() {
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

	private final void removeCurrentPage() {
		removeAll();
	}
	
}
