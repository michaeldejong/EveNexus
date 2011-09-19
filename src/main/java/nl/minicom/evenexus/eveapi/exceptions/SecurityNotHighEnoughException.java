package nl.minicom.evenexus.eveapi.exceptions;


public class SecurityNotHighEnoughException extends WarnableException {

	private static final long serialVersionUID = 1692558899048580936L;
	
	@Override
	public String createWarningMessage() {
		return "You are using an API key which does not support all of the following APIs: WalletTransactions, WalletJournals, MarketOrders, CharacterSheet and Standings.";
	}

}
