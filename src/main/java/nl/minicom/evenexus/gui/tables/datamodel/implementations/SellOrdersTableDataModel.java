package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.gui.tables.datamodel.ITypeNameFilter;
import nl.minicom.evenexus.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;


public class SellOrdersTableDataModel implements ITableDataModel, ITypeNameFilter {
	
	private static final Logger logger = LogManager.getRootLogger();

	private String typeName;
	
	public SellOrdersTableDataModel() {
		setTypeName(null);
	}
	
	@Override
	public List<Object[]> reload() {
		try {
			return loadTable();
		}
		catch (HibernateException e) {
			logger.error(e);
		}
		return null;
	}

	private List<Object[]> loadTable() throws HibernateException {
		final String sql = new StringBuilder()
		.append("SELECT ")
		.append("marketorders.volentered, ")
		.append("marketorders.volremaining, ")
		.append("marketorders.minvolume, ")
		.append("marketorders.escrow, ")
		.append("marketorders.price, ")
		.append("marketorders.bid, ")
		.append("marketorders.issued, ")
		.append("invtypes.typeName, ")
		.append("stastations.stationName ")
		.append("FROM marketorders, invtypes, stastations ")
		.append("WHERE marketorders.bid = 0 ")
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
				"marketorders.volentered",
				"marketorders.volremaining",
				"marketorders.minvolume",
				"marketorders.escrow",
				"marketorders.price",
				"marketorders.bid",
				"marketorders.issued",
				"invtypes.typeName",
				"stastations.stationName"
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
