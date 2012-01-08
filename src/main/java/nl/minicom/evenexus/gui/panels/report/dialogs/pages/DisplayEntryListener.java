package nl.minicom.evenexus.gui.panels.report.dialogs.pages;

import nl.minicom.evenexus.gui.panels.report.dialogs.pages.ReportDisplayPage.ReportDisplayEntry;

public interface DisplayEntryListener {

	void onStateChange(ReportDisplayEntry selected);
	
	void onSelectionMove(ReportDisplayEntry selected);

	void onDeselectionMove(ReportDisplayEntry selected);
	
}
