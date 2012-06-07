package nl.minicom.evenexus.persistence;

import javax.inject.Singleton;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * This class is responsible for pooling a {@link Session} for every {@link Thread}.
 * 
 * @author michael
 */
@Singleton
public class Database {

	private final ThreadLocal<Session> sessions;
	private SessionFactory sessionFactory = null;
	
	/**
	 * This contructs a new {@link Database} object.
	 */
	public Database() {
		this.sessions = new ThreadLocal<Session>();
	}
	
	@SuppressWarnings("deprecation")
	private void ensureInitialized() {
		if (sessionFactory == null) {
			Configuration config = new Configuration();
			config.configure("hibernate.cfg.xml");
			this.sessionFactory = config.buildSessionFactory();
		}
	}
	
	/**
	 * @return
	 * 		The current open {@link Session} of the calling {@link Thread}. 
	 * 		If no (open) {@link Session} has been associated with this {@link Thread}, a new one is openend.
	 */
	public Session getCurrentSession() {
		synchronized (this) {
			ensureInitialized();
			Session session = sessions.get();
			if (session == null || !session.isOpen()) {
				session = sessionFactory.openSession();
				sessions.set(session);
			}
			
			return session;
		}
	}
	
	/**
	 * This method closes the calling {@link Thread}'s {@link Session}.
	 */
	public void closeCurrentSession() {
		synchronized (this) {
			Session session = sessions.get();
			if (session != null) {
				if (session.isOpen()) {
					session.close();
				}
				sessions.remove();
			}
		}
	}
	
}