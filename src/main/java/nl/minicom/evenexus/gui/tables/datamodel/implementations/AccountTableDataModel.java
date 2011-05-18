package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;


public class AccountTableDataModel implements ITableDataModel {

	@Override
	public List<Object[]> reload() {
		return new Query<List<Object[]>>() {
			@Override
			protected List<Object[]> doQuery(Session session) {
				String sql = "SELECT name, userid, apikey, charid FROM apisettings ORDER BY name ASC";
				List<Object[]> result = new ArrayList<Object[]>();
				SQLQuery query = session.createSQLQuery(sql);
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
				"name",
				"userid",
				"apikey",
				"charid"
		};
	}

}
