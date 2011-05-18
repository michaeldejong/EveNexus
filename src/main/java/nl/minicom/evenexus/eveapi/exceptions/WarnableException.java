package nl.minicom.evenexus.eveapi.exceptions;


public abstract class WarnableException extends Exception {
	
	private static final long serialVersionUID = 5417057318826936434L;

	public WarnableException() {
		this(null);
	}

	public WarnableException(String message) {
		super(message);
	}

	public abstract String createWarningMessage();
	
}
