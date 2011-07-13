package nl.minicom.evenexus.core;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.gui.utils.progresswindows.ProgressManager;
import nl.minicom.evenexus.gui.utils.progresswindows.SplashFrame;
import nl.minicom.evenexus.inventory.InventoryManager;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.Version;
import nl.minicom.evenexus.persistence.versioning.ContentUpgrader;
import nl.minicom.evenexus.persistence.versioning.RevisionExecutor;
import nl.minicom.evenexus.persistence.versioning.StructureUpgrader;
import nl.minicom.evenexus.utils.ProxyManager;
import nl.minicom.evenexus.utils.SettingsManager;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Guice;
import com.google.inject.Injector;

@Singleton
public final class Application {
	
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	@Inject private Database database;
	@Inject private SettingsManager settingsManager;
	@Inject private InventoryManager inventoryManager;
	@Inject private RevisionExecutor revisionExecutor;
	@Inject private ProxyManager proxyManager;
	@Inject private ImportManager importManager;
	@Inject private Gui gui;
	
	private boolean initialized = false;
	
	public static void main(String[] args) throws Exception {
		SplashFrame frame = new SplashFrame();
		frame.buildGui();
		frame.setVisible(true);
		
		LOG.info("Creating Guice injector...");
		frame.update(9, 1, "Creating Guice injector...");
		Injector injector = Guice.createInjector(new ApplicationModule());
		Application application = injector.getInstance(Application.class);
		
		LOG.info("Launching application...");
		application.initialize(frame, args);
		frame.dispose();
		
		application.initializeGui();
	}
	
	public void initialize(ProgressManager progressManager, String[] args) throws Exception {
		synchronized (this) {
			Preconditions.checkArgument(!initialized, "This class has already been initialized!");
						
			// 1. Initialize program settings.
			LOG.info("Reading program settings...");
			progressManager.update(9, 2, "Reading program settings...");
			settingsManager.initialize();
			
			// 2. Preparing proxy settings.
			LOG.info("Initializing proxy settings...");
			progressManager.update(9, 3, "Initializing proxy settings...");
			proxyManager.initialize();
			
			// 3. Check database structure & upgrade.
			LOG.info("Checking database structure consistency...");
			progressManager.update(9, 4, "Checking database structure consistency");
			revisionExecutor.execute(new StructureUpgrader());
			revisionExecutor.execute(new ContentUpgrader());
			
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
	
	private void initializeGui() {
		gui.initialize();
	}
	
	public String getDatabaseVersion() {
		return getVersion("database");
	}
	
	public String getContentVersion() {
		return getVersion("content");
	}
	
	private String getVersion(String type) {
		Preconditions.checkArgument(initialized, "This class has not yet been initialized!");
		
		Session session = database.getCurrentSession();
		Version currentVersion = (Version) session.get(Version.class, type);
		
		if (currentVersion != null) {
			return currentVersion.getVersion() + "." + currentVersion.getRevision();
		}
		return "unknown";
	}
	
}