package nl.minicom.evenexus.eveapi.importers.implementations;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
import nl.minicom.evenexus.persistence.dao.WalletJournal;
import nl.minicom.evenexus.persistence.interceptor.Transactional;
import nl.minicom.evenexus.utils.TimeUtils;

import org.hibernate.Session;
import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JournalImporter extends ImporterTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(JournalImporter.class);
	
	private final BugReportDialog dialog;
	
	private volatile boolean isReady = true; 
	
	@Inject
	public JournalImporter(
			Database database, 
			Provider<ApiParser> apiParserProvider, 
			Provider<ImporterThread> importerThreadProvider, 
			ImportManager importManager,
			BugReportDialog dialog) {
		
		super(database, apiParserProvider, importerThreadProvider, importManager, Api.CHAR_WALLET_JOURNAL);
		this.dialog = dialog;
	}

	@Override
	public void parseApi(Node root, ApiKey apiKey) {
		synchronized (this) {
			isReady = false;
			
			int inserted = 0;
			final Node node = root.get("result").get("rowset");
			for (int i = node.size() - 1; i >= 0; i--) {
				if (processRow(node, i)) {
					inserted++;
				}
			}
			
			LOG.info("Inserted " + inserted + " new journal entries.");
			isReady = true;
		}
	}

	private boolean processRow(Node root, int i) {
		if (root.get(i) instanceof Node) {
			Node row = (Node) root.get(i);
			if (row.getTag().equals("row")) {
				return persistChangeData(row);
			}
		}
		
		return false;
	}

	@Transactional
	boolean persistChangeData(Node row) {
		Session session = getDatabase().getCurrentSession();
		
		try {			
			long refId = Long.parseLong(row.getAttribute("refID"));
			int journalTypeId = Integer.parseInt(row.getAttribute("refTypeID")); 
			Timestamp currentTime = TimeUtils.convertToTimestamp(row.getAttribute("date"));
			String ownerName1 = row.getAttribute("ownerName1");
			long ownerId1 = Long.parseLong(row.getAttribute("ownerID1"));
			String ownerName2 = row.getAttribute("ownerName2");
			long ownerId2 = Long.parseLong(row.getAttribute("ownerID2"));
			String argName1 = row.getAttribute("argName1");
			long argId1 = Long.parseLong(row.getAttribute("argID1"));
			BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(row.getAttribute("amount")));
			BigDecimal balance = BigDecimal.valueOf(Double.parseDouble(row.getAttribute("balance")));
			String reason = row.getAttribute("reason");	
			long taxReceiverId = getTaxReceiverId(row);
			BigDecimal taxAmount = getTaxAmount(row);
			
			WalletJournal journal = (WalletJournal) session.get(WalletJournal.class, refId);
			if (journal == null) {
				journal = new WalletJournal();
				journal.setRefId(refId);
				journal.setJournalTypeId(journalTypeId);
				journal.setDate(currentTime);
				journal.setOwnerName1(ownerName1);
				journal.setOwnerId1(ownerId1);
				journal.setOwnerName2(ownerName2);
				journal.setOwnerId2(ownerId2);
				journal.setArgName1(argName1);
				journal.setArgId1(argId1);
				journal.setAmount(amount);
				journal.setBalance(balance);
				journal.setReason(reason);
				journal.setTaxReceiverId(taxReceiverId);
				journal.setTaxAmount(taxAmount);
				
				getDatabase().getCurrentSession().save(journal);
				
				return true;
			}
		} 
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
		}
		
		return false;
	}

	private BigDecimal getTaxAmount(Node row) {
		BigDecimal taxAmount = BigDecimal.ZERO;
		String taxAmountString = row.getAttribute("taxAmount");
		if (taxAmountString != null && !taxAmountString.isEmpty()) {
			taxAmount = BigDecimal.valueOf(Double.parseDouble(taxAmountString));
		}
		return taxAmount;
	}

	private long getTaxReceiverId(Node row) {
		long taxReceiverId = 0L;
		String taxReceiverIdString = row.getAttribute("taxReceiverID");
		if (taxReceiverIdString != null && !taxReceiverIdString.isEmpty()) {
			taxReceiverId = Long.parseLong(taxReceiverIdString);
		}
		return taxReceiverId;
	}

	@Override
	public boolean isReady() {
		synchronized (this) {
			return isReady;
		}
	}
}
