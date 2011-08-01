package nl.minicom.evenexus.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtils {
		
	/**
	 * Returns a time stamp in seconds resembling Reykjavic time ie. GMT+0:00 
	 * and no Day Light Savings time, no matter what time zone you are in.
	 * @return
	 */
	public static long getServerTime() {
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/London"));
		return calendar.getTime().getTime();
	}	

	/**
	 * Converts a given dateTime String (from the API) into a timestamp which 
	 * corresponds with the Tranquility server time in the United Kingdom.
	 * @param dateTime
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp convertToTimestamp(String dateTime) throws ParseException {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("Europe/London"));
		calendar.set(
				Integer.parseInt(dateTime.substring(0, 4)),
				Integer.parseInt(dateTime.substring(5, 7)) - 1,
				Integer.parseInt(dateTime.substring(8, 10)),
				Integer.parseInt(dateTime.substring(11, 13)),
				Integer.parseInt(dateTime.substring(14, 16)),
				Integer.parseInt(dateTime.substring(17, 19))
		);
		
		return new Timestamp(calendar.getTime().getTime());		
	}
	
	/**
	 * This method translates a timestamp into a date Calendar.
	 * @param timestamp
	 * @return
	 */
	public static Calendar convertToCalendar(long timestamp) {
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/London"));
		calendar.setTimeInMillis(timestamp);
		return calendar;
	}
	
	/**
	 * This method translates a timestamp into a date String.
	 * @param timestamp
	 * @return
	 */
	public static String convertToDate(long timestamp) {
		Calendar calendar = convertToCalendar(timestamp);
		calendar.setTimeZone(GregorianCalendar.getInstance().getTimeZone());
		
		StringBuilder builder = new StringBuilder();
		builder.append(twoDigit(calendar.get(Calendar.HOUR_OF_DAY)) + ":");
		builder.append(twoDigit(calendar.get(Calendar.MINUTE)) + ":");
		builder.append(twoDigit(calendar.get(Calendar.SECOND)) + " - ");
		builder.append(twoDigit(calendar.get(Calendar.DAY_OF_MONTH)) + "/");
		builder.append(twoDigit(calendar.get(Calendar.MONTH) + 1) + "/");
		builder.append(calendar.get(Calendar.YEAR));
		
		return builder.toString();
	}
	
	/**
	 * This method translates a timestamp into a date String.
	 * @param timestamp
	 * @return
	 */
	public static String convertToDate2(long timestamp) {
		Calendar calendar = convertToCalendar(timestamp);
		
		StringBuilder builder = new StringBuilder();
		builder.append(calendar.get(Calendar.YEAR) + "/");
		builder.append(twoDigit(calendar.get(Calendar.MONTH) + 1) + "/");
		builder.append(twoDigit(calendar.get(Calendar.DAY_OF_MONTH)) + "-");
		builder.append(twoDigit(calendar.get(Calendar.HOUR_OF_DAY)) + ":");
		builder.append(twoDigit(calendar.get(Calendar.MINUTE)) + ":");
		builder.append(twoDigit(calendar.get(Calendar.SECOND)));
		return builder.toString();
	}

	/**
	 * Conveniance method for formatting dates in Strings.
	 * @param i
	 * @return
	 */
	private static String twoDigit(int i) {
		if (i > 9) {
			return Integer.toString(i);
		}
		else {
			return "0" + i;
		}
	}
}
