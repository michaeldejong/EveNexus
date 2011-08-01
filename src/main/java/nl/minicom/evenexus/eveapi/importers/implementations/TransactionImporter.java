package nl.minicom.evenexus.eveapi.importers.implementations;


import java.math.BigDecimal;
import java.math.RoundingMode;
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
import nl.minicom.evenexus.persistence.dao.MapRegion;
import nl.minicom.evenexus.persistence.dao.Skill;
import nl.minicom.evenexus.persistence.dao.SkillIdentifier;
import nl.minicom.evenexus.persistence.dao.Standing;
import nl.minicom.evenexus.persistence.dao.StandingIdentifier;
import nl.minicom.evenexus.persistence.dao.Station;
import nl.minicom.evenexus.persistence.dao.WalletTransaction;
import nl.minicom.evenexus.persistence.interceptor.Transactional;
import nl.minicom.evenexus.utils.TimeUtils;

import org.hibernate.Session;
import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TransactionImporter extends ImporterTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(TransactionImporter.class);
	
	private final BugReportDialog dialog;

	@Inject
	public TransactionImporter(
			Database database, 
			Provider<ApiParser> apiParserProvider, 
			Provider<ImporterThread> importerThreadProvider, 
			ImportManager importManager,
			BugReportDialog dialog) {
		
		super(database, apiParserProvider, importerThreadProvider, importManager, Api.CHAR_WALLET_TRANSACTIONS);
		this.dialog = dialog;
	}

	@Override
	public void parseApi(Node node, ApiKey apiKey) {
		final int brokerRelation = getSkillLevel(apiKey.getCharacterID(), 3446);
		final int accounting = getSkillLevel(apiKey.getCharacterID(), 16622);
		final Node root = node.get("result").get("rowset");
		for (int i = root.size() - 1; i >= 0; i--) {
			processRow(root, i, apiKey, brokerRelation, accounting);
		}
	}
	
	private int getSkillLevel(long characterId, long skillId) {
		int level = 0;
		Skill skill = getSkill(new SkillIdentifier(characterId, skillId));
		if (skill != null) {
			level = skill.getLevel();
		}
		
		return level;
	}
	
	@Transactional
	private Skill getSkill(final SkillIdentifier id) {
		Session session = getDatabase().getCurrentSession();
		return (Skill) session.get(Skill.class, id);
	}

	private void processRow(Node root, int i, ApiKey apiKey, int brokerRelation, int accounting) {
		if (root.get(i) instanceof Node) {
			Node row = (Node) root.get(i);
			if (row.getTag().equals("row")) {
				persistChangeData(row, apiKey, brokerRelation, accounting);
			}
		}
	}

	@Transactional
	private void persistChangeData(Node row, ApiKey apiKey, int brokerRelation, int accounting) {
		Session session = getDatabase().getCurrentSession();

		try {
			Timestamp currentTime = TimeUtils.convertToTimestamp(row.getAttribute("transactionDateTime"));
			long transactionID = Long.parseLong(row.getAttribute("transactionID")); 
			long quantity = Long.parseLong(row.getAttribute("quantity")); 
			String typeName = row.getAttribute("typeName");
			long typeId = Long.parseLong(row.getAttribute("typeID"));
			BigDecimal price = BigDecimal.valueOf(Float.parseFloat(row.getAttribute("price"))); 
			long clientId = Long.parseLong(row.getAttribute("clientID")); 
			String clientName = row.getAttribute("clientName");
			long stationId = Long.parseLong(row.getAttribute("stationID")); 
			String stationName = row.getAttribute("stationName");
			boolean isBuy = ("buy").equals(row.getAttribute("transactionType")); 
			boolean isPersonal = ("personal").equals(row.getAttribute("transactionFor"));
			
			BigDecimal corporationStanding = getCorporationStanding(apiKey, stationId);
			BigDecimal factionStanding = getFactionStanding(apiKey, stationId);
			
			BigDecimal taxes = (BigDecimal.valueOf(0.01).subtract(BigDecimal.valueOf(0.0005).multiply(BigDecimal.valueOf(brokerRelation)))).divide(BigDecimal.valueOf(Math.exp(((BigDecimal.valueOf(0.1).multiply(factionStanding)).add(BigDecimal.valueOf(0.04).multiply(corporationStanding)).doubleValue()))), 3, RoundingMode.HALF_UP);
			if (!isBuy) {
				taxes = taxes.add(BigDecimal.valueOf(0.01).subtract(BigDecimal.valueOf(0.001).multiply(BigDecimal.valueOf(accounting))));
			}
			
			WalletTransaction transaction = (WalletTransaction) session.get(WalletTransaction.class, transactionID);
			if (transaction == null) {
				BigDecimal actualPrice = price;
				if (isBuy) {
					actualPrice = price.negate();
				}
				
				transaction = new WalletTransaction();
				transaction.setTransactionId(transactionID);
				transaction.setTransactionDateTime(currentTime);
				transaction.setCharacterId(apiKey.getCharacterID());
				transaction.setQuantity(quantity);
				transaction.setRemaining(quantity);
				transaction.setTypeName(typeName);
				transaction.setTypeId(typeId);
				transaction.setPrice(actualPrice);
				transaction.setTaxes(taxes.multiply(price.abs()).negate());
				transaction.setClientId(clientId);
				transaction.setClientName(clientName);
				transaction.setStationId(stationId);
				transaction.setStationName(stationName);
				transaction.setPersonal(isPersonal);
				session.save(transaction);		
			}			
		} 
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
		}
	}

	@Transactional
	private BigDecimal getCorporationStanding(ApiKey apiKey, long stationId) {
		Session session = getDatabase().getCurrentSession();
		Station station = (Station) session.get(Station.class, stationId);
		if (station != null) {
			long characterId = apiKey.getCharacterID();
			StandingIdentifier id = new StandingIdentifier(characterId, station.getCorporationId());
			Standing standing = (Standing) session.get(Standing.class, id);
			return standing.getStanding();
		}
		return BigDecimal.ZERO;
	}

	@Transactional
	private BigDecimal getFactionStanding(ApiKey apiKey, long mapRegionId) {
		Session session = getDatabase().getCurrentSession();
		MapRegion region = (MapRegion) session.get(MapRegion.class, mapRegionId);
		if (region != null) {
			long characterId = apiKey.getCharacterID();
			StandingIdentifier id = new StandingIdentifier(characterId, region.getFactionId());
			Standing standing = (Standing) session.get(Standing.class, id);
			return standing.getStanding();
		}
		return BigDecimal.ZERO;
	}

}
