package nl.minicom.evenexus.eveapi.exceptions;


public class HttpRequestException extends WarnableException {

	private static final long serialVersionUID = -1832444191966010169L;

	public HttpRequestException(String message) {
		super(message);
	}

	@Override
	public String createWarningMessage() {
		return "Could not connect to the server. Please check your firewall, proxy, and connection!";
	}

}
