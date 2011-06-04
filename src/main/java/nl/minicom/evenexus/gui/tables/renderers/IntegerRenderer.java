package nl.minicom.evenexus.gui.tables.renderers;

import java.awt.Component;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.NumberFormatter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class IntegerRenderer extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = -4703532359023302661L;

	private static final Logger logger = LogManager.getRootLogger();
	
	private AbstractFormatter formatter = new NumberFormatter(new DecimalFormat("###,###,###,###,###,##0", DecimalFormatSymbols.getInstance(Locale.US)));

	public IntegerRenderer() {
		super();
		setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
	}
	
	@Override
	public String getText() {
		return(" " + super.getText() + " ");
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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
			setValue(formatter.valueToString(longValue));
		}
		catch (ParseException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		
		return c;
	}
}
