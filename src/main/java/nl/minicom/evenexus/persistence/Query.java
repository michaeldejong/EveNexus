package nl.minicom.evenexus.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class Query<T> {
	
	private final Database database;
	
	public Query(Database database) {
		this.database = database;
	}
	
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
	
	protected abstract T doQuery(Session session);

}
