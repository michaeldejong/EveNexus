package nl.minicom.evenexus.eveapi.importers.implementations;


import java.math.BigDecimal;

import nl.minicom.evenexus.eveapi.ApiParser;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.ApiServerManager;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.eveapi.importers.ImporterTask;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.Standing;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.mortbay.xml.XmlParser.Node;


public class StandingImporter extends ImporterTask {
	
	private static final Logger logger = LogManager.getRootLogger();
	
	public StandingImporter(ApiServerManager apiServerManager, ImportManager importManager, ApiKey settings) {
		super(apiServerManager, importManager, Api.CHAR_STANDINGS, settings);
	}
	
	public void parseApi(final ApiParser parser) throws Exception {
		final Node root = parser.getRoot().get("result").get("characterNPCStandings");
		new Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				long characterId = parser.getSettings().getCharacterID();
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
				return null;
			}
		}.doQuery();
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
			logger.error(e);
		}
	}
}
