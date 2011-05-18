package nl.minicom.evenexus.core.report.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 * The {@link QueryBuilder} class can be used to create new SQL queries.
 *
 * @author Michael
 */
public class QueryBuilder {

	private StringBuilder builder;
	private List<Object> parameters;
	
	/**
	 * This constructor creates a new {@link QueryBuilder} object.
	 */
	public QueryBuilder() {
		clear();
	}
	
	/**
	 * Clears this {@link QueryBuilder}'s parameters and query.
	 */
	public final void clear() {
		this.builder = new StringBuilder();
		this.parameters = new ArrayList<Object>();
	}

	/**
	 * Adds a value for a parameter.
	 * 
	 * @param value		The value to set a parameter.
	 */
	public void addParameter(Object value) {
		parameters.add(value);
	}
	
	/**
	 * Appends a new {@link String} part to the {@link QueryBuilder}.
	 * 
	 * @param part		The {@link String} part to append.
	 */
	public void append(String part) {
		builder.append(part.trim() + " ");
	}
	
	/**
	 * Creates a new {@link SQLQuery} from this {@link QueryBuilder}.
	 * 
	 * @param session				A reference to the database connection.
	 * @return						A {@link SQLQuery} created from this {@link QueryBuilder}.
	 * @throws HibernateException 	Will be thrown if the query is invalid.
	 */
	public SQLQuery createStatement(Session session) throws HibernateException {
		String query = builder.toString().trim();
		SQLQuery statement = session.createSQLQuery(query);
		for (int i = 0; i < parameters.size(); i++) {
			statement.setParameter(i, parameters.get(i));
		}
		return statement;
	}
	
}
