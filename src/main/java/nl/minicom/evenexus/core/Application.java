package nl.minicom.evenexus.core;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.gui.utils.progresswindows.ProgressManager;
import nl.minicom.evenexus.i18n.Translator;
import nl.minicom.evenexus.inventory.InventoryManager;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.Version;
import nl.minicom.evenexus.persistence.interceptor.Transactional;
import nl.minicom.evenexus.persistence.versioning.ContentUpgrader;
import nl.minicom.evenexus.persistence.versioning.RevisionExecutor;
import nl.minicom.evenexus.persistence.versioning.StructureUpgrader;
import nl.minicom.evenexus.utils.ProxyManager;
import nl.minicom.evenexus.utils.SettingsManager;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

@Singleton
public class Application {
	
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	@Inject private Database database;
	@Inject private SettingsManager settingsManager;
	@Inject private InventoryManager inventoryManager;
	@Inject private RevisionExecutor revisionExecutor;
	@Inject private Translator translator;
	@Inject private ProxyManager proxyManager;
	@Inject private ImportManager importManager;
	@Inject private Gui gui;
	
	private boolean initialized = false;
	
	public void initialize(ProgressManager progressManager, String[] args) throws Exception {
		synchronized (this) {
			Preconditions.checkArgument(!initialized, "This class has already been initialized!");
			
			// 0. Initialize program settings.
			LOG.info("Reading program settings...");
			progressManager.update(9, 1, "Reading program settings...");
			settingsManager.initialize();
			
			// 1. Establishing database connection
			LOG.info("Establishing database connection...");
			progressManager.update(9, 2, "Establishing database connection...");
			database.getCurrentSession();
			
			// 2. Preparing proxy settings.
			LOG.info("Initializing proxy settings...");
			progressManager.update(9, 3, "Initializing proxy settings...");
			proxyManager.initialize();
			
			// 3. Check database structure & upgrade.
			LOG.info("Checking database structure consistency...");
			progressManager.update(9, 4, "Checking database structure consistency");
			revisionExecutor.execute(new StructureUpgrader());
			revisionExecutor.execute(new ContentUpgrader());
			
			progressManager.update(9, 4, "Initializing ResourceBundles");
			translator.initialize(Locale.ENGLISH);
			
			// 4. Create inventory manager.
			LOG.info("Initializing inventory manager...");
			progressManager.update(9, 5, "Initializing inventory manager...");
			inventoryManager.processUnprocessedTransactions();
			
			// 5. Initializing importers.
			LOG.info("Initializing API importers...");
			progressManager.update(9, 6, "Initializing API importers...");
			importManager.initialize();
			
			// 6. Preparing program shutdown hook.
			LOG.info("Preparing program shutdown hook...");
			progressManager.update(9, 7, "Preparing program shutdown hook...");
			Runtime.getRuntime().addShutdownHook(new ShutdownThread(settingsManager));
			
			// 7. Completing initialization.
			LOG.info("Completing initialization...");
			progressManager.update(9, 8, "Completing initialization...");
			initialized = true;
		}
	}

	public void initializeGui() {
		gui.initialize();
	}
	
	public String getDatabaseVersion() {
		return getVersion("database");
	}
	
	public String getContentVersion() {
		return getVersion("content");
	}
	
	@Transactional
	protected String getVersion(String type) {
		Preconditions.checkArgument(initialized, "This class has not yet been initialized!");
		
		Session session = database.getCurrentSession();
		Version currentVersion = (Version) session.get(Version.class, type);
		
		if (currentVersion != null) {
			return currentVersion.getVersion() + "." + currentVersion.getRevision();
		}
		return "unknown";
	}
	
}