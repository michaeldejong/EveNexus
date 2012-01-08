package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;


/**
 * This class represents the table data model for data on accounts.
 *
 * @author michael
 */
public class AccountTableDataModel implements ITableDataModel {

	private final Database database;
	
	/**
	 * This constructs a new {@link AccountTableDataModel} object.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 */
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

	@Override
	@Transactional
	public void delete(Map<String, Object> row) {
		if (!row.containsKey("KeyID") || row.get("KeyID") == null) {
			throw new IllegalStateException("Row is not valid!");
		}
		
		Session session = database.getCurrentSession();
		StringBuilder builder = new StringBuilder();
		builder.append("DELETE FROM ");
		builder.append("	apikeys ");
		builder.append("WHERE ");
		builder.append("	keyid = ? ");
		builder.append("LIMIT 1");
		
		session.createSQLQuery(builder.toString()).setBigInteger(0, (BigInteger) row.get("KeyID")).executeUpdate();
	}

}
