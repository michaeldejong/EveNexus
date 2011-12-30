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

import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link RefTypeImporter} is responsible for importing ref types
 * from the EVE API server.
 *
 * @author michael
 */
public class RefTypeImporter extends ImporterTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(RefTypeImporter.class);
	
	private final BugReportDialog dialog;
	
	private volatile boolean isReady = true;

	/**
	 * This constructs a new {@link RefTypeImporter} object.
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
	 * @param dialog
	 * 		The {@link BugReportDialog}.
	 */
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
	public void parseApi(Node node, ApiKey apiKey) {
		synchronized (this) {
			isReady = false;
			final Node root = node.get("result").get("rowset");
			for (int i = root.size() - 1; i >= 0; i--) {
				processRow(root, i);
			}
			isReady = true;
		}
	}

	private void processRow(Node root, int i) {
		if (root.get(i) instanceof Node) {
			Node row = (Node) root.get(i);
			if (row.getTag().equals("row")) {
				persistChangeData(row);
			}
		}
	}
	
	/**
	 * This method writes the journal entry to the database.
	 * 
	 * @param row
	 * 		The {@link Node} containing all the ref type information.
	 */
	@Transactional
	protected void persistChangeData(Node row) {
		try {			
			long refTypeId = Long.parseLong(row.getAttribute("refTypeID"));
			String refTypeName = row.getAttribute("refTypeName");	
			
			RefType refType = new RefType();
			refType.setRefTypeId(refTypeId);
			refType.setDescription(refTypeName);
			
			getDatabase().getCurrentSession().saveOrUpdate(refType);
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
