package nl.minicom.evenexus.eveapi.importers.implementations;


import java.math.BigDecimal;

import javax.inject.Inject;
import javax.inject.Provider;

import nl.minicom.evenexus.eveapi.ApiParser;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.eveapi.importers.ImporterTask;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.MarketOrder;
import nl.minicom.evenexus.persistence.interceptor.Transactional;
import nl.minicom.evenexus.utils.TimeUtils;

import org.hibernate.Session;
import org.mortbay.xml.XmlParser.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MarketOrderImporter extends ImporterTask {
	
	private static final Logger LOG = LoggerFactory.getLogger(MarketOrderImporter.class);
	
	@Inject
	public MarketOrderImporter(Database database, Provider<ApiParser> apiParserProvider, ImportManager importManager) {
		super(database, apiParserProvider, importManager, Api.CHAR_MARKET_ORDERS);
	}

	@Override
	@Transactional
	public void parseApi(Node node, ApiKey apiKey) {
		Session session = getDatabase().getCurrentSession();
		final Node root = node.get("result").get("rowset");
		MarketOrder.markAllActiveAsExpired(session, apiKey.getCharacterID());
		for (int i = root.size() - 1; i >= 0 ; i--) {
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
			long orderId = Long.parseLong(row.getAttribute("orderID"));
			
			MarketOrder order = new MarketOrder();
			order.setOrderId(orderId);
			order.setCharacterId(Long.parseLong(row.getAttribute("charID")));
			order.setStationId(Long.parseLong(row.getAttribute("stationID")));
			order.setVolumeEntered(Long.parseLong(row.getAttribute("volEntered")));
			order.setVolumeRemaining(Long.parseLong(row.getAttribute("volRemaining")));
			order.setMinimumVolume(Long.parseLong(row.getAttribute("minVolume")));
			order.setOrderState(Integer.parseInt(row.getAttribute("orderState")));
			order.setTypeId(Long.parseLong(row.getAttribute("typeID")));
			order.setRange(Integer.parseInt(row.getAttribute("range")));
			order.setAccountKey(Integer.parseInt(row.getAttribute("accountKey")));
			order.setDuration(Integer.parseInt(row.getAttribute("duration")));
			order.setEscrow(BigDecimal.valueOf(Double.valueOf(row.getAttribute("escrow"))));
			order.setPrice(BigDecimal.valueOf(Double.valueOf(row.getAttribute("price"))));
			order.setBid("1".equals(row.getAttribute("bid")));
			order.setIssued(TimeUtils.convertToTimestamp(row.getAttribute("issued")));
			session.saveOrUpdate(order);
		} 
		catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
	}

}
