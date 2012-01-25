package nl.minicom.evenexus.gui.panels.report.renderers;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.definition.components.ReportGroup.Type;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;
import nl.minicom.evenexus.core.report.definition.components.ReportItem.Unit;
import nl.minicom.evenexus.core.report.engine.Dataset;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.gui.panels.report.DisplayConfiguration;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.time.Week;

/**
 * This class is responsible for drawing a graph for the report engine.
 * 
 * @author michael
 */
public final class LineChart extends Chart {

	public JFreeChart render(ChartPanel chartPanel, ReportModel reportModel, Dataset dataset, DisplayConfiguration configuration) {
		ReportGroup currentGroup = getCurrentGroup(reportModel, configuration.getExpressions());
		return render(chartPanel, reportModel, currentGroup, dataset, configuration);
	}
	
	public JFreeChart render(ChartPanel chartPanel, ReportModel reportModel, ReportGroup currentGroup, 
			Dataset dataset, DisplayConfiguration configuration) {

		XYPlot plot = new XYPlot();
		
		List<Color> colors = createColorSequence(reportModel.getReportItems().size());
		
		TimePeriodValuesCollection quantityCollection = new TimePeriodValuesCollection();
		TimePeriodValuesCollection currencyCollection = new TimePeriodValuesCollection();

		DefaultXYItemRenderer quantityRenderer = new DefaultXYItemRenderer();
		DefaultXYItemRenderer currencyRenderer = new DefaultXYItemRenderer();
		
		int currencyItems = 0;
		int quantityItems = 0;
		
		for (ReportItem reportItem : reportModel.getReportItems()) {
			TimePeriodValues series = new TimePeriodValues(reportItem.getKey());
			for (String group : dataset.groupSet()) {
				TimePeriod period = getPeriod(currentGroup, group);
				BigDecimal value = dataset.getValue(reportItem.getKey(), group);
				series.add(period, value);
			}
			
			int index = currencyItems + quantityItems;
			if (Unit.CURRENCY == reportItem.getUnit()) {
				currencyCollection.addSeries(series);
				currencyRenderer.setSeriesPaint(index, colors.get(index));
				currencyItems++;
			}
			else if (Unit.QUANTITY == reportItem.getUnit()) {
				quantityCollection.addSeries(series);
				quantityRenderer.setSeriesPaint(index, colors.get(index));
				quantityItems++;
			}
			else {
				throw new IllegalArgumentException("Invalid Unit: " + reportItem.getUnit().name());
			}
		}
		
		int index = 0;
		if (currencyItems > 0) {
			plot.setDataset(index, currencyCollection);
			plot.setRangeAxis(index, new NumberAxis("ISK"));
			plot.setRenderer(index, currencyRenderer);
			plot.mapDatasetToRangeAxis(index, index);
			index++;
		}
		if (quantityItems > 0) {
			plot.setDataset(index, quantityCollection);
			NumberAxis numberAxis = new NumberAxis("Quantity");
			plot.setRangeAxis(index, numberAxis);
			plot.setRenderer(index, quantityRenderer);
			plot.mapDatasetToRangeAxis(index, index);
		}
		
		plot.setDomainAxis(new DateAxis(currentGroup.getKey()));
		JFreeChart chart = new JFreeChart(plot);
		chartPanel.setChart(chart);
		
		return chart;
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
			System.err.println(input);
			Calendar c = GregorianCalendar.getInstance();
			c.set(Calendar.YEAR, Integer.parseInt(split[0]));
			c.set(Calendar.DAY_OF_YEAR, Integer.parseInt(split[1]) * 7);
			return new Week(c.getTime());
		}
		else if (Type.MONTH == currentGroup.getType()) {
			String[] split = input.split("-");
			Calendar c = GregorianCalendar.getInstance();
			c.set(Calendar.YEAR, Integer.parseInt(split[0]));
			c.set(Calendar.MONTH, Integer.parseInt(split[1]));
			return new Month(c.getTime());
		}
		else {
			throw new IllegalArgumentException("Unsupported type: " + currentGroup.getType().name());
		}
	}

	@Override
	public void removeListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshGraph() {
		// TODO Auto-generated method stub
		
	}

}
