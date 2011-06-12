package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.gui.tables.datamodel.ITypeNameFilter;
import nl.minicom.evenexus.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BuyOrdersTableDataModel implements ITableDataModel, ITypeNameFilter {
	
	private static final Logger LOG = LoggerFactory.getLogger(BuyOrdersTableDataModel.class);

	private String typeName;
	
	public BuyOrdersTableDataModel() {
		setTypeName(null);
	}
	
	@Override
	public List<Object[]> reload() {
		try {
			return loadTable();
		}
		catch (HibernateException e) {
			LOG.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}

	private List<Object[]> loadTable() throws HibernateException {
		final String sql = new StringBuilder()
		.append("SELECT ")
		.append("marketorders.volentered AS volentered, ")
		.append("marketorders.volremaining AS volremaining, ")
		.append("marketorders.minvolume AS minvolume, ")
		.append("marketorders.escrow AS escrow, ")
		.append("marketorders.issued AS issued, ")
		.append("marketorders.price AS price, ")
		.append("marketorders.bid AS bid, ")
		.append("invtypes.typeName AS typeName, ")
		.append("price * -1 AS orderPrice, ")
		.append("stastations.stationName AS stationName ")
		.append("FROM marketorders, invtypes, stastations ")
		.append("WHERE marketorders.bid = 1 ")
		.append("AND marketorders.orderState = 0 ")
		.append("AND LCASE(invtypes.typeName) LIKE ? ")
		.append("AND marketorders.typeID = invtypes.typeID ")
		.append("AND marketorders.stationID = stastations.stationID ")
		.append("ORDER BY invtypes.typeName ASC, stastations.stationName ASC")
		.toString();
		
		return new Query<List<Object[]>>() {
			@Override
			protected List<Object[]> doQuery(Session session) {
				List<Object[]> result = new ArrayList<Object[]>();
				SQLQuery query = session.createSQLQuery(sql);
				query.setString(0, typeName);
				ScrollableResults resultSet = query.scroll();
				if (resultSet.first()) {
					do {
						result.add(resultSet.get().clone());
					}
					while (resultSet.next());
				}
				return result;
			}
		}.doQuery();
	}

	@Override
	public String[] getFields() {
		return new String[] {
				"volentered", 
				"volremaining",
				"minvolume",
				"escrow",
				"issued",
				"price",
				"bid",
				"typeName",
				"orderPrice",
				"stationName"
		};
	}

	@Override
	public void setTypeName(String name) {
		if (name == null || name.isEmpty()) {
			typeName = "%";
			return;
		}
		typeName = "%" + name.toLowerCase() + "%";
	}

}
