package nl.minicom.evenexus.gui.tables.formatters;

import java.sql.Timestamp;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import nl.minicom.evenexus.utils.TimeUtils;

/**
 * This class lets us format date classes.
 * 
 * @author michael
 */
public class DateTimeFormatter extends AbstractFormatter {

	private static final long serialVersionUID = -1208977482711815877L;

	@Override
	public Object stringToValue(String arg0) throws ParseException {
		return null;
	}

	@Override
	public String valueToString(Object arg0) throws ParseException {
		if (arg0 instanceof Integer) {
			return TimeUtils.convertToDate((Integer) arg0);
		}
		else if (arg0 instanceof Long) {
			return TimeUtils.convertToDate((Long) arg0);
		}
		else if (arg0 instanceof Timestamp) {
			return TimeUtils.convertToDate(((Timestamp) arg0).getTime());
		}
		return arg0.toString();
	}

	
}
