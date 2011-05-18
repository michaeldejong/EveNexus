package nl.minicom.evenexus.persistence.versioning;

import org.hibernate.Session;

public interface IRevision {

	public void execute(Session session);
	
	public int getRevisionNumber();
	
}
