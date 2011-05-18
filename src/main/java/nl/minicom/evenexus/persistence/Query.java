package nl.minicom.evenexus.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class Query<T> {
	
	public T doQuery() {
		T result = null;
		Session session = Database.createNewSession();
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
		}
		finally {
			session.close();
		}
		
		return result;
	}
	
	protected abstract T doQuery(Session session);

}
