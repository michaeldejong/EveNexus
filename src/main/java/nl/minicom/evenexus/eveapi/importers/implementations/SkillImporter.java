package nl.minicom.evenexus.eveapi.importers.implementations;


import nl.minicom.evenexus.eveapi.ApiParser;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.ApiServerManager;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.eveapi.importers.ImporterTask;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.Skill;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.mortbay.xml.XmlParser.Node;


public class SkillImporter extends ImporterTask {
	
	private static final Logger logger = LogManager.getRootLogger();
	
	public SkillImporter(ApiServerManager apiServerManager, ImportManager importManager, ApiKey apiKey) {
		super(apiServerManager, importManager, Api.CHAR_SKILLS, apiKey);
	}
	
	public void parseApi(ApiParser parser) throws Exception {
		final Node root = parser.getRoot().get("result");
		new Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				for (int j = 0; j < root.size(); j++) {
					if (root.get(j) instanceof Node) {
						Node subNode = (Node) root.get(j);
						if (subNode.getTag().equals("rowset") && subNode.getAttribute("name").equals("skills")) {
							for (int i = subNode.size() - 1; i >= 0 ; i--) {
								processRow(subNode, i, session);
							}
						}
					}
				}
				return null;
			}
		}.doQuery();
	}

	private void processRow(Node root, int i, Session session) {
		if (root.get(i) instanceof Node) {
			Node row = (Node) root.get(i);
			if (row.getTag().equals("row")) {
				persistChangeData(row, session);
			}
		}
	}

	private void persistChangeData(Node row, Session session) {
		try {			
			long typeId = Long.parseLong(row.getAttribute("typeID"));
			int level = Integer.parseInt(row.getAttribute("level")); 
			
			Skill skill = new Skill();
			skill.setCharacterId(getApiKey().getCharacterID());
			skill.setTypeId(typeId);
			skill.setLevel(level);
			session.saveOrUpdate(skill);
		}
		catch (Exception e) {
			logger.error(e);
		}
	}

}
