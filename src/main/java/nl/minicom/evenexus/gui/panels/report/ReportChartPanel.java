package nl.minicom.evenexus.gui.panels.report;

import java.awt.Dimension;

import javax.inject.Singleton;
import javax.swing.JPanel;

import nl.minicom.evenexus.core.report.engine.ReportModel;

@Singleton
public class ReportChartPanel extends JPanel {

	private static final long serialVersionUID = -6460787014267238845L;
	
	public ReportChartPanel() {
		setMinimumSize(new Dimension(0, 0));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}

	public void drawReport(ReportModel reportModel) {
		// TODO Auto-generated method stub
		
	}

}
