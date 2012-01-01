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

/**
 * This class can be used to start the application.
 * 
 * @author michael
 */
@Singleton
public class Application {
	
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	private final Database database;
	private final SettingsManager settingsManager;
	private final InventoryManager inventoryManager;
	private final RevisionExecutor revisionExecutor;
	private final Translator translator;
	private final ProxyManager proxyManager;
	private final ImportManager importManager;
	private final Gui gui;
	
	private boolean initialized = false;
	
	/**
	 * This constructs a new {@link Application} object.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 * 
	 * @param settingsManager
	 * 		The {@link SettingsManager}.
	 * 
	 * @param inventoryManager
	 * 		The {@link InventoryManager}.
	 * 
	 * @param revisionExecutor
	 * 		The {@link RevisionExecutor}.
	 * 
	 * @param translator
	 * 		The {@link Translator}.
	 * 
	 * @param proxyManager
	 * 		The {@link ProxyManager}.
	 * 
	 * @param importManager
	 * 		The {@link ImportManager}.
	 * 
	 * @param gui
	 * 		The {@link Gui}.
	 */
	@Inject
	public Application(Database database, SettingsManager settingsManager, InventoryManager inventoryManager,
			RevisionExecutor revisionExecutor, Translator translator, ProxyManager proxyManager, 
			ImportManager importManager, Gui gui) {
		
		this.database = database;
		this.settingsManager = settingsManager;
		this.inventoryManager = inventoryManager;
		this.revisionExecutor = revisionExecutor;
		this.translator = translator;
		this.proxyManager = proxyManager;
		this.importManager = importManager;
		this.gui = gui;
	}
	
	/**
	 * This method initializes the {@link Application} object.
	 * 
	 * @param progressManager
	 * 		A {@link ProgressManager} to keep track of the progress while initializing.
	 * 
	 * @param args
	 * 		Additional arguments if required.
	 * 
	 * @throws Exception
	 * 		If the {@link Application} could not be initialized.
	 */
	public void initialize(final ProgressManager progressManager, String[] args) throws Exception {
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

	/**
	 * This method initializes the GUI.
	 */
	public void initializeGui() {
		gui.initialize();
	}

	/**
	 * @return
	 * 		The version of the database structure.
	 */
	public String getDatabaseVersion() {
		return getVersion("database");
	}
	
	/**
	 * @return
	 * 		The version of the content in the database.
	 */
	public String getContentVersion() {
		return getVersion("content");
	}
	
	/**
	 * This method returns the current version of the specified type.
	 * 
	 * @param type
	 * 		The type of resource to look for.
	 * 
	 * @return
	 * 		The version of the resource, or "unknown" if no version is known.
	 */
	@Transactional
	String getVersion(String type) {
		Preconditions.checkArgument(initialized, "This class has not yet been initialized!");
		
		Session session = database.getCurrentSession();
		Version currentVersion = (Version) session.get(Version.class, type);
		
		if (currentVersion != null) {
			return currentVersion.getVersion() + "." + currentVersion.getRevision();
		}
		return "unknown";
	}
	
}