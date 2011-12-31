package nl.minicom.evenexus.persistence.versioning;

import java.util.List;

import javax.inject.Inject;

import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Ignore;

/**
 * This class provides utility method to the other revision test classes.
 * 
 * @author michael
 */
@Ignore
public class RevisionUtil {

	private final Database database;
	
	/**
	 * Constructs a new {@link RevisionUtil} object.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 */
	@Inject
	public RevisionUtil(Database database) {
		this.database = database;
	}

	/**
	 * This method loads the test tabel.
	 * 
	 * @return
	 * 		A {@link List} of objects in the test table.
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	protected List<Object[]> loadTestTable() {
		Session session = database.getCurrentSession();
		return session.createSQLQuery("SELECT id, value FROM testtable").list();
	}

	/**
	 * This method drops all objects in the database.
	 */
	@Transactional
	protected void dropDatabase() {
		Session session = database.getCurrentSession();
		session.createSQLQuery("DROP ALL OBJECTS").executeUpdate();
		session.flush();
	}
	
	/**
	 * This method lists all column names present in a specific table.
	 * 
	 * @param tableName
	 * 		The table to look up.
	 * 
	 * @return
	 * 		An array of colunm names.
	 */
	@Transactional
	protected Object[] listDbColumnNames(final String tableName) {
		Session session = database.getCurrentSession();
		String sql = "SHOW COLUMNS FROM " + tableName;
		SQLQuery query = session.createSQLQuery(sql);
		return query.list().toArray();
	}
	
}
