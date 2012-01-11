package nl.minicom.evenexus.gui.panels.report.renderers;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Collections;
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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.IntervalBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;

import com.google.common.collect.Lists;

/**
 * This class is responsible for drawing a graph for the report engine.
 * 
 * @author michael
 */
public final class BarChart {

	private static final int LIMIT = 20;

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

		CategoryPlot plot = new CategoryPlot();
		
		List<Color> colors = createColorSequence(Math.min(LIMIT, reportModel.getReportItems().size()));
		BarRenderer currencyRenderer = createRenderer();
		BarRenderer quantityRenderer = createRenderer();

		DefaultCategoryDataset quantityCollection = new DefaultCategoryDataset();
		DefaultCategoryDataset currencyCollection = new DefaultCategoryDataset();
		
		int count = 0;
		boolean hasQuantityItems = false;
		boolean hasCurrencyItems = false;
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
					hasCurrencyItems = true;
				}
				else if (Unit.QUANTITY == reportItem.getUnit()) {
					currencyCollection.addValue(BigDecimal.ZERO, reportItem.getKey(), group);
					quantityCollection.addValue(value, reportItem.getKey(), group);
					hasQuantityItems = true;
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
		
		if (hasCurrencyItems) {
			plot.setDataset(index, currencyCollection);
			plot.setRangeAxis(index, currencyAxis);
			plot.setRenderer(index, currencyRenderer);
			plot.mapDatasetToRangeAxis(index, index);
			index++;
		}
		if (hasQuantityItems) {
			plot.setDataset(index, quantityCollection);
			plot.setRangeAxis(index, quantityAxis);
			plot.setRenderer(index, quantityRenderer);
			plot.mapDatasetToRangeAxis(index, index);
		}

		if (hasCurrencyItems && hasQuantityItems) {
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
	
	private BarChart() {
		// Prevent instatiation.
	}

}
