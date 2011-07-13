package nl.minicom.evenexus.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Singleton
public class Database {

	private final Map<Thread, Session> sessionMapping;
	private SessionFactory sessionFactory = null;
	
	public Database() {
		this.sessionMapping = new HashMap<Thread, Session>();
	}
	
	private void ensureInitialized() {
		if (sessionFactory == null) {
			Configuration config = new Configuration();
			config.configure("hibernate.cfg.xml");
			this.sessionFactory = config.buildSessionFactory();
		}
	}
	
	public Session getCurrentSession() {
		ensureInitialized();
		Session session = sessionMapping.get(Thread.currentThread());
		if (session == null || !session.isOpen()) {
			session = sessionFactory.openSession();
			sessionMapping.put(Thread.currentThread(), session);
		}
		
		return session;
	}
	
	public void closeCurrentSession() {
		Session session = sessionMapping.get(Thread.currentThread());
		if (session != null) {
			if (session.isOpen()) {
				session.close();
			}
			sessionMapping.remove(Thread.currentThread());
		}
	}
	
}