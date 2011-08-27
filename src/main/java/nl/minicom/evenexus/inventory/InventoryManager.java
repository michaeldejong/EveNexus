package nl.minicom.evenexus.inventory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.importers.ImportListener;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.gui.panels.profit.ProfitPanel;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class InventoryManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(InventoryManager.class);
	
	private final Database database;
	private final Provider<InventoryWorker> workerProvider;
	private final ThreadPoolExecutor executor;
	private final ProfitPanel profitPanel;
	
	@Inject
	public InventoryManager(ImportManager importManager, 
			Provider<InventoryWorker> workerProvider, 
			ProfitPanel profitPanel, Database database) {
		
		this.profitPanel = profitPanel;
		this.workerProvider = workerProvider;
		this.database = database;
		this.executor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(256, true));
		
		executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				LOG.warn("InventoryManager did not accept new runnable job!");
			}
		});
		
		importManager.addListener(Api.CHAR_WALLET_TRANSACTIONS, new ImportListener() {
			@Override
			public void onImportComplete() {
				processUnprocessedTransactions();
			}
		});
	}	
	
	public void processUnprocessedTransactions() {
		Collection<Future<?>> futures = new LinkedList<Future<?>>();
		List<Number> typeIds = queryUnprocessedTypeIds();
		LOG.info("Calculating inventory and profits for " + typeIds.size() + " unprocessed transaction(s)");

		for (Number typeId : typeIds) {
			InventoryWorker worker = workerProvider.get();
			worker.initialize(typeId.longValue());
			Future<?> future = executor.submit(worker);
			futures.add(future);
		}
		
		for (Future<?> future : futures) {
			try {
				future.get();
			} catch (Exception e) {
				// Do nothing.
			}
		}
		
		if (!typeIds.isEmpty()) {
			profitPanel.reloadTab();
		}
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	protected List<Number> queryUnprocessedTypeIds() {
		Session session = database.getCurrentSession();
		String query = "select distinct(t.typeId) from WalletTransaction t where t.remaining > 0";
		return (List<Number>) session.createQuery(query).list();
	}

}
