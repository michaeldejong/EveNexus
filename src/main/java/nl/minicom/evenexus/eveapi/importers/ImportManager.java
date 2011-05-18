package nl.minicom.evenexus.eveapi.importers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.ApiServerManager;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.ApiKey;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


public class ImportManager extends Timer {
	
	private static final Logger logger = LogManager.getRootLogger();
	
	private final List<CharacterImporter> importers;
	private final Multimap<Api, ImportListener> listeners;
	private final ApiServerManager apiServerManager;
	
	public ImportManager(ApiServerManager apiServerManager) {
		this.apiServerManager = apiServerManager;
		this.importers = new ArrayList<CharacterImporter>();
		this.listeners = HashMultimap.create();
	}
	
	public void initialize() {
		if (!importers.isEmpty()) {
			logger.error("Already initialized!");
		}
		else {
			createCharacterImporters();
		}
	}
	
	public List<CharacterImporter> getImporters() {
		return Collections.unmodifiableList(importers);
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
		logger.debug("Scheduling importers for character: " + apiKey.getCharacterName());
		importers.add(new CharacterImporter(apiServerManager, this, apiKey));
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
