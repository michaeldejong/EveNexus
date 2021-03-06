package nl.minicom.evenexus.gui.tables.renderers;

import java.awt.Component;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.NumberFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IntegerRenderer extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = -4703532359023302661L;

	private static final Logger LOG = LoggerFactory.getLogger(IntegerRenderer.class);
	
	private static final String FORMAT = "###,###,###,###,###,##0";
	private static final DecimalFormatSymbols SYMBOLS = DecimalFormatSymbols.getInstance(Locale.US);
	private static final NumberFormatter FORMATTER = new NumberFormatter(new DecimalFormat(FORMAT, SYMBOLS));

	/**
	 * This constructs a new {@link IntegerRenderer} object.
	 */
	public IntegerRenderer() {
		setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
	}
	
	@Override
	public String getText() {
		return " " + super.getText() + " ";
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, 
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (!(value instanceof Number)) {
			return null;
		}
		
		Long longValue = null;
		if (value instanceof BigDecimal) {
			longValue = ((BigDecimal) value).longValue();
		}
		else if (value instanceof BigInteger) {
			longValue = ((BigInteger) value).longValue();
		}
		else {
			longValue = (Long) value;
		}
		
		try {
			setValue(FORMATTER.valueToString(longValue));
		}
		catch (ParseException e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
		
		return c;
	}
}
