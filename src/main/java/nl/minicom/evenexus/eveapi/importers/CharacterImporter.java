package nl.minicom.evenexus.eveapi.importers;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Provider;

import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.importers.implementations.JournalImporter;
import nl.minicom.evenexus.eveapi.importers.implementations.MarketOrderImporter;
import nl.minicom.evenexus.eveapi.importers.implementations.SkillImporter;
import nl.minicom.evenexus.eveapi.importers.implementations.StandingImporter;
import nl.minicom.evenexus.eveapi.importers.implementations.TransactionImporter;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.utils.TimeUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


public class CharacterImporter {
	
	private static final Logger LOG = LoggerFactory.getLogger(CharacterImporter.class);
	
	private final ImportManager importManager;
	private final Provider<JournalImporter> journalImporterProvider;
	private final Provider<MarketOrderImporter> marketOrderImporterProvider;
	private final Provider<SkillImporter> skillImporterProvider;
	private final Provider<StandingImporter> standingImporterProvider;
	private final Provider<TransactionImporter> transactionImporterProvider;
	private final Map<Api, ImporterTask> importers;
	
	private ApiKey apiKey = null;
	
	@Inject
	public CharacterImporter(ImportManager importManager, 
			Provider<JournalImporter> journalImporterProvider,
			Provider<MarketOrderImporter> marketOrderImporterProvider,
			Provider<SkillImporter> skillImporterProvider,
			Provider<StandingImporter> standingImporterProvider,
			Provider<TransactionImporter> transactionImporterProvider) {
		
		this.importManager = importManager;
		this.journalImporterProvider = journalImporterProvider;
		this.marketOrderImporterProvider = marketOrderImporterProvider;
		this.skillImporterProvider = skillImporterProvider;
		this.standingImporterProvider = standingImporterProvider;
		this.transactionImporterProvider = transactionImporterProvider;
		this.importers = new TreeMap<Api, ImporterTask>();
	}

	public void scheduleApiImporters(ApiKey apiKey) {
		Preconditions.checkNotNull(apiKey);
		if (this.apiKey != null) {
			throw new IllegalStateException("CharacterImporter already initialized!");
		}
		
		this.apiKey = apiKey;
		
		scheduleApiImporter(journalImporterProvider.get());
		scheduleApiImporter(marketOrderImporterProvider.get());
		scheduleApiImporter(skillImporterProvider.get());
		scheduleApiImporter(standingImporterProvider.get());
		scheduleApiImporter(transactionImporterProvider.get());
	}
	
	private void scheduleApiImporter(ImporterTask task) {
		importers.put(task.getApi(), task);		
		long nextRun = task.getNextRun(apiKey.getCharacterId()) + 5000;
		if (nextRun < TimeUtils.getServerTime()) {
			nextRun = TimeUtils.getServerTime() + 5000;
		}
		
		task.initialize(apiKey);
		
		importManager.scheduleAtFixedRate(task, nextRun - TimeUtils.getServerTime(), task.getImporter(task.getApi().getImporterId()).getCooldown());
		LOG.info("Scheduling " + task.getName() + " importer (characterID: " + apiKey.getCharacterId() + ") at: " + new Date(nextRun));
	}
	
}
