package nl.minicom.evenexus.gui.tables.datamodel;

public interface IPeriodFilter {

	int DAY = 1;
	int WEEK = 7;
	int TWO_WEEKS = 14;
	int THREE_WEEKS = 21;
	int FOUR_WEEKS = 28;
	int EIGHT_WEEKS = 56;
	
	void setPeriod(int days);
	
}
