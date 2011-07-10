package nl.minicom.evenexus.core;

import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.inventory.InventoryManager;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.interceptor.Transactional;
import nl.minicom.evenexus.persistence.interceptor.TransactionalInterceptor;
import nl.minicom.evenexus.persistence.versioning.RevisionExecutor;
import nl.minicom.evenexus.utils.ProxyManager;
import nl.minicom.evenexus.utils.SettingsManager;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class ApplicationModule extends AbstractModule {

	@Override
	protected void configure() {
		Database database = new Database();
		bind(Database.class).toInstance(database);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), 
				new TransactionalInterceptor(database));
		
		bind(Application.class).asEagerSingleton();
		bind(SettingsManager.class).asEagerSingleton();
		bind(InventoryManager.class).asEagerSingleton();
		bind(RevisionExecutor.class).asEagerSingleton();
		bind(ProxyManager.class).asEagerSingleton();;
		bind(ImportManager.class).asEagerSingleton();
		bind(Gui.class).asEagerSingleton();
	}

}
