package nl.minicom.evenexus.eveapi.importers.implementations;


import java.math.BigDecimal;

import javax.inject.Inject;
import javax.inject.Provider;

import nl.minicom.evenexus.eveapi.ApiParser;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.eveapi.importers.ImporterTask;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.Standing;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.Session;
import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StandingImporter extends ImporterTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(StandingImporter.class);

	@Inject
	public StandingImporter(Database database, Provider<ApiParser> apiParserProvider, ImportManager importManager) {
		super(database, apiParserProvider, importManager, Api.CHAR_STANDINGS);
	}

	@Override
	@Transactional
	public void parseApi(Node node, ApiKey apiKey) {
		Session session = getDatabase().getCurrentSession();
		final Node root = node.get("result").get("characterNPCStandings");
		long characterId = apiKey.getCharacterID();
		for (int j = 0; j < root.size(); j++) {
			if (root.get(j) instanceof Node) {
				Node subNode = (Node) root.get(j);
				if (subNode.getTag().equals("rowset")) {
					for (int i = subNode.size() - 1; i >= 0 ; i--) {
						processRow(subNode, i, characterId, session);
					}
				}
			}
		}
	}

	private void processRow(Node root, int i, long characterID, Session session) {
		if (root.get(i) instanceof Node) {
			Node row = (Node) root.get(i);
			if (row.getTag().equals("row")) {
				persistChangeData(row, characterID, session);
			}
		}
	}

	private void persistChangeData(Node row, long characterId, Session session) {
		try {			
			long fromId = Long.parseLong(row.getAttribute("fromID"));
			String fromName = row.getAttribute("fromName");
			BigDecimal value = BigDecimal.valueOf(Double.parseDouble(row.getAttribute("standing"))); 
			
			Standing standing = new Standing();
			standing.setCharacterId(characterId);
			standing.setFromId(fromId);
			standing.setFromName(fromName);
			standing.setStanding(value);
			session.saveOrUpdate(standing);
		}
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
	}
}
