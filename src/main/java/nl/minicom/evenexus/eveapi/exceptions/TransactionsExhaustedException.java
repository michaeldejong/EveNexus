package nl.minicom.evenexus.eveapi.exceptions;


public class TransactionsExhaustedException extends WarnableException {

	private static final long serialVersionUID = -1852057292315501651L;

	@Override
	public String createWarningMessage() {
		return "Transactions have been fetched already.";
	}

}
