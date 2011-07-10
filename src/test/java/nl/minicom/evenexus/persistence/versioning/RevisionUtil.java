package nl.minicom.evenexus.persistence.versioning;

import java.util.List;

import javax.inject.Inject;

import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Ignore;

@Ignore
public class RevisionUtil {

	private final Database database;
	
	@Inject
	public RevisionUtil(Database database) {
		this.database = database;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	protected List<Object[]> loadTestTable() {
		Session session = database.getCurrentSession();
		return session.createSQLQuery("SELECT id, value FROM testtable").list();
	}

	@Transactional
	protected void dropDatabase() {
		Session session = database.getCurrentSession();
		session.createSQLQuery("DROP ALL OBJECTS").executeUpdate();
		session.flush();
	}
	
	@Transactional
	protected Object[] listDbColumnNames(final String tableName) {
		Session session = database.getCurrentSession();
		String sql = "SHOW COLUMNS FROM " + tableName;
		SQLQuery query = session.createSQLQuery(sql);
		return query.list().toArray();
	}
	
}
