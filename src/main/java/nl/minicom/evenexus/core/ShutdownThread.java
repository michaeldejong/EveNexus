package nl.minicom.evenexus.core;


import nl.minicom.evenexus.utils.SettingsManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class ShutdownThread extends Thread {
	
	private static final Logger logger = LogManager.getRootLogger();
	
	private final SettingsManager settingsManager;
	
	public ShutdownThread(SettingsManager settingsManager) {
		super();
		this.settingsManager = settingsManager;
	}

	@Override
	public void run() {
		try {
			//Save all gui related information.
			settingsManager.save();
			
			//Close the database.
			closeDatabase();
			
			//Release all resources held by Appenders.
			logger.removeAllAppenders();
			
			//Shut down the logger.
			LogManager.shutdown();
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private void closeDatabase() {
//		Session session = Database.createNewSession();
//		Transaction transaction = null;
//		try {
//			transaction = session.beginTransaction();
//			session.createSQLQuery("SHUTDOWN").executeUpdate();
//			transaction.commit();
//		}
//		catch (HibernateException e) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
//			throw e;
//		}
//		finally {
//			session.close();
//		}
	}
		
}
