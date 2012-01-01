package nl.minicom.evenexus.gui.panels.report.renderers;

import java.util.Date;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;
import nl.minicom.evenexus.core.report.engine.Dataset;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.core.report.persistence.expressions.Expression;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;

/**
 * This class is responsible for drawing a graph for the report engine.
 * 
 * @author michael
 */
public final class LineGraphChart {

	/**
	 * This method creates a {@link JFreeChart} based on the provided dataset.
	 * 
	 * @param reportModel
	 * 		The {@link ReportModel}.
	 * 
	 * @param dataset
	 * 		The associated {@link Dataset}.
	 * 
	 * @param groupExpressions
	 * 		The used group {@link Expression}s.
	 * 
	 * @return
	 * 		The constructed {@link JFreeChart}.
	 */
	public static JFreeChart render(ReportModel reportModel, Dataset dataset, Expression... groupExpressions) {
		ReportItem reportItem = reportModel.getReportItems().get(0);
		TimePeriodValues series = new TimePeriodValues(reportItem.getKey());
		series.add(new Day(new Date()), 100.0);
		series.add(new Day(new Date(new Date().getTime() + 3600 * 1000 * 24)), 100.0);
		
		TimePeriodValuesCollection collection = new TimePeriodValuesCollection();
		collection.addSeries(series);
		
		XYPlot plot1 = new XYPlot();
		plot1.setDataset(collection);
		plot1.setRangeAxis(new NumberAxis(reportItem.getKey()));
		plot1.setRenderer(new DefaultXYItemRenderer());
		
		ReportGroup currentGroup = getCurrentGroup(reportModel, groupExpressions);
		ValueAxis domainAxis = new DateAxis(currentGroup.getKey());
		
		CombinedDomainXYPlot plot = new CombinedDomainXYPlot(domainAxis);
		plot.add(plot1);
		
		return new JFreeChart(plot);
	}
	
	private static ReportGroup getCurrentGroup(ReportModel reportModel, Expression... groupExpressions) {
		int index = 0;
		for (Expression expression : groupExpressions) {
			if (expression == null) {
				continue;
			}
			index++;
		}
		return reportModel.getReportGroups().get(index);
	}
	
	private LineGraphChart() {
		// Prevent instatiation.
	}

}
