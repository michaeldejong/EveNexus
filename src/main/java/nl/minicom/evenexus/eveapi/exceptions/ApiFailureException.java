package nl.minicom.evenexus.eveapi.exceptions;

/**
 * This class is an extension off a warnable exception. And represents a failure while using the API.
 *
 * @author michael
 */
public class ApiFailureException extends WarnableException {

	private static final long serialVersionUID = 8513464147390418610L;

	/**
	 * This constructs a new {@link ApiFailureException} object.
	 * 
	 * @param message
	 * 		The message of this {@link WarnableException}.
	 */
	public ApiFailureException(String message) {
		super(message);
	}

	@Override
	public String createWarningMessage() {
		return getMessage();
	}

}
