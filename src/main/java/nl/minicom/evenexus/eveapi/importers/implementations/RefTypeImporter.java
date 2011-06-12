package nl.minicom.evenexus.eveapi.importers.implementations;

import nl.minicom.evenexus.eveapi.ApiParser;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.ApiServerManager;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.eveapi.importers.ImporterTask;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.RefType;

import org.hibernate.Session;
import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RefTypeImporter extends ImporterTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(RefTypeImporter.class);
	
	public RefTypeImporter(ApiServerManager apiServerManager, ImportManager importManager) {
		super(apiServerManager, importManager, Api.EVE_REF_TYPE, null, 3600000L);
	}

	public void parseApi(ApiParser parser) throws Exception {
		final Node root = parser.getRoot().get("result").get("rowset");
		new Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				for (int i = root.size() - 1; i >= 0 ; i--) {
					processRow(root, i, session);
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
			long refTypeId = Long.parseLong(row.getAttribute("refTypeID"));
			String refTypeName = row.getAttribute("refTypeName");	
			
			RefType refType = new RefType();
			refType.setRefTypeId(refTypeId);
			refType.setDescription(refTypeName);
			session.saveOrUpdate(refType);
		} 
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
	}
}
