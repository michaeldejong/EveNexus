package nl.minicom.evenexus.eveapi.importers.implementations;


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
import nl.minicom.evenexus.persistence.dao.Skill;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SkillImporter extends ImporterTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(SkillImporter.class);
	
	private final BugReportDialog dialog;
	
	private volatile boolean isReady = true;

	@Inject
	public SkillImporter(
			Database database, 
			Provider<ApiParser> apiParserProvider, 
			Provider<ImporterThread> importerThreadProvider, 
			ImportManager importManager,
			BugReportDialog dialog) {
		
		super(database, apiParserProvider, importerThreadProvider, importManager, Api.CHAR_SKILLS);
		this.dialog = dialog;
	}

	@Override
	public void parseApi(Node node, ApiKey apiKey) {
		synchronized (this) {
			isReady = false;
			final Node root = node.get("result");
			for (int j = 0; j < root.size(); j++) {
				if (root.get(j) instanceof Node) {
					Node subNode = (Node) root.get(j);
					if (subNode.getTag().equals("rowset") && subNode.getAttribute("name").equals("skills")) {
						for (int i = subNode.size() - 1; i >= 0; i--) {
							processRow(subNode, i, apiKey);
						}
					}
				}
			}
			isReady = true;
		}
	}

	private void processRow(Node root, int i, ApiKey apiKey) {
		if (root.get(i) instanceof Node) {
			Node row = (Node) root.get(i);
			if (row.getTag().equals("row")) {
				persistChangeData(row, apiKey);
			}
		}
	}

	@Transactional
	void persistChangeData(Node row, ApiKey apiKey) {
		try {			
			long typeId = Long.parseLong(row.getAttribute("typeID"));
			int level = Integer.parseInt(row.getAttribute("level")); 
			
			Skill skill = new Skill();
			skill.setCharacterId(apiKey.getCharacterId());
			skill.setTypeId(typeId);
			skill.setLevel(level);
			
			getDatabase().getCurrentSession().saveOrUpdate(skill);
		}
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
		}
	}

	@Override
	public boolean isReady() {
		synchronized (this) {
			return isReady;
		}
	}

}
