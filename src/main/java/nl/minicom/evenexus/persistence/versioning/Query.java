package nl.minicom.evenexus.persistence.versioning;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for allowing the developer to define a {@link PreparedStatement}, 
 * and process the resulting {@link ResultSet} object.
 * 
 * @author michael
 */
public abstract class Query {
	
	private static final Logger LOG = LoggerFactory.getLogger(Query.class);
	
	/**
	 * This constructs and immediately executes and processes a query defined in the abstract methods of this class.
	 * 
	 * @param conn
	 * 		The {@link Connection}. 
	 *  
	 * @throws SQLException
	 * 		If the database could not execute our query.
	 */
	public Query(Connection conn) throws SQLException {
		PreparedStatement statement = null;
		try {
			statement = createStatement(conn);
			processResultSet(statement.executeQuery());
		}
		catch (SQLException e) {
			LOG.error(e.getLocalizedMessage(), e);
			throw e;
		}
		finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
	
	/**
	 * This method constructs the {@link PreparedStatement} used to query the database.
	 * 
	 * @param conn
	 * 		The {@link Connection}. 
	 * 
	 * @return
	 * 		The created {@link PreparedStatement}.
	 * 
	 * @throws SQLException
	 * 		If the query could not be executed.
	 */
	public abstract PreparedStatement createStatement(Connection conn) throws SQLException;
	
	/**
	 * This method processes the resulting {@link ResultSet}.
	 * 
	 * @param resultSet
	 * 		The resulting {@link ResultSet}.
	 * 
	 * @throws SQLException
	 * 		If an exception occurred while processing the {@link ResultSet}.
	 */
	public abstract void processResultSet(ResultSet resultSet) throws SQLException;

}
