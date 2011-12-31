package nl.minicom.evenexus.gui.panels.report;

import java.awt.Dimension;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JPanel;

import nl.minicom.evenexus.core.report.engine.ReportModel;

/**
 * This panel draws the user defines reports.
 * 
 * @author michael
 */
@Singleton
public class ReportChartPanel extends JPanel {

	private static final long serialVersionUID = -6460787014267238845L;
	
	/**
	 * Constructs a new {@link ReportChartPanel}.
	 */
	@Inject
	public ReportChartPanel() {
		setMinimumSize(new Dimension(0, 0));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}

	/**
	 * This method draws the specified {@link ReportModel}.
	 * 
	 * @param reportModel
	 * 		The {@link ReportModel} to draw.
	 */
	public void drawReport(ReportModel reportModel) {
		// TODO Auto-generated method stub
		
	}

}
