package nl.minicom.evenexus.eveapi;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import nl.minicom.evenexus.eveapi.exceptions.ApiFailureException;
import nl.minicom.evenexus.eveapi.exceptions.JournalsExhaustedException;
import nl.minicom.evenexus.eveapi.exceptions.MarketOrdersExhaustedException;
import nl.minicom.evenexus.eveapi.exceptions.SecurityNotHighEnoughException;
import nl.minicom.evenexus.eveapi.exceptions.TransactionsExhaustedException;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.ImportLog;
import nl.minicom.evenexus.persistence.dao.ImportLogIdentifier;
import nl.minicom.evenexus.persistence.dao.Importer;
import nl.minicom.evenexus.utils.TimeUtils;

import org.hibernate.Session;
import org.mortbay.xml.XmlParser;
import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;


public class ApiParser {
	
	private static final Logger LOG = LoggerFactory.getLogger(ApiParser.class);

	/**
	 * An enumeration of queryable API services. A service consists of an URL
	 * (where to send the request to) and a cooldownMinutes (how many minutes
	 * have to be between requests)
	 */
	
	public enum Api {
		CHAR_LIST(1),
		CHAR_BALANCE(2),
		CHAR_SKILLS(3),
		CHAR_STANDINGS(4),
		CHAR_WALLET_TRANSACTIONS(5),
		CHAR_WALLET_JOURNAL(6),
		CHAR_MARKET_ORDERS(7), 
		EVE_REF_TYPE(8);
		
		private long importerID;
		
		private Api(long importerID) {
			this.importerID = importerID;
		}
		
		public long getImporterId() {
			return importerID;
		}
	}
		
	private Node root = null;
	private long importerId;
	private ApiKey apiKey;
	private final ApiServerManager apiServerManager;

	public ApiParser(ApiServerManager apiServerManager, long importerId, ApiKey apiKey) throws FileNotFoundException, IOException {
		this(apiServerManager, importerId, apiKey, null);
	}

	public ApiParser(ApiServerManager apiServerManager, long importerId, final ApiKey apiKey, Map<String, String> additionalArguments) throws FileNotFoundException, IOException {
		this.apiKey = apiKey;
		this.apiServerManager = apiServerManager;
		this.importerId = importerId;
		
		Map<String, String> arguments = new TreeMap<String, String>();
		if (apiKey != null) {
			arguments.put("userID", Integer.toString(apiKey.getUserID()));
			arguments.put("apiKey", apiKey.getApiKey());
			arguments.put("characterID", Long.toString(apiKey.getCharacterID()));
		}

		Map<String, String> allArguments = new TreeMap<String, String>(arguments);
		if (additionalArguments != null) {
			allArguments.putAll(additionalArguments);
		}
		
		final Importer importer = getImporter(importerId);
		
		String urlWithAdditionalArguments = createURL(allArguments, importer.getPath());
		if (apiKey != null) {
			boolean parseApi = new Query<Boolean>() {
				@Override
				protected Boolean doQuery(Session session) {
					ImportLogIdentifier id = new ImportLogIdentifier(importer.getId(), apiKey.getCharacterID());
					ImportLog log = (ImportLog) session.get(ImportLog.class, id);
					if (log == null) {
						log = new ImportLog();
						log.setImporterId(id.getImporterId());
						log.setCharacterId(id.getCharacterId());
						log.setLastRun(new Timestamp(TimeUtils.getServerTime()));
						session.save(log);
						return true;
					}
					else if (log.getLastRun().getTime() + importer.getCooldown() < TimeUtils.getServerTime()) {
						log.setLastRun(new Timestamp(TimeUtils.getServerTime()));
						session.update(log);
						return true;
					}
					
					return false;
				}
			}.doQuery(); 
			
			if (parseApi) {
				parseAPI(urlWithAdditionalArguments, importer.getPath());
			}
		}
		else {
			boolean parseApi = new Query<Boolean>() {
				@Override
				protected Boolean doQuery(Session session) {
					ImportLogIdentifier id = new ImportLogIdentifier(importer.getId(), 0);
					ImportLog log = (ImportLog) session.get(ImportLog.class, id);
					if (log == null) {
						log = new ImportLog();
						log.setImporterId(id.getImporterId());
						log.setCharacterId(id.getCharacterId());
						log.setLastRun(new Timestamp(TimeUtils.getServerTime()));
						session.save(log);
						return true;
					}
					else if (log.getLastRun().getTime() + importer.getCooldown() < TimeUtils.getServerTime()) {
						log.setLastRun(new Timestamp(TimeUtils.getServerTime()));
						session.update(log);
						return true;
					}
					
					return false;
				}
			}.doQuery(); 
			
			if (parseApi) {
				parseAPI(urlWithAdditionalArguments, importer.getPath());
			}
		}
	}
	
