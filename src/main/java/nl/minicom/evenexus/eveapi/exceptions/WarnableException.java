package nl.minicom.evenexus.eveapi.exceptions;

/**
 * This class can be extended to throw specific {@link Exception}s which
 * can be thrown and catched more specifically.
 *
 * @author michael
 */
public abstract class WarnableException extends Exception {
	
	private static final long serialVersionUID = 5417057318826936434L;

	/**
	 * This constructs a new {@link WarnableException}.
	 */
	public WarnableException() {
		this(null);
	}

	/**
	 * This constructs a new {@link WarnableException}.
	 * 
	 * @param message
	 * 		The message of the {@link WarnableException}.
	 */
	public WarnableException(String message) {
		super(message);
	}

	/**
	 * @return
	 * 		The warning message associated with the {@link WarnableException}.
	 */
	public abstract String createWarningMessage();
	
}
