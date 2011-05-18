package nl.minicom.evenexus.eveapi.exceptions;


public class ApiFailureException extends WarnableException {

	private static final long serialVersionUID = 8513464147390418610L;

	public ApiFailureException(String message) {
		super(message);
	}

	@Override
	public String createWarningMessage() {
		return getMessage();
	}

}
