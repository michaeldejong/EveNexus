package nl.minicom.evenexus.gui.panels.report.renderers;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;
import nl.minicom.evenexus.core.report.definition.components.ReportItem.Unit;
import nl.minicom.evenexus.core.report.engine.Dataset;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.core.report.persistence.expressions.Expression;

import org.jfree.chart.ChartPanel;
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

/**
 * This class is responsible for drawing a graph for the report engine.
 * 
 * @author michael
 */
public final class BarChart extends Chart {

	private static final int LIMIT = 20;
	
//	private static Set<CategoryMarker> markers = Sets.newHashSet();

	public JFreeChart render(ChartPanel chartPanel, ReportModel reportModel, Dataset dataset, Expression... groupExpressions) {
		ReportGroup currentGroup = getCurrentGroup(reportModel, groupExpressions);
		return render(chartPanel, reportModel, currentGroup, dataset, groupExpressions);
	}
	
	public JFreeChart render(final ChartPanel chartPanel, ReportModel reportModel, ReportGroup currentGroup, 
			Dataset dataset, Expression[] groupExpressions) {

		final CategoryPlot plot = new CategoryPlot();
		
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
			currencyRenderer.setSeriesPaint(count, colors.get(count));
			quantityRenderer.setSeriesPaint(count, colors.get(count));
			
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
		chartPanel.setChart(chart);
		
//		chartPanel.addChartMouseListener(new ChartMouseListener() {
//			@Override
//			public void chartMouseMoved(ChartMouseEvent arg0) {
//				int x = arg0.getTrigger().getX();
//				CategoryItemEntity entity = null;
//				for (int y = 0; y < (int) chartPanel.getSize().getHeight(); y++) {
//					ChartEntity entityForPoint = chartPanel.getEntityForPoint(x, y);
//					if (entityForPoint instanceof CategoryItemEntity) {
//						entity = (CategoryItemEntity) entityForPoint;
//						if (entity != null) {
//							break;
//						}
//					}
//				}
//
//				synchronized (markers) {
//					if (!markers.isEmpty()) {
//						for (CategoryMarker marker : markers) {
//							plot.removeDomainMarker(marker);
//						}
//					}
//					
//					if (entity != null) {
//						CategoryMarker marker = new CategoryMarker(entity.getColumnKey());
//						marker.setAlpha(0.2f);
//						chartPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//						plot.addDomainMarker(marker);
//						
//						markers.add(marker);
//					}
//					else {
//						chartPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//					}
//				}
//			}
//			
//			@Override
//			public void chartMouseClicked(ChartMouseEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		return chart;
	}
	
	private BarRenderer createRenderer() {
		IntervalBarRenderer renderer = new IntervalBarRenderer();
		renderer.setShadowVisible(false);
		renderer.setItemMargin(0.05);
		renderer.setBarPainter(new StandardBarPainter());
		return renderer;
	}

}
