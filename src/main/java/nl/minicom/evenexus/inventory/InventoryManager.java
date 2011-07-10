package nl.minicom.evenexus.inventory;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.Session;

@Singleton
public class InventoryManager {
	
	private final Database database;
	private final Provider<InventoryWorker> workerProvider;
	
	@Inject
	public InventoryManager(Provider<InventoryWorker> workerProvider, Database database) {
		this.workerProvider = workerProvider;
		this.database = database;
	}	
	
	public void initialize() {
		createAndStartWorkers(database);
	}

	private void createAndStartWorkers(Database database) {
		List<BigInteger> typeIds = queryUnprocessedTypeIds();
		for (BigInteger typeId : typeIds) {
			InventoryWorker worker = workerProvider.get();
			worker.initialize(typeId.longValue());
			new Thread(worker).start();
		}
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	private List<BigInteger> queryUnprocessedTypeIds() {
		Session session = database.getCurrentSession();
		String query = "SELECT DISTINCT(typeID) FROM transactions WHERE price > 0 AND remaining > 0";
		return (List<BigInteger>) session.createSQLQuery(query).list();
	}

}
