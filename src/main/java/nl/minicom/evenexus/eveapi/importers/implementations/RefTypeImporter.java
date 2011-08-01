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
import nl.minicom.evenexus.persistence.dao.RefType;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.Session;
import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RefTypeImporter extends ImporterTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(RefTypeImporter.class);
	
	private final BugReportDialog dialog;

	@Inject
	public RefTypeImporter(
			Database database, 
			Provider<ApiParser> apiParserProvider, 
			Provider<ImporterThread> importerThreadProvider, 
			ImportManager importManager,
			BugReportDialog dialog) {
		
		super(database, apiParserProvider, importerThreadProvider, importManager, Api.EVE_REF_TYPE);
		this.dialog = dialog;
	}

	@Override
	@Transactional
	public void parseApi(Node node, ApiKey apiKey) {
		Session session = getDatabase().getCurrentSession();
		final Node root = node.get("result").get("rowset");
		for (int i = root.size() - 1; i >= 0; i--) {
			processRow(root, i, session);
		}
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
			dialog.setVisible(true);
		}
	}
}
