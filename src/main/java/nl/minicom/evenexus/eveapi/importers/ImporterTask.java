package nl.minicom.evenexus.eveapi.importers;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.TimerTask;

import nl.minicom.evenexus.eveapi.ApiParser;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.ApiServerManager;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.ImportLog;
import nl.minicom.evenexus.persistence.dao.ImportLogIdentifier;
import nl.minicom.evenexus.persistence.dao.Importer;
import nl.minicom.evenexus.utils.TimeUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;


public abstract class ImporterTask extends TimerTask {
	
	private static Logger logger = LogManager.getRootLogger();

	private final Api type;
	private final ApiServerManager apiServerManager;
	private final ImportManager importManager;
	private final ApiKey apiKey;
	private final Importer importer;

	public ImporterTask(ApiServerManager apiServerManager, ImportManager importManager, Api type, ApiKey apiKey) {
		this.apiKey = apiKey;
		this.apiServerManager = apiServerManager;
		this.importManager = importManager;
		this.type = type;
		this.importer = loadImporter(type.getImporterId());
	}
	
	private Importer loadImporter(final long importerId) {
		return new Query<Importer>() {
			@Override
			protected Importer doQuery(Session session) {
				return (Importer) session.get(Importer.class, importerId);
			}
		}.doQuery();
	}
	
	private ImportLog loadImportLog(final ImportLogIdentifier importLogId) {
		return new Query<ImportLog>() {
			@Override
			protected ImportLog doQuery(Session session) {
				return (ImportLog) session.get(ImportLog.class, importLogId);
			}
		}.doQuery();
	}

	protected final void triggerImportCompleteEvent() {
		importManager.triggerImportCompleteEvent(getApi());
	}

	@Override
	public final void run() {
		new ImporterThread(this).start();
	}

	public long getNextRun() {
		ImportLog log = loadImportLog(new ImportLogIdentifier(type.getImporterId(), apiKey.getCharacterID()));
		if (log != null && log.getLastRun() != null) {
			return log.getLastRun().getTime() + getImporter().getCooldown();
		}
		return 0;
	}
	
	protected void runImporter() throws Exception {
		logger.info("Running " + getImporter().getName() + " importer (characterID: " + getApiKey().getCharacterID() + ")");
		ApiParser parser = new ApiParser(apiServerManager, getImporter().getId(), apiKey);
		if (parser.isAvailable()) {
			parseApi(parser);
		}
		
		updateLastRun();
		triggerImportCompleteEvent();
	}
	
	private void updateLastRun() throws SQLException {
		new Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				long importerId = getImporter().getId();
				long characterId = getApiKey().getCharacterID();
				ImportLogIdentifier id = new ImportLogIdentifier(importerId, characterId);
				ImportLog log = (ImportLog) session.get(ImportLog.class, id);
				log.setLastRun(new Timestamp(TimeUtils.getServerTime()));
				session.saveOrUpdate(log);
				return null;
			}
		}.doQuery();
	}

	public Importer getImporter() {
		return importer;
	}
	
	public Api getApi() {
		return type;
	}
	
	public ApiKey getApiKey() {
		return apiKey;
	}
	
	public abstract void parseApi(ApiParser parser) throws Exception;
	
}
