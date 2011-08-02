package nl.minicom.evenexus.persistence.interceptor;

import javax.inject.Inject;

import nl.minicom.evenexus.persistence.Database;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionalInterceptor implements MethodInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(TransactionalInterceptor.class);
	
	private final Database database;
	
	@Inject
	public TransactionalInterceptor(Database database) {
		this.database = database;
	}
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Session session = database.getCurrentSession();
		
		Object returnValue = null;
		Transaction tx = null;
		boolean alreadyInTransaction = false;
		
		try {
			tx = session.getTransaction();
			alreadyInTransaction = tx.isActive();
			if (!alreadyInTransaction) {
				tx = session.beginTransaction();
			}
			
			returnValue = invocation.proceed();
			
			if (session.isDirty()) {
				session.flush();
			}
			
			if (!alreadyInTransaction) {
				tx.commit();
			}
		}
		catch (HibernateException e) {
			LOG.error(e.getLocalizedMessage(), e);
			if (tx != null && !alreadyInTransaction) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			if (tx.wasCommitted() && !alreadyInTransaction) {
				database.closeCurrentSession();
			}
		}
		
		return returnValue;
	}
}
