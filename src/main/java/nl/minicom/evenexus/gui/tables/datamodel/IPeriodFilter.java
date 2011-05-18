package nl.minicom.evenexus.gui.tables.datamodel;

public interface IPeriodFilter {

	public static int DAY = 1;
	public static int WEEK = 7;
	public static int TWO_WEEKS = 14;
	public static int THREE_WEEKS = 21;
	public static int FOUR_WEEKS = 28;
	public static int EIGHT_WEEKS = 56;
	
	public void setPeriod(int days);
	
}
