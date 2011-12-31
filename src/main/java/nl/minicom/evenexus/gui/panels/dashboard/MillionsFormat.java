package nl.minicom.evenexus.gui.panels.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * This formatter formats {@link BigDecimal}s to millions.
 * 
 * @author michael
 */
public class MillionsFormat extends NumberFormat {

	private static final long serialVersionUID = -7321220259544291956L;

	@Override
	public StringBuffer format(double arg0, StringBuffer arg1, FieldPosition arg2) {
		BigDecimal value = BigDecimal.valueOf(arg0).divide(BigDecimal.valueOf(1000000));
		value = value.setScale(2, RoundingMode.HALF_UP);
		DecimalFormat format = new DecimalFormat();
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
		StringBuffer buffer = new StringBuffer();
		buffer.append(format.format(value.doubleValue()));
		return buffer;
	}

	@Override
	public StringBuffer format(long arg0, StringBuffer arg1, FieldPosition arg2) {
		return null;
	}

	@Override
	public Number parse(String arg0, ParsePosition arg1) {
		return null;
	}

	
}
