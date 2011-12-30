package nl.minicom.evenexus.eveapi.importers;

import java.sql.Timestamp;
import java.util.TimerTask;

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

/**
 * The {@link ImporterTask} is a class which is responsible for accessing the 
 * EVE API on a regular basis.
 *
 * @author michael
 */
public abstract class ImporterTask extends TimerTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(ImporterTask.class);

	private final Api type;
	private final Database database;
	private final ImportManager importManager;
	private final Provider<ApiParser> apiParserProvider;
	private final Provider<ImporterThread> importerThreadProvider;
	
	private long minimumDelay = 0;
	private ApiKey apiKey;

	/**
	 * This constructs a new {@link ImporterTask} object.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 * 
	 * @param apiParserProvider
	 * 		The {@link Provider} which provides {@link ApiParser}s.
	 * 
	 * @param importerThreadProvider
	 * 		The {@link Provider} which provides {@link ImporterThread}s.
	 * 
	 * @param importManager
	 * 		The {@link ImportManager}.
	 * 
	 * @param type
	 * 		The {@link Api}.
	 */
	protected ImporterTask(Database database, 
			Provider<ApiParser> apiParserProvider, 
			Provider<ImporterThread> importerThreadProvider,
			ImportManager importManager, 
			Api type) {
		
		this.type = type;
		this.database = database;
		this.importManager = importManager;
		this.apiParserProvider = apiParserProvider;
		this.importerThreadProvider = importerThreadProvider;
	}

	/**
	 * This method initializes the {@link ImporterTask}.
	 * 
	 * @param apiKey
	 * 		The {@link ApiKey} to use.
	 */
	public void initialize(ApiKey apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return
	 * 		The {@link Database}.
	 */
	protected Database getDatabase() {
		return database;
	}
	
	/**
	 * This method notifies the {@link ImportManager} that an import has completed. 
	 */
	protected final void triggerImportCompleteEvent() {
		importManager.triggerImportCompleteEvent(type);
	}

	@Override
	public final void run() {
		ImporterThread thread = importerThreadProvider.get();
		thread.initialize(this, apiKey);
		thread.start();
	}
	
	/**
	 * This method runs the import.
	 * 
	 * @param apiKey
	 * 		The {@link ApiKey} to use with the import.
	 * 
	 * @throws Exception
	 * 		If the API could not be accessed or processed.
	 */
	protected void runImporter(ApiKey apiKey) throws Exception {
		if (apiKey != null) {
			LOG.info("Running " + getName() + " importer (characterID: " + apiKey.getCharacterId() + ")");
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
	
	/**
	 * Update the last run field of the specified {@link ApiKey}.
	 * 
	 * @param apiKey
	 * 		The {@link ApiKey}.
	 */
	@Transactional
	protected void updateLastRun(ApiKey apiKey) {
		Session session = database.getCurrentSession();
		long importerId = type.getImporterId();
		long characterId = 0;
		if (apiKey != null) {
			characterId = apiKey.getCharacterId();
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

	/**
	 * This method returns the timestamp when the next import should be run.
	 * 
	 * @param characterId
	 * 		For the specified character id.
	 * 
	 * @return
	 * 		The time in milliseconds from the start of the unix epoch.
	 */
	protected long getNextRun(long characterId) {
		ImportLog log = getImportLog(type.getImporterId(), characterId);
		if (log != null && log.getLastRun() != null) {
			long cooldown = getImporter(type.getImporterId()).getCooldown();
			cooldown = Math.max(cooldown, minimumDelay);
			return log.getLastRun().getTime() + cooldown;
		}
		return 0;
	}
	
	/**
	 * This method sets the minimum delay between imports in milliseconds.
	 * 
	 * @param minimumDelay
	 * 		The minimum delay in milliseconds.
	 */
	protected void setMinimumDelay(long minimumDelay) {
		this.minimumDelay = minimumDelay;
	}
	
	/**
	 * This method gets the {@link ImportLog} entry for the specified importer id
	 * and character id.
	 * 
	 * @param importerId
	 * 		The id of the importer.
	 * 
	 * @param characterId
	 * 		The id of the character.
	 * 
	 * @return
	 * 		The {@link ImportLog} entry for the specified importer en character id.
	 */
	@Transactional
	protected ImportLog getImportLog(long importerId, long characterId) {
		Session session = database.getCurrentSession();
		ImportLogIdentifier id = new ImportLogIdentifier(importerId, characterId);
		return (ImportLog) session.createCriteria(ImportLog.class)
			.add(Restrictions.eq(ImportLog.KEY, id))
			.uniqueResult();
	}
	
	/**
	 * This method gets the {@link Importer} of the specified id.
	 * 
	 * @param importerId
	 * 		The id of the {@link Importer}.
	 * 
	 * @return
	 * 		The {@link Importer} with the specified id.
	 */
	@Transactional
	protected Importer getImporter(long importerId) {
		Session session = database.getCurrentSession();
		return (Importer) session.createCriteria(Importer.class)
			.add(Restrictions.eq(Importer.ID, importerId))
			.uniqueResult();
	}

	/**
	 * @return
	 * 		The {@link Api} used by this {@link ImporterTask}.
	 */
	public Api getApi() {
		return type;
	}

	/**
	 * @return
	 * 		The name of this {@link ImporterTask}.
	 */
	public String getName() {
		return type.name();
	}
	
	/**
	 * This method defines how the API request should be interpreted.
	 * 
	 * @param root
	 * 		The XML root node.
	 * 
	 * @param apiKey
	 * 		The {@link ApiKey} used to access the API.
	 * 
	 * @throws Exception
	 * 		If something went wrong while processing the API.
	 */
	public abstract void parseApi(Node root, ApiKey apiKey) throws Exception;

	/**
	 * @return
	 * 		True if the {@link ImporterTask} is done.
	 */
	public abstract boolean isReady();
	
}
