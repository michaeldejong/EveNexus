package nl.minicom.evenexus.persistence.versioning;

import org.hibernate.Session;

public interface IRevision {

	void execute(Session session);
	
	int getRevisionNumber();
	
}
