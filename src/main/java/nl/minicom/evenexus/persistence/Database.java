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
	private final SessionFactory sessionFactory;
	
	public Database() {
		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		this.sessionMapping = new HashMap<Thread, Session>();
		this.sessionFactory = config.buildSessionFactory();
	}
	
	public Session getCurrentSession() {
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