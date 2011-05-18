package nl.minicom.evenexus.eveapi.exceptions;


public class SecurityNotHighEnoughException extends WarnableException {

	private static final long serialVersionUID = 1692558899048580936L;
	
	public SecurityNotHighEnoughException() {
		super();
	}

	@Override
	public String createWarningMessage() {
		return "You are using the limited API. EveNexus requires your full API in order to function.";
	}

}
