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
import nl.minicom.evenexus.inventory.InventoryManager;
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


/**
 * This class is responsible for importing transactions.
 *
 * @author michael
 */
public class TransactionImporter extends ImporterTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(TransactionImporter.class);
	
	private final InventoryManager inventoryManager;
	private final BugReportDialog dialog;
	
	/**
	 * This constructs a new {@link TransactionImporter} object.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 * 
	 * @param apiParserProvider
	 * 		A provider of {@link ApiParser}s.
	 * 
	 * @param importerThreadProvider
	 * 		A provider of {@link ImporterThread} objects.
	 * 
	 * @param importManager
	 * 		The {@link ImportManager}.
	 * 
	 * @param inventoryManager
	 * 		The {@link ImportManager}.
	 * 
	 * @param dialog
	 * 		The {@link BugReportDialog}.
	 */
	@Inject
	public TransactionImporter(
			Database database, 
			Provider<ApiParser> apiParserProvider, 
			Provider<ImporterThread> importerThreadProvider, 
			ImportManager importManager,
			InventoryManager inventoryManager, 
			BugReportDialog dialog) {
		
		super(database, apiParserProvider, importerThreadProvider, importManager, Api.CHAR_WALLET_TRANSACTIONS);
		
		this.inventoryManager = inventoryManager;
		this.dialog = dialog;
	}

	@Override
	public void parseApi(Node node, ApiKey apiKey) {
		int inserted = 0;
		
		final int brokerRelation = getSkillLevel(apiKey.getCharacterId(), 3446);
		final int accounting = getSkillLevel(apiKey.getCharacterId(), 16622);
		final Node root = node.get("result").get("rowset");
		for (int i = root.size() - 1; i >= 0; i--) {
			if (processRow(root, i, apiKey, brokerRelation, accounting)) {
				inserted++;
			}
		}
		
		LOG.info("Inserted " + inserted + " new transactions.");
	}
	
	private int getSkillLevel(long characterId, long skillId) {
		int level = 0;
		Skill skill = getSkill(new SkillIdentifier(characterId, skillId));
		if (skill != null) {
			level = skill.getLevel();
		}
		
		return level;
	}
	
	/**
	 * This method returns the level of the specified skill.
	 * 
	 * @param id
	 * 		The identifier of the skill and character.
	 * 
	 * @return
	 * 		The level of the specified skill.
	 */
	@Transactional
	Skill getSkill(final SkillIdentifier id) {
		Session session = getDatabase().getCurrentSession();
		return (Skill) session.get(Skill.class, id);
	}

	private boolean processRow(Node root, int i, ApiKey apiKey, int brokerRelation, int accounting) {
		if (root.get(i) instanceof Node) {
			Node row = (Node) root.get(i);
			if (row.getTag().equals("row")) {
				return persistChangeData(row, apiKey, brokerRelation, accounting);
			}
		}
		return false;
	}

	/**
	 * This method persists a new wallet transaction to the database.
	 * 
	 * @param row
	 * 		The {@link Node} containing the transaction data.
	 * 
	 * @param apiKey
	 * 		The apiKey used to retrieve this transaction from the API.
	 * 
	 * @param brokerRelation
	 * 		The level of the broker relation skill.
	 * 
	 * @param accounting
	 * 		The level of the accounting skill.
	 * 
	 * @return
	 * 		True if the transaction was persisted, or false if it was not.
	 */
	@Transactional
	boolean persistChangeData(Node row, ApiKey apiKey, int brokerRelation, int accounting) {
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
			
			BigDecimal taxes = (BigDecimal.valueOf(0.01).subtract(BigDecimal.valueOf(0.0005)
					.multiply(BigDecimal.valueOf(brokerRelation))))
					.divide(BigDecimal.valueOf(Math.exp(((BigDecimal.valueOf(0.1).multiply(factionStanding))
					.add(BigDecimal.valueOf(0.04).multiply(corporationStanding)).doubleValue()
					))), 3, RoundingMode.HALF_UP);
			
			if (!isBuy) {
				taxes = taxes.add(BigDecimal.valueOf(0.01).subtract(BigDecimal.valueOf(0.001)
						.multiply(BigDecimal.valueOf(accounting))));
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
				transaction.setCharacterId(apiKey.getCharacterId());
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
				
				return true;
			}
		} 
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
		}
		
		return false;
	}

	/**
	 * This method retrieves the corporation's standing of this character with a certain station's owner.
	 * 
	 * @param apiKey
	 * 		The {@link ApiKey} containing the character id.
	 * 
	 * @param stationId
	 * 		The station id.
	 * 
	 * @return
	 * 		The corporation's standing of this character with a certain station's owner.
	 */
	@Transactional
	BigDecimal getCorporationStanding(ApiKey apiKey, long stationId) {
		Session session = getDatabase().getCurrentSession();
		Station station = (Station) session.get(Station.class, stationId);
		if (station != null) {
			long characterId = apiKey.getCharacterId();
			StandingIdentifier id = new StandingIdentifier(characterId, station.getCorporationId());
			Standing standing = (Standing) session.get(Standing.class, id);
			if (standing != null) {
				return standing.getStanding();
			}
		}
		return BigDecimal.ZERO;
	}

	/**
	 * This method retrieves the faction standings for a certain region.
	 * 
	 * @param apiKey
	 * 		The {@link ApiKey} containing the character id.
	 * 
	 * @param mapRegionId
	 * 		The id of the region.
	 * 
	 * @return
	 * 		The faction standing of this character with this region's empire.
	 */
	@Transactional
	BigDecimal getFactionStanding(ApiKey apiKey, long mapRegionId) {
		Session session = getDatabase().getCurrentSession();
		MapRegion region = (MapRegion) session.get(MapRegion.class, mapRegionId);
		if (region != null) {
			long characterId = apiKey.getCharacterId();
			StandingIdentifier id = new StandingIdentifier(characterId, region.getFactionId());
			Standing standing = (Standing) session.get(Standing.class, id);
			return standing.getStanding();
		}
		return BigDecimal.ZERO;
	}
	
	@Override
	public final boolean isReady() {
		return inventoryManager.isIdle();
	}

}
