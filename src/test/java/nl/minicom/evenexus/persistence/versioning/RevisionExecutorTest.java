package nl.minicom.evenexus.persistence.versioning;

import java.util.List;

import junit.framework.Assert;
import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.Version;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RevisionExecutorTest {

	private String databaseType;
	private RevisionCollection collection;
	private RevisionCollection invalidCollection;
	private RevisionExecutor executor;
	private RevisionExecutor invalidExecutor;
	
	@Before
	public void setup() {
		databaseType = "test";
		collection = new RevisionCollection(databaseType);
		executor = new RevisionExecutor(collection);

		invalidCollection = new RevisionCollection(databaseType);
		invalidExecutor = new RevisionExecutor(invalidCollection);
		
		Revision firstRevision = new Revision(1) {
			public void execute(Session session) {
				StringBuilder builder = new StringBuilder();
				builder.append("CREATE TABLE testtable (");
				builder.append("id INT NOT NULL,");
				builder.append("value VARCHAR(255) NOT NULL,");
				builder.append("PRIMARY KEY (`id`))");
				session.createSQLQuery(builder.toString()).executeUpdate();
			}
		};

		invalidCollection.registerRevision(firstRevision);
		
		collection.registerRevision(firstRevision);
		collection.registerRevision(new Revision(2) {
			public void execute(Session session) {
				StringBuilder builder = new StringBuilder();
				builder.append("INSERT INTO testtable VALUES (1, 'Hello World!')");
				session.createSQLQuery(builder.toString()).executeUpdate();
			}
		});
		
		dropDatabase();
	}

	@After
	public void tearDown() {
		dropDatabase();
	}
	
	@Test
	public void testConstructorWithValidInput() {
		new RevisionExecutor(collection);
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testConstructorWithInvalidInput() {
		new RevisionExecutor(null);
	}
	
	@Test
	public void testExecution() {
		Version dbVersion = executor.execute();
		checkDatabaseAndCleanup(dbVersion);
	}
	
	@Test
	public void testDoubleExecution() {
		// Execute once.
		Version dbVersion = executor.execute();
		
		// Execute twice.
		dbVersion = executor.execute();
		
		checkDatabaseAndCleanup(dbVersion);
	}
	
	@Test(expected = java.lang.IllegalStateException.class)
	public void testInvalidVersioning() {
		// Execute the complete list of revision.
		executor.execute();
		
		/*
		 * Execute only the first revision.
		 * This should fail because this executor has only one revision while we already
		 * have executed two upon the database. This cannot be a valid executor.
		 */
		invalidExecutor.execute();
	}

	@SuppressWarnings("unchecked")
	private void checkDatabaseAndCleanup(Version dbVersion) {
		Assert.assertEquals(dbVersion.getRevision(), 2);
		Assert.assertEquals(dbVersion.getType(), databaseType);
		
		List<Object[]> results = new Query<List<Object[]>>() {
			@Override
			protected List<Object[]> doQuery(Session session) {
				return (List<Object[]>) session.createSQLQuery("SELECT id, value FROM testtable").list();
			}
		}.doQuery();
		
		Assert.assertEquals(results.size(), 1);
		Assert.assertEquals(results.get(0)[0], 1);
		Assert.assertEquals(results.get(0)[1], "Hello World!");
	}
	
	private void dropDatabase() {
		new Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				session.createSQLQuery("DROP ALL OBJECTS").executeUpdate();
				return null;
			}
		}.doQuery();
	}
	
}
