package nl.minicom.evenexus.persistence;

import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.ImportLog;
import nl.minicom.evenexus.persistence.dao.Importer;
import nl.minicom.evenexus.persistence.dao.Item;
import nl.minicom.evenexus.persistence.dao.MapRegion;
import nl.minicom.evenexus.persistence.dao.MarketOrder;
import nl.minicom.evenexus.persistence.dao.Profit;
import nl.minicom.evenexus.persistence.dao.RefType;
import nl.minicom.evenexus.persistence.dao.Skill;
import nl.minicom.evenexus.persistence.dao.Standing;
import nl.minicom.evenexus.persistence.dao.Station;
import nl.minicom.evenexus.persistence.dao.Version;
import nl.minicom.evenexus.persistence.dao.WalletJournal;
import nl.minicom.evenexus.persistence.dao.WalletTransaction;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class Database {
	
	private static SessionFactory factory;
	
	public static final Session createNewSession() {
		if (factory == null) {
			factory = createSessionFactory();
		}
		
		return factory.openSession();
	}
	
	private static final SessionFactory createSessionFactory() {
		AnnotationConfiguration config = null;
		
		try {
			config = new AnnotationConfiguration();
			config.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
			config.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
			config.setProperty("hibernate.connection.url", "jdbc:h2:database/database");
			config.setProperty("hibernate.connection.username", "root");
			config.setProperty("hibernate.connection.password", "");
			config.setProperty("hibernate.connection.pool_size", "1");
			config.setProperty("hibernate.connection.autocommit", "true");
			config.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
			config.setProperty("hibernate.cache.use_second_level_cache", "false");
			config.setProperty("hibernate.cache.use_query_cache", "false");
			config.setProperty("hibernate.hbm2ddl.auto", "");
			config.setProperty("hibernate.show_sql", "true");
			config.setProperty("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");
			config.setProperty("hibernate.current_session_context_class", "thread");

			// Add your mapped classes here:
			config.addAnnotatedClass(ApiKey.class);
			config.addAnnotatedClass(Version.class);
			config.addAnnotatedClass(Importer.class);
			config.addAnnotatedClass(ImportLog.class);
			config.addAnnotatedClass(Item.class);
			config.addAnnotatedClass(MapRegion.class);
			config.addAnnotatedClass(MarketOrder.class);
			config.addAnnotatedClass(Profit.class);
			config.addAnnotatedClass(RefType.class);
			config.addAnnotatedClass(Skill.class);
			config.addAnnotatedClass(Standing.class);
			config.addAnnotatedClass(Station.class);
			config.addAnnotatedClass(WalletJournal.class);
			config.addAnnotatedClass(WalletTransaction.class);
			
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
		
		return config.buildSessionFactory();
	}
	
}