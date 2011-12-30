package nl.minicom.evenexus.eveapi.importers;


import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.importers.implementations.RefTypeImporter;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.interceptor.Transactional;
import nl.minicom.evenexus.utils.TimeUtils;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * This class is responsible for scheduling all importers.
 *
 * @author michael
 */
@Singleton
public class ImportManager extends Timer {
	
	private static final Logger LOG = LoggerFactory.getLogger(ImportManager.class);
	
	private final Database database;
	private final Provider<RefTypeImporter> refTypeImporterProvider;
	private final Provider<CharacterImporter> characterImporterProvider;
	private final Multimap<Api, ImportListener> listeners;
	
	/**
	 * This constructs a new {@link ImportManager} object.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 * 
	 * @param refTypeImporterProvider
	 * 		The {@link Provider} providing {@link RefTypeImporter}s.
	 * 
	 * @param characterImporterProvider
	 * 		The {@link Provider} providing {@link CharacterImporter}s.
	 */
	@Inject
	public ImportManager(Database database,
			Provider<RefTypeImporter> refTypeImporterProvider, 
			Provider<CharacterImporter> characterImporterProvider) {
		
		this.database = database;
		this.refTypeImporterProvider = refTypeImporterProvider;
		this.characterImporterProvider = characterImporterProvider;
		this.listeners = HashMultimap.create();
	}
	
	/**
	 * This method initializes the {@link ImportManager}.
	 */
	public void initialize() {
		synchronized (this) {
			createCharacterImporters();
			createGeneralImporters();
		}
	}
	
	private void createGeneralImporters() {
		scheduleImporter(refTypeImporterProvider.get());
	}
	
	private void scheduleImporter(ImporterTask task) {
		long nextRun = task.getNextRun(0) + 5000;
		if (nextRun < TimeUtils.getServerTime()) {
			nextRun = TimeUtils.getServerTime() + 5000;
		}
		
		long importerId = task.getApi().getImporterId();
		scheduleAtFixedRate(task, nextRun - TimeUtils.getServerTime(), task.getImporter(importerId).getCooldown());
		LOG.info("Scheduling " + task.getImporter(importerId).getName() + " importer at: " + new Date(nextRun));
	}

	/**
	 * This method creates the character importers.
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	protected void createCharacterImporters() {
		synchronized (this) {
			Session session = database.getCurrentSession();
			List<ApiKey> apiKeys = session.createCriteria(ApiKey.class).list();
			
			for (ApiKey apiKey : apiKeys) {
				addCharacterImporter(apiKey);
			}
		}
	}

	/**
	 * This method adds a new character importer for a specified {@link ApiKey}.
	 * 
	 * @param apiKey
	 * 		THe {@link ApiKey} to use.
	 */
	public void addCharacterImporter(ApiKey apiKey) {
		synchronized (this) {
			CharacterImporter importer = characterImporterProvider.get();
			importer.scheduleApiImporters(apiKey);
		}
	}

	/**
	 * This method adds a new {@link ImportListener}.
	 * 
	 * @param api
	 * 		The {@link Api} to listen to.
	 * 
	 * @param listener
	 * 		The {@link ImportListener} to notify.
	 */
	public void addListener(Api api, ImportListener listener) {
		synchronized (this) {
			listeners.put(api, listener);
		}
	}

	/**
	 * This method notifies the {@link ImportListener}s that a certain {@link Api} importer
	 * has finished its import.
	 * 
	 * @param api
	 * 		The {@link Api} to which finished.
	 */
	protected void triggerImportCompleteEvent(Api api) {
		synchronized (this) {
			for (final ImportListener listener : listeners.get(api)) {
				Runnable runner = new Runnable() {
					@Override
					public void run() {
						listener.onImportComplete();
					}
				};
				
				new Thread(runner).start();
			}
		}
	}
	
}