	private Importer getImporter(final long importerId) {
		return new Query<Importer>() {
			@Override
			protected Importer doQuery(Session session) {
				return (Importer) session.get(Importer.class, importerId);
			}
		}.doQuery();
	}

	private String createURL(Map<String, String> arguments, String importerPath) {
		String url = apiServerManager.getApiServer() + importerPath;
		Set<String> keys = arguments.keySet();
		int count = 0;
		
		if (arguments.size() > 0) {
			url += "?";
			for (String key : keys) {
				if (count > 0) {
					url += "&";
				}
				url += key + "=" + arguments.get(key);
				count++;
			}
		}
		return url;
	}

	private void parseAPI(String url, String importerPath) {
		String hostURL = apiServerManager.getApiServer();
		
		try {
			LOG.info("Requesting: " + hostURL + importerPath);
			File xmlFile = downloadFile(url);
			if (xmlFile == null) {
				throw new IOException("Could not find: " + hostURL + importerPath);
			}
			
			XmlParser parser = new XmlParser();
			root = parser.parse(xmlFile);
			xmlFile.delete();
			
			updateCooldown();
		}
		catch (Throwable e) {
			LOG.warn(e.getLocalizedMessage(), e);
		}
	}

	private void updateCooldown() {
		try {
			if (isAvailable()) {
				new Query<Void>() {
					@Override
					protected Void doQuery(Session session) {
						try {
							Importer object = (Importer) session.get(Importer.class, importerId);
							Timestamp current = TimeUtils.convertToTimestamp(getRoot().get("currentTime").get(0).toString());
							Timestamp until = TimeUtils.convertToTimestamp(getRoot().get("cachedUntil").get(0).toString());
							long diffMin = (until.getTime() - current.getTime()) + 3 * 60000;
							object.setCooldown(diffMin);
							session.saveOrUpdate(object);
						}
						catch (Throwable e) {
							LOG.error(e.getLocalizedMessage(), e);
						}
						return null;
					}
				}.doQuery();
			}
		} 
		catch (Exception e) {	}
	}

	private File downloadFile(String urlString) throws IOException {
		URL url = new URL(urlString);
		URLConnection urlConn = url.openConnection();
		InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
		BufferedReader buff = new BufferedReader(inStream);
		
		StringBuilder builder = new StringBuilder();
		while (true) {
			String nextLine = buff.readLine();
			if (nextLine != null) {
				builder.append(nextLine + "\n");
			}
			else {
				break;
			}
		}
		
		File result = File.createTempFile("EveNexus", ".xml");
		FileWriter out = new FileWriter(result, false);
		out.write(builder.toString());
		out.close();
		inStream.close();
		
		return result;
	}

	/**
	 * Returns the root Node. If it is not requested yet, it will send a
	 * request. If it has been requested already, it will return the cached
	 * response.
	 * 
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws SQLException
	 */
	public Node getRoot() throws IOException, SAXException, SQLException {
		return root;
	}

	/**
	 * Tell us if we can access the root Node safely.
	 * 
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws SQLException
	 * @throws ApiFailureException 
	 */
	public boolean isAvailable() throws Exception {
		boolean hasErrors = false;
		boolean hasRoot = getRoot() != null;
		Node errorNode = null;
		
		if (hasRoot) {
			errorNode = getRoot().get("error");
			hasErrors = errorNode != null;
		}
		if (hasErrors) {
			int errorCode = Integer.parseInt(errorNode.getAttribute("code"));
			switch (errorCode) {
				case 101: 	throw new TransactionsExhaustedException();
				case 103: 	throw new JournalsExhaustedException();
				case 117: 	throw new MarketOrdersExhaustedException();
				case 200: 	throw new SecurityNotHighEnoughException();
				default:	throw new ApiFailureException((String) getRoot().get("error").get(0));
			}			
		}
		return hasRoot && !hasErrors;
	}

	public ApiKey getSettings() {
		return apiKey;
	}

}
