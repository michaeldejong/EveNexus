package nl.minicom.evenexus.core;

import nl.minicom.evenexus.eveapi.ApiServerManager;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.inventory.InventoryManager;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.Version;
import nl.minicom.evenexus.persistence.versioning.ContentUpgrader;
import nl.minicom.evenexus.persistence.versioning.RevisionExecutor;
import nl.minicom.evenexus.persistence.versioning.StructureUpgrader;
import nl.minicom.evenexus.utils.ProgressListener;
import nl.minicom.evenexus.utils.ProxyManager;
import nl.minicom.evenexus.utils.SettingsManager;

import org.apache.commons.lang.Validate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Application {
	
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	private boolean initialized;
	
	private SettingsManager settingsManager;
	private InventoryManager inventoryManager;
	private ProxyManager proxyManager;
	private ApiServerManager apiServerManager;
	private ImportManager importManager;
	
	public Application() {
		this.initialized = false;
	}
	
	public void initialize(ProgressListener listener, String[] args) throws Exception {
		synchronized (this) {
			Validate.isTrue(!initialized, "This class has already been initialized!");
						
			// 3. Initialize program settings.
			LOG.info("Reading program settings...");
			listener.update(12, 3, "Reading program settings...");
			settingsManager = new SettingsManager();
			
			// 4. Preparing proxy settings.
			LOG.info("Preparing proxy settings...");
			listener.update(12, 4, "Preparing proxy settings...");
			proxyManager = new ProxyManager(settingsManager);
			
			// 5. Preparing proxy settings.
			LOG.info("Preparing api server settings...");
			listener.update(12, 5, "Preparing api server settings...");
			apiServerManager = new ApiServerManager(settingsManager);
	
			// 6. Connecting to database.
			LOG.info("Connecting to internal database...");
			listener.update(12, 6, "Connecting to internal database...");
			
			// 7. Check database structure & upgrade.
			LOG.info("Checking database structure consistency...");
			listener.update(12, 7, "Checking database structure consistency...");
			new RevisionExecutor(new StructureUpgrader()).execute();
			new RevisionExecutor(new ContentUpgrader()).execute();
			
			// 8. Create inventory manager.
			LOG.info("Creating inventory manager...");
			listener.update(12, 8, "Creating inventory manager...");
			inventoryManager = new InventoryManager();
			
			// 9. Create import manager.
			LOG.info("Creating import manager...");
			listener.update(12, 9, "Creating import manager...");
			importManager = new ImportManager(apiServerManager);
			
			// 10. Initializing importers.
			LOG.info("Initializing API importers...");
			listener.update(12, 10, "Creating import manager...");
			importManager.initialize();
			
			// 11. Preparing program shutdown hook.
			LOG.info("Preparing program shutdown hook...");
			listener.update(12, 11, "Preparing program shutdown hook...");
			Runtime.getRuntime().addShutdownHook(new ShutdownThread(settingsManager));
			
			// 12. Completing initialization.
			LOG.info("Completing initialization...");
			listener.update(12, 12, "Completing initialization...");
			initialized = true;
		}
	}
	
	public InventoryManager getInventoryManager() {
		Validate.isTrue(initialized, "This class has not yet been initialized!");
		return inventoryManager;
	}
	
	public SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	public ProxyManager getProxyManager() {
		return proxyManager;
	}

	public ApiServerManager getApiServerManager() {
		return apiServerManager;
	}

	public ImportManager getImportManager() {
		return importManager;
	}

	public String getDatabaseVersion() {
		Validate.isTrue(initialized, "This class has not yet been initialized!");
		
		Version currentVersion = new Query<Version>() {
			@Override
			protected Version doQuery(Session session) {
				return (Version) session.get(Version.class, "database");
			}
		}.doQuery();
		
		if (currentVersion != null) {
			return currentVersion.getVersion() + "." + currentVersion.getRevision();
		}
		return "unknown";
	}
	
}