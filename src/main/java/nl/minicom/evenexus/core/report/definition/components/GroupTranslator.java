package nl.minicom.evenexus.core.report.definition.components;

/**
 * This interface ensures a {@link ReportGroup}'s value can be translated to
 * a more user friendly category name for the {@link ReportGroup}.
 *
 * @author Michael
 */
public interface GroupTranslator {
	
	/**
	 * This method is responsible for translating the value of a {@link ReportGroup}
	 * into a more readable category name.
	 * 
	 * @param input		The value to translate.
	 * @return			The translated value.
	 */
	String translate(String input);

}
