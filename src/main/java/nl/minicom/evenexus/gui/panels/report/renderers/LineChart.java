package nl.minicom.evenexus.gui.panels.report.renderers;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.definition.components.ReportGroup.Type;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;
import nl.minicom.evenexus.core.report.definition.components.ReportItem.Unit;
import nl.minicom.evenexus.core.report.engine.Dataset;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.core.report.persistence.expressions.Expression;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;

/**
 * This class is responsible for drawing a graph for the report engine.
 * 
 * @author michael
 */
public final class LineChart {

	public static JFreeChart render(ReportModel reportModel, Dataset dataset, Expression... groupExpressions) {
		ReportGroup currentGroup = getCurrentGroup(reportModel, groupExpressions);
		return render(reportModel, currentGroup, dataset, groupExpressions);
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
	
	public static JFreeChart render(ReportModel reportModel, ReportGroup currentGroup, 
			Dataset dataset, Expression[] groupExpressions) {

		XYPlot plot = new XYPlot();
		
		TimePeriodValuesCollection quantityCollection = new TimePeriodValuesCollection();
		TimePeriodValuesCollection currencyCollection = new TimePeriodValuesCollection();
		
		for (ReportItem reportItem : reportModel.getReportItems()) {
			TimePeriodValues series = new TimePeriodValues(reportItem.getKey());
			for (String group : dataset.groupSet()) {
				TimePeriod period = getPeriod(currentGroup, group);
				BigDecimal value = dataset.getValue(reportItem.getKey(), group);
				series.add(period, value);
			}
			if (Unit.CURRENCY == reportItem.getUnit()) {
				currencyCollection.addSeries(series);
			}
			else if (Unit.QUANTITY == reportItem.getUnit()) {
				quantityCollection.addSeries(series);
			}
			else {
				throw new IllegalArgumentException("Invalid Unit: " + reportItem.getUnit().name());
			}
		}
		
		int index = 0;
		if (currencyCollection.getSeriesCount() > 0) {
			plot.setDataset(index, currencyCollection);
			plot.setRangeAxis(index, new NumberAxis("ISK"));
			plot.setRenderer(index, new DefaultXYItemRenderer());
			plot.mapDatasetToRangeAxis(index, index);
			index++;
		}
		if (quantityCollection.getSeriesCount() > 0) {
			plot.setDataset(index, quantityCollection);
			NumberAxis numberAxis = new NumberAxis("Quantity");
			plot.setRangeAxis(index, numberAxis);
			plot.setRenderer(index, new DefaultXYItemRenderer());
			plot.mapDatasetToRangeAxis(index, index);
		}
		
		plot.setDomainAxis(new DateAxis(currentGroup.getKey()));
		
		return new JFreeChart(plot);
	}
	
	private static TimePeriod getPeriod(ReportGroup currentGroup, String input) {
		if (Type.DAY == currentGroup.getType()) {
			String[] split = input.split("-");
			Calendar c = GregorianCalendar.getInstance();
			c.set(Calendar.YEAR, Integer.parseInt(split[0]));
			c.set(Calendar.DAY_OF_YEAR, Integer.parseInt(split[1]));
			return new Day(c.getTime());
		}
		else if (Type.WEEK == currentGroup.getType()) {
			String[] split = input.split("-");
			Calendar c = GregorianCalendar.getInstance();
			c.set(Calendar.YEAR, Integer.parseInt(split[0]));
			c.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(split[1]));
			return new Day(c.getTime());
		}
		else if (Type.MONTH == currentGroup.getType()) {
			String[] split = input.split("-");
			Calendar c = GregorianCalendar.getInstance();
			c.set(Calendar.YEAR, Integer.parseInt(split[0]));
			c.set(Calendar.MONTH, Integer.parseInt(split[1]));
			return new Day(c.getTime());
		}
		else {
			throw new IllegalArgumentException("Unsupported type: " + currentGroup.getType().name());
		}
	}

	private LineChart() {
		// Prevent instatiation.
	}

}
