package nl.minicom.evenexus.eveapi.importers;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Provider;

import nl.minicom.evenexus.eveapi.ApiParser;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.ImportLog;
import nl.minicom.evenexus.persistence.dao.ImportLogIdentifier;
import nl.minicom.evenexus.persistence.dao.Importer;
import nl.minicom.evenexus.persistence.interceptor.Transactional;
import nl.minicom.evenexus.utils.TimeUtils;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class ImporterTask extends TimerTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(ImporterTask.class);

	private final Api type;
	private final Database database;
	private final ImportManager importManager;
	private final Provider<ApiParser> apiParserProvider;
	private final Provider<ImporterThread> importerThreadProvider;
	private final long minimumDelay;
	
	private ApiKey apiKey;

	@Inject
	protected ImporterTask(Database database, 
			Provider<ApiParser> apiParserProvider, 
			Provider<ImporterThread> importerThreadProvider,
			ImportManager importManager, 
			Api type) {
		
		this (database, apiParserProvider, importerThreadProvider, importManager, type, 0);
	}
	
	@Inject
	protected ImporterTask(Database database, 
			Provider<ApiParser> apiParserProvider, 
			Provider<ImporterThread> importerThreadProvider,
			ImportManager importManager, 
			Api type, 
			long minimumDelay) {
		
		this.type = type;
		this.database = database;
		this.importManager = importManager;
		this.apiParserProvider = apiParserProvider;
		this.importerThreadProvider = importerThreadProvider;
		this.minimumDelay = minimumDelay;
	}
	
	public abstract void parseApi(Node root, ApiKey apiKey) throws Exception;

	protected Database getDatabase() {
		return database;
	}
	
	protected final void triggerImportCompleteEvent() {
		importManager.triggerImportCompleteEvent(type);
	}

	public void initialize(ApiKey apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public final void run() {
		ImporterThread thread = importerThreadProvider.get();
		thread.initialize(this, apiKey);
		thread.start();
	}
	
	protected void runImporter(ApiKey apiKey) throws Exception {
		if (apiKey != null) {
			LOG.info("Running " + getName() + " importer (characterID: " + apiKey.getCharacterID() + ")");
		}
		else {
			LOG.info("Running " + getName() + " importer");
		}
		
		ApiParser parser = apiParserProvider.get();
		Node root = parser.parseApi(type, apiKey);
		if (ApiParser.isAvailable(root)) {
			parseApi(root, apiKey);
		}
		
		updateLastRun(apiKey);
		triggerImportCompleteEvent();
	}
	
	private void updateLastRun(ApiKey apiKey) throws SQLException {
		Session session = database.getCurrentSession();
		long importerId = type.getImporterId();
		long characterId = 0;
		if (apiKey != null) {
			characterId = apiKey.getCharacterID();
		}
		
		ImportLogIdentifier id = new ImportLogIdentifier(importerId, characterId);
		ImportLog log = (ImportLog) session.get(ImportLog.class, id);
		if (log == null) {
			log = new ImportLog();
			log.setCharacterId(characterId);
			log.setImporterId(importerId);
		}
		
		log.setLastRun(new Timestamp(TimeUtils.getServerTime()));
		session.saveOrUpdate(log);
	}

	public long getNextRun(long characterId) {
		ImportLog log = getImportLog(type.getImporterId(), characterId);
		if (log != null && log.getLastRun() != null) {
			long cooldown = getImporter(type.getImporterId()).getCooldown();
			cooldown = Math.max(cooldown, minimumDelay);
			return log.getLastRun().getTime() + cooldown;
		}
		return 0;
	}
	
	@Transactional
	public ImportLog getImportLog(long importerId, long characterId) {
		Session session = database.getCurrentSession();
		ImportLogIdentifier id = new ImportLogIdentifier(importerId, characterId);
		return (ImportLog) session.createCriteria(ImportLog.class)
			.add(Restrictions.eq(ImportLog.KEY, id))
			.uniqueResult();
	}
	
	@Transactional
	public Importer getImporter(long importerId) {
		Session session = database.getCurrentSession();
		return (Importer) session.createCriteria(Importer.class)
			.add(Restrictions.eq(Importer.ID, importerId))
			.uniqueResult();
	}

	public Api getApi() {
		return type;
	}

	public String getName() {
		return type.name();
	}
	
}
