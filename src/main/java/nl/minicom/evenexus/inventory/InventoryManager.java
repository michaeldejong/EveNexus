package nl.minicom.evenexus.inventory;

import java.math.BigInteger;
import java.util.List;

import nl.minicom.evenexus.persistence.Query;

import org.hibernate.Session;

public class InventoryManager {
	
	public InventoryManager() {
		List<BigInteger> typeIds = queryUnprocessedTypeIds();
		for (BigInteger typeId : typeIds) {
			new Thread(new InventoryWorker(typeId.longValue())).start();
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<BigInteger> queryUnprocessedTypeIds() {
		return new Query<List<BigInteger>>() {
			@Override
			protected List<BigInteger> doQuery(Session session) {
				String query = "SELECT DISTINCT(typeID) FROM transactions WHERE price > 0 AND remaining > 0";
				return (List<BigInteger>) session.createSQLQuery(query).list();
			}
		}.doQuery();
	}

}
