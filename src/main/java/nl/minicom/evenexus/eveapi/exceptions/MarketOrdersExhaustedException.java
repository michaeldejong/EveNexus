package nl.minicom.evenexus.eveapi.exceptions;


public class MarketOrdersExhaustedException extends WarnableException {

	private static final long serialVersionUID = -1832444191966010169L;

	public MarketOrdersExhaustedException() {
		super();
	}

	@Override
	public String createWarningMessage() {
		return "Market orders have been fetched already.";
	}

}
