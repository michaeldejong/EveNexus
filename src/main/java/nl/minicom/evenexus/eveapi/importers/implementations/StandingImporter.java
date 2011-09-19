package nl.minicom.evenexus.eveapi.importers.implementations;


import java.math.BigDecimal;

import javax.inject.Inject;
import javax.inject.Provider;

import nl.minicom.evenexus.eveapi.ApiParser;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.eveapi.importers.ImporterTask;
import nl.minicom.evenexus.eveapi.importers.ImporterThread;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.Standing;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StandingImporter extends ImporterTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(StandingImporter.class);
	
	private final BugReportDialog dialog;

	@Inject
	public StandingImporter(
			Database database, 
			Provider<ApiParser> apiParserProvider, 
			Provider<ImporterThread> importerThreadProvider, 
			ImportManager importManager,
			BugReportDialog dialog) {
		
		super(database, apiParserProvider, importerThreadProvider, importManager, Api.CHAR_STANDINGS);
		this.dialog = dialog;
	}

	@Override
	public void parseApi(Node node, ApiKey apiKey) {
		final Node root = node.get("result").get("characterNPCStandings");
		long characterId = apiKey.getCharacterId();
		for (int j = 0; j < root.size(); j++) {
			if (root.get(j) instanceof Node) {
				Node subNode = (Node) root.get(j);
				if (subNode.getTag().equals("rowset")) {
					for (int i = subNode.size() - 1; i >= 0; i--) {
						processRow(subNode, i, characterId);
					}
				}
			}
		}
	}

	private void processRow(Node root, int i, long characterID) {
		if (root.get(i) instanceof Node) {
			Node row = (Node) root.get(i);
			if (row.getTag().equals("row")) {
				persistChangeData(row, characterID);
			}
		}
	}

	@Transactional
	protected void persistChangeData(Node row, long characterId) {
		try {			
			long fromId = Long.parseLong(row.getAttribute("fromID"));
			String fromName = row.getAttribute("fromName");
			BigDecimal value = BigDecimal.valueOf(Double.parseDouble(row.getAttribute("standing"))); 
			
			Standing standing = new Standing();
			standing.setCharacterId(characterId);
			standing.setFromId(fromId);
			standing.setFromName(fromName);
			standing.setStanding(value);
			
			getDatabase().getCurrentSession().saveOrUpdate(standing);
		}
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
		}
	}
}
