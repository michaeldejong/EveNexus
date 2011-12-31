package nl.minicom.evenexus.persistence.versioning;

import org.hibernate.Session;

/**
 * This interface defines the most basic database revision which can be executed.
 * 
 * @author michael
 */
public interface IRevision {

	/**
	 * The execute method. This method is called when the revision has not yet been executed.
	 * 
	 * @param session
	 * 		The {@link Session} to use, when communicating to the database.
	 */
	void execute(Session session);
	
	/**
	 * @return
	 * 		The revision number.
	 */
	int getRevisionNumber();
	
}
