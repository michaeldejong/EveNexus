package nl.minicom.evenexus.eveapi.exceptions;

/**
 * This {@link WarnableException} can be thrown when the Journal API has been 
 * requested too often.
 *
 * @author michael
 */
public class JournalsExhaustedException extends WarnableException {

	private static final long serialVersionUID = -5665931045401601980L;

	@Override
	public String createWarningMessage() {
		return "Journal entries have been fetched already.";
	}
	
}
