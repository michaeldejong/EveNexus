package nl.minicom.evenexus.gui.panels.report.renderers;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.definition.components.ReportGroup.Type;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;
import nl.minicom.evenexus.core.report.definition.components.ReportItem.Unit;
import nl.minicom.evenexus.core.report.engine.Dataset;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.core.report.persistence.expressions.Expression;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.IntervalBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;

import com.google.common.collect.Lists;

/**
 * This class is responsible for drawing a graph for the report engine.
 * 
 * @author michael
 */
public final class LineGraphChart {

	private static final int LIMIT = 20;

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
		ReportGroup currentGroup = getCurrentGroup(reportModel, groupExpressions);
		if (matches(currentGroup, Type.DAY, Type.WEEK, Type.MONTH)) {
			return renderDateGraph(reportModel, currentGroup, dataset, groupExpressions);
		}
		else if (matches(currentGroup, Type.NAME)) {
			return renderCategoryGraph(reportModel, currentGroup, dataset, groupExpressions);
		}
		else {
			throw new IllegalArgumentException("Unsupported type: " + currentGroup.getType().name());
		}
	}
	
	private static JFreeChart renderDateGraph(ReportModel reportModel, ReportGroup currentGroup, 
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

	private static JFreeChart renderCategoryGraph(ReportModel reportModel, ReportGroup currentGroup, 
			Dataset dataset, Expression[] groupExpressions) {

		CategoryPlot plot = new CategoryPlot();
		
		List<Color> colors = createColorSequence(Math.min(LIMIT, reportModel.getReportItems().size()));
		BarRenderer currencyRenderer = createRenderer();
		BarRenderer quantityRenderer = createRenderer();

		DefaultCategoryDataset quantityCollection = new DefaultCategoryDataset();
		DefaultCategoryDataset currencyCollection = new DefaultCategoryDataset();
		
		int count = 0;
		for (ReportItem reportItem : reportModel.getReportItems()) {
			int index = 0;
			currencyRenderer.setSeriesFillPaint(count, colors.get(count));
			quantityRenderer.setSeriesFillPaint(count, colors.get(count));
			
			if (reportItem.getUnit() == Unit.CURRENCY) {
				currencyRenderer.setSeriesVisibleInLegend(count, true);
				quantityRenderer.setSeriesVisibleInLegend(count, false);
			}
			else if (reportItem.getUnit() == Unit.QUANTITY) {
				currencyRenderer.setSeriesVisibleInLegend(count, false);
				quantityRenderer.setSeriesVisibleInLegend(count, true);
			}
			
			for (String group : dataset.groupSet()) {
				if (index > LIMIT) {
					continue;
				}
				BigDecimal value = dataset.getValue(reportItem.getKey(), group);
				if (Unit.CURRENCY == reportItem.getUnit()) {
					quantityCollection.addValue(BigDecimal.ZERO, reportItem.getKey(), group);
					currencyCollection.addValue(value, reportItem.getKey(), group);
				}
				else if (Unit.QUANTITY == reportItem.getUnit()) {
					currencyCollection.addValue(BigDecimal.ZERO, reportItem.getKey(), group);
					quantityCollection.addValue(value, reportItem.getKey(), group);
				}
				else {
					throw new IllegalArgumentException("Invalid Unit: " + reportItem.getUnit().name());
				}
				index++;
			}
			count++;
		}
		
		int index = 0;
		NumberAxis currencyAxis = new NumberAxis("ISK");
		NumberAxis quantityAxis = new NumberAxis("Quantity");
		
		if (currencyCollection.getColumnCount() > 0) {
			
			plot.setDataset(index, currencyCollection);
			plot.setRangeAxis(index, currencyAxis);
			plot.setRenderer(index, currencyRenderer);
			plot.mapDatasetToRangeAxis(index, index);
			index++;
		}
		if (quantityCollection.getColumnCount() > 0) {
			
			plot.setDataset(index, quantityCollection);
			plot.setRangeAxis(index, quantityAxis);
			plot.setRenderer(index, quantityRenderer);
			plot.mapDatasetToRangeAxis(index, index);
		}

		Range combinedRange = Range.combine(quantityAxis.getRange(), currencyAxis.getRange());
		double quantityCenter = quantityAxis.getRange().getLength();
		double currencyCenter = currencyAxis.getRange().getLength();
		if (quantityCenter < currencyCenter) {
			quantityAxis.setRange(Range.scale(combinedRange, quantityCenter / currencyCenter));
			currencyAxis.setRange(combinedRange);
		}
		else {
			quantityAxis.setRange(combinedRange);
			currencyAxis.setRange(Range.scale(combinedRange, currencyCenter / quantityCenter));
		}
		
		CategoryAxis categoryAxis = new CategoryAxis(currentGroup.getKey());
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		plot.setDomainAxis(categoryAxis);

		JFreeChart chart = new JFreeChart(plot);
		return chart;
	}
	
	private static BarRenderer createRenderer() {
		IntervalBarRenderer renderer = new IntervalBarRenderer();
		renderer.setShadowVisible(false);
		renderer.setItemMargin(0.05);
		renderer.setBarPainter(new StandardBarPainter());
		return renderer;
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
	
	private static boolean matches(ReportGroup group, Type... types) {
		Type currentType = group.getType();
		for (Type type : types) {
			if (type == currentType) {
				return true;
			}
		}
		return false;
	}
	
	private static List<Color> createColorSequence(int length) {
		List<Color> colors = Lists.newArrayList();
		double count = 0.0;
		while (count < length) {
			int r = (int) (127.5 * (Math.cos(count) + 1.00));
			int g = (int) (127.5 * (Math.cos(count + Math.PI) + 1.00));
			int b = (int) (127.5 * (Math.sin(count + (1.5 * Math.PI)) + 1.00));
			colors.add(new Color(r, g, b));
			count = count + 1.0;
		}
		return Collections.unmodifiableList(colors);
	}
	
	private LineGraphChart() {
		// Prevent instatiation.
	}

}
