package nl.minicom.evenexus.eveapi.exceptions;

/**
 * This {@link WarnableException} can be thrown when the MarketOrders API has been 
 * requested too often.
 *
 * @author michael
 */
public class MarketOrdersExhaustedException extends WarnableException {

	private static final long serialVersionUID = -1832444191966010169L;

	@Override
	public String createWarningMessage() {
		return "Market orders have been fetched already.";
	}

}
