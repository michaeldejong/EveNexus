package nl.minicom.evenexus.eveapi.exceptions;


/**
 * This {@link WarnableException} is thrown when the provided API key does not have enough clearance.
 * 
 * @author michael
 */
public class SecurityNotHighEnoughException extends WarnableException {

	private static final long serialVersionUID = 1692558899048580936L;
	
	@Override
	public String createWarningMessage() {
		return new StringBuilder()
		.append("You are using an API key which does not support all of the following APIs: ")
		.append("WalletTransactions, WalletJournals, MarketOrders, CharacterSheet and Standings.")
		.toString();
	}

}
