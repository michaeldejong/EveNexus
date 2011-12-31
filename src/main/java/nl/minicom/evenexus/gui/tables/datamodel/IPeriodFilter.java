package nl.minicom.evenexus.gui.tables.datamodel;

/**
 * This interface allows us to set the period on an object.
 * 
 * @author michael
 */
public interface IPeriodFilter {

	int DAY = 1;
	int WEEK = 7;
	int TWO_WEEKS = 14;
	int THREE_WEEKS = 21;
	int FOUR_WEEKS = 28;
	int EIGHT_WEEKS = 56;
	
	/**
	 * This method sets the period of an object.
	 * 
	 * @param days
	 * 		The amount of days in the past.
	 */
	void setPeriod(int days);
	
}
