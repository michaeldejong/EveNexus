package nl.minicom.evenexus.gui.panels.dashboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import nl.minicom.evenexus.utils.TimeUtils;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimePeriodValue;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.ui.RectangleInsets;

public class LineGraphEngine extends ChartPanel {

	private static final long serialVersionUID = 6134550946248367873L;
	
//	private static final Logger logger = LoggerFactory.getRootLogger();

	private final List<GraphElement> elements;
	private int period;

//	private Marker marker = null;
	
	public LineGraphEngine(int period) {
		super(null, true);		
		this.elements = new ArrayList<GraphElement>();
		this.period = period;
	}
	
	public void addGraphElement(GraphElement element) {
		if (!elements.contains(element)) {
			elements.add(element);
		}
	}

	public List<GraphElement> getGraphElements() {
		return Collections.unmodifiableList(elements);
	}
	
	public void reload() {
		try {
			TimePeriodValuesCollection collection = new TimePeriodValuesCollection();
			XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
			
			int count = 0;
			for (int i = 0; i < elements.size(); i++) {
				GraphElement element = elements.get(i);
				if (element.isVisible()) {
					element.reload();
					collection.addSeries(createDataset(element));
					element.setRenderer(renderer, count);
					count++;
				}
			}
			
			final DateAxis dateAxis = new DateAxis();
			dateAxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 7));
			dateAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
			
			NumberAxis numberAxis = new NumberAxis("Amount  (millions ISK)");
			numberAxis.setNumberFormatOverride(new MillionsFormat());
			numberAxis.setAutoRangeIncludesZero(true);
			numberAxis.setAutoRangeStickyZero(false);
			
			XYPlot plot = new XYPlot();
			plot.addRangeMarker(new ValueMarker(0.0, Color.GRAY, new BasicStroke(1f)));
			plot.setDataset(collection);
			plot.setRangeAxis(numberAxis);
			plot.setDomainAxis(dateAxis);
			plot.setRenderer(renderer);
			plot.setBackgroundPaint(new Color(245, 245, 245));
						
			final JFreeChart chart = new JFreeChart(plot);
			chart.setPadding(new RectangleInsets(3, 7, 7, 0));
			chart.getLegend().setPadding(new RectangleInsets(7, 10, 7, 0));
			chart.setBackgroundPaint(Color.WHITE);
			
			setChart(chart);

//			addMouseMotionListener(new MouseMotionListener() {
//				@Override
//				public void mouseMoved(MouseEvent arg0) {
//					double mouse = arg0.getX();
//					long value = (long) dateAxis.java2DToValue(mouse, getScreenDataArea(), RectangleEdge.BOTTOM);
//					long halfDay = 12L * 3600L * 1000L;
//					
//					XYPlot xPlot = ((XYPlot) chart.getPlot());
//					xPlot.clearDomainMarkers();
//					
//					value = (long) (value / (2 * halfDay)) * (2 * halfDay) + halfDay;
//					
//					marker = new IntervalMarker(value - halfDay, value + halfDay);
//					marker.setPaint(new Color(0, 0, 0, 8));
//					marker.setOutlinePaint(new Color(0, 0, 0, 0));
//					xPlot.addDomainMarker(marker, Layer.BACKGROUND);
//					
//					System.err.println(mouse + "\t" + value + "\t" + new Date(value));
//				}
//				
//				@Override
//				public void mouseDragged(MouseEvent arg0) {
//				}
//			});
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private TimePeriodValues createDataset(GraphElement element) {
		Calendar calendar = TimeUtils.convertToCalendar(TimeUtils.getServerTime());
		TimePeriodValues dataset = new TimePeriodValues(element.getName() + "   ");
		for (int i = 0; i < period; i++) {
			dataset.add(new TimePeriodValue(new Day(calendar.getTime()), element.getValue(i)));
			calendar.add(Calendar.DAY_OF_YEAR, -1);
		}
		return dataset;
	}

	public void setPeriod(Integer value) {
		period = value;
	}
	
}
