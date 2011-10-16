package nl.minicom.evenexus.persistence.versioning;

import java.sql.Connection;
import java.sql.SQLException;

public interface IRevision {

	void execute(Connection conn) throws SQLException;
	
	int getRevisionNumber();
	
}
