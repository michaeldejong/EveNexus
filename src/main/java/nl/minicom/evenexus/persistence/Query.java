package nl.minicom.evenexus.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * This abstract class allows the programmer to execute an SQL query with a return value.
 * 
 * @author michael
 *
 * @param <T>
 */
public abstract class Query<T> {
	
	private final Database database;
	
	/**
	 * This constructs a new {@link Query} object.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 */
	public Query(Database database) {
		this.database = database;
	}
	
	/**
	 * This method executes the query.
	 * 
	 * @return
	 * 		The result of the query.
	 */
	public T doQuery() {
		T result = null;
		Session session = database.getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			result = doQuery(session);
			if (session.isDirty()) {
				session.flush();
			}
			tx.commit();
		}
		catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			database.closeCurrentSession();
		}
		
		return result;
	}
	
	/**
	 * This method is called to do the actual query.
	 * 
	 * @param session
	 * 		The {@link Session} required to talk to the database.
	 * 
	 * @return
	 * 		The constructed value.
	 */
	protected abstract T doQuery(Session session);

}
