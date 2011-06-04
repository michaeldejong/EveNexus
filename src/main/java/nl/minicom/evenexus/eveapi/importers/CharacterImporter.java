package nl.minicom.evenexus.eveapi.importers;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.ApiServerManager;
import nl.minicom.evenexus.eveapi.importers.implementations.JournalImporter;
import nl.minicom.evenexus.eveapi.importers.implementations.MarketOrderImporter;
import nl.minicom.evenexus.eveapi.importers.implementations.SkillImporter;
import nl.minicom.evenexus.eveapi.importers.implementations.StandingImporter;
import nl.minicom.evenexus.eveapi.importers.implementations.TransactionImporter;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.utils.TimeUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class CharacterImporter {
	
	private static final Logger logger = LogManager.getRootLogger();
	
	private final ImportManager importManager;
	private final Map<Api, ImporterTask> importers;
	
	public CharacterImporter(ApiServerManager apiServerManager, ImportManager importManager, ApiKey apiKey) {
		this.importManager = importManager;
		this.importers = new TreeMap<Api, ImporterTask>();
		scheduleApiImporters(apiServerManager, apiKey);
	}

	private void scheduleApiImporters(ApiServerManager apiServerManager, ApiKey apiKey) {
		scheduleApiImporter(new JournalImporter(apiServerManager, importManager, apiKey));
		scheduleApiImporter(new MarketOrderImporter(apiServerManager, importManager, apiKey));
		scheduleApiImporter(new SkillImporter(apiServerManager, importManager, apiKey));
		scheduleApiImporter(new StandingImporter(apiServerManager, importManager, apiKey));
		scheduleApiImporter(new TransactionImporter(apiServerManager, importManager, apiKey));
	}
	
	private void scheduleApiImporter(ImporterTask task) {
		importers.put(task.getApi(), task);		
		long nextRun = task.getNextRun() + 5000;
		if (nextRun < TimeUtils.getServerTime()) {
			nextRun = TimeUtils.getServerTime() + 5000;
		}
		
		importManager.scheduleAtFixedRate(task, nextRun - TimeUtils.getServerTime(), task.getImporter().getCooldown());
		logger.info("Scheduling " + task.getImporter().getName() + " importer (characterID: " + task.getApiKey().getCharacterID() + ") at: " + new Date(nextRun));
	}
	
	public Collection<ImporterTask> getSchedule() {
		return Collections.unmodifiableCollection(importers.values());
	}
	
}
