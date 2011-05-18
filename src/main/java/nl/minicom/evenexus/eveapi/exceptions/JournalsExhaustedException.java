package nl.minicom.evenexus.eveapi.exceptions;


public class JournalsExhaustedException extends WarnableException {

	private static final long serialVersionUID = -5665931045401601980L;

	public JournalsExhaustedException() {
		super();
	}

	@Override
	public String createWarningMessage() {
		return "Journal entries have been fetched already.";
	}
	
}
