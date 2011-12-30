package nl.minicom.evenexus.eveapi.exceptions;

/**
 * This {@link WarnableException} can be thrown when EveNexus could not connect to
 * the API server.
 *
 * @author michael
 */
public class HttpRequestException extends WarnableException {

	private static final long serialVersionUID = -1832444191966010169L;

	/**
	 * This constructs a new {@link HttpRequestException}.
	 * 
	 * @param message
	 * 		The message of the {@link HttpRequestException}.
	 */
	public HttpRequestException(String message) {
		super(message);
	}

	@Override
	public String createWarningMessage() {
		return "Could not connect to the server. Please check your firewall, proxy, and connection!";
	}

}
