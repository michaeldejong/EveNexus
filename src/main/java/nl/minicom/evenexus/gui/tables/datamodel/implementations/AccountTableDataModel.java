package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.persistence.Database;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;


public class AccountTableDataModel implements ITableDataModel {

	private final Database database;
	
	@Inject
	public AccountTableDataModel(Database database) {
		this.database = database;
	}
	
	@Override
	public List<Object[]> reload() {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT ");
		builder.append("	keyid, ");
		builder.append("	verificationcode, ");
		builder.append("	characterid, ");
		builder.append("	charactername, ");
		builder.append("	corporationid, ");
		builder.append("	corporationname ");
		builder.append("FROM ");
		builder.append("	apikeys ");
		builder.append("ORDER BY ");
		builder.append("	charactername ASC");
		
		String sql = builder.toString();
		List<Object[]> result = new ArrayList<Object[]>();
		Session session = database.getCurrentSession();
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

	@Override
	public String[] getFields() {
		return new String[] {
				"keyid",
				"verificationcode",
				"characterid",
				"charactername",
				"corporationid",
				"corporationname"
		};
	}

}
