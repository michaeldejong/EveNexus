package nl.minicom.evenexus.eveapi.importers;


import java.util.Date;
import java.util.List;
import java.util.Timer;

import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.ApiServerManager;
import nl.minicom.evenexus.eveapi.importers.implementations.RefTypeImporter;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.utils.TimeUtils;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


public class ImportManager extends Timer {
	
	private static final Logger LOG = LoggerFactory.getLogger(ImportManager.class);
	
	private final Multimap<Api, ImportListener> listeners;
	private final ApiServerManager apiServerManager;
	
	public ImportManager(ApiServerManager apiServerManager) {
		this.apiServerManager = apiServerManager;
		this.listeners = HashMultimap.create();
	}
	
	public void initialize() {
		createCharacterImporters();
		createGeneralImporters();
	}
	
	private void createGeneralImporters() {
		scheduleImporter(new RefTypeImporter(apiServerManager, this));
	}
	
	private void scheduleImporter(ImporterTask task) {
		long nextRun = task.getNextRun() + 5000;
		if (nextRun < TimeUtils.getServerTime()) {
			nextRun = TimeUtils.getServerTime() + 5000;
		}
		
		scheduleAtFixedRate(task, nextRun - TimeUtils.getServerTime(), task.getImporter().getCooldown());
		LOG.info("Scheduling " + task.getImporter().getName() + " importer at: " + new Date(nextRun));
	}

	private void createCharacterImporters() {
		List<ApiKey> apiKeys = new Query<List<ApiKey>>() {
			@Override
			protected List<ApiKey> doQuery(Session session) {
				return ApiKey.getAll(session);
			}
		}.doQuery();
		
		for (ApiKey apiKey : apiKeys) {
			addCharacterImporter(apiKey);
		}
	}

	public void addCharacterImporter(ApiKey apiKey) {
		LOG.debug("Scheduling importers for character: " + apiKey.getCharacterName());
		new CharacterImporter(apiServerManager, this, apiKey);
	}

	public void addListener(Api api, ImportListener listener) {
		listeners.put(api, listener);
	}

	protected void triggerImportCompleteEvent(Api api) {
		for (ImportListener listener : listeners.get(api)) {
			listener.onImportComplete();
		}
	}
	
}
