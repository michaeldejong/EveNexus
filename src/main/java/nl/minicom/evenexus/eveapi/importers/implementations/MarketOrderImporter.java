package nl.minicom.evenexus.eveapi.importers.implementations;


import java.math.BigDecimal;

import nl.minicom.evenexus.eveapi.ApiParser;
import nl.minicom.evenexus.eveapi.ApiParser.Api;
import nl.minicom.evenexus.eveapi.ApiServerManager;
import nl.minicom.evenexus.eveapi.importers.ImportManager;
import nl.minicom.evenexus.eveapi.importers.ImporterTask;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.MarketOrder;
import nl.minicom.evenexus.utils.TimeUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.mortbay.xml.XmlParser.Node;


public class MarketOrderImporter extends ImporterTask {
	
	private static final Logger logger = LogManager.getRootLogger();
	
	public MarketOrderImporter(ApiServerManager apiServerManager, ImportManager importManager, ApiKey settings) {
		super(apiServerManager, importManager, Api.CHAR_MARKET_ORDERS, settings);
	}

	public void parseApi(ApiParser parser) throws Exception {
		final Node root = parser.getRoot().get("result").get("rowset");
		new Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				MarketOrder.markAllActiveAsExpired(session, getApiKey().getCharacterID());
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
			logger.error(e.getLocalizedMessage(), e);
		}
	}

}
