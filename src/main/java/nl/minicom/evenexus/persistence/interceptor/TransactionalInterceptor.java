package nl.minicom.evenexus.persistence.interceptor;

import nl.minicom.evenexus.persistence.Database;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@link MethodInterceptor} intercepts methods and prepares a {@link Session} object for them.
 * After exiting the method, it will automatically close the {@link Session}.
 * 
 * @author michael
 */
public class TransactionalInterceptor implements MethodInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(TransactionalInterceptor.class);
	
	private final Database database;
	
	/**
	 * This constructs a new {@link TransactionalInterceptor} object.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 */
	public TransactionalInterceptor(Database database) {
		this.database = database;
	}
	
	/**
	 * This method is called, when the target method is intercepted.
	 * 
	 * @param invocation
	 * 		The target method which was invoked.
	 * 
	 * @return
	 * 		An object which will be returned by the target method.
	 * 
	 * @throws Throwable
	 * 		If something went wrong during the intercept.
	 */
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
