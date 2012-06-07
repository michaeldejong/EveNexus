package nl.minicom.evenexus.gui.panels.report.renderers;

import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;
import java.util.Set;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.engine.Dataset;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.panels.report.DisplayConfiguration;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.data.general.DatasetUtilities;

import com.google.common.collect.Sets;

/**
 * This class is responsible for drawing a graph for the report engine.
 * 
 * @author michael
 */
public final class PieChart extends Chart implements MouseWheelListener, ChartMouseListener {

	
	private final ChartPanel chartPanel;
	private final ReportModel reportModel;
	
	private MultiplePiePlot plot;
	
	public PieChart(ChartPanel chartPanel, ReportModel reportModel, Dataset dataset, DisplayConfiguration configuration) {
		this (chartPanel, reportModel, getCurrentGroup(reportModel, configuration.getExpressions()), dataset, configuration);
	}
	
	public PieChart(final ChartPanel chartPanel, ReportModel reportModel, ReportGroup currentGroup, 
			Dataset dataset, DisplayConfiguration configuration) {
		
		this.chartPanel = chartPanel;
		this.reportModel = reportModel;
		this.plot = drawGraph();
		attachListeners();
	}
	
	private void attachListeners() {
		chartPanel.addMouseWheelListener(this);
		chartPanel.addChartMouseListener(this);
	}
	
	public void removeListeners() {
		chartPanel.removeMouseWheelListener(this);
		chartPanel.removeChartMouseListener(this);
	}
	
	public void refreshGraph() {
		plot = drawGraph();
	}

	private final MultiplePiePlot drawGraph() {
		
		List<Color> colors = createColorSequence(reportModel.getReportItems().size());
		
		MultiplePiePlot plot = new MultiplePiePlot(DatasetUtilities.createCategoryDataset("Region", "Sales", createDataset()));
		
		JFreeChart chart = new JFreeChart(plot);
		chartPanel.setChart(chart);
		
		return plot;
	}
	
	private double[][] createDataset() {
		return new double[][] {
				{1.0, 2.0},
				{1.5, 1.5}
		};
	}

	@Override
	public void chartMouseClicked(ChartMouseEvent arg0) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent arg0) {
		
	}

}
