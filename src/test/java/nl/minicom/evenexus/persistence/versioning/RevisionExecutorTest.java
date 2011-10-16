package nl.minicom.evenexus.persistence.versioning;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;
import nl.minicom.evenexus.TestModule;
import nl.minicom.evenexus.persistence.dao.Version;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class RevisionExecutorTest {

	private String databaseType;
	private RevisionExecutor executor;
	private RevisionCollection collection;
	private RevisionCollection invalidCollection;
	private RevisionUtil util;
	
	@Before
	public void setup() {
		databaseType = "database";

		Injector injector = Guice.createInjector(new TestModule());
		executor = injector.getInstance(RevisionExecutor.class);
		util = injector.getInstance(RevisionUtil.class);
		collection = new RevisionCollection(databaseType);
		invalidCollection = new RevisionCollection(databaseType);
		
		Revision firstRevision = new Revision(1) {
			public void execute(Connection connection) throws SQLException {
				StringBuilder builder = new StringBuilder();
				builder.append("CREATE TABLE testtable (");
				builder.append("id INT NOT NULL, ");
				builder.append("value VARCHAR(255) NOT NULL, ");
				builder.append("PRIMARY KEY (id))");
				connection.createStatement().execute(builder.toString());
			}
		};

		invalidCollection.registerRevision(firstRevision);
		
		collection.registerRevision(firstRevision);
		collection.registerRevision(new Revision(2) {
			public void execute(Connection connection) throws SQLException {
				StringBuilder builder = new StringBuilder();
				builder.append("INSERT INTO testtable VALUES (1, 'Hello World!')");
				connection.createStatement().execute(builder.toString());
			}
		});
		
		util.dropDatabase();
	}

	@After
	public void tearDown() {
		util.dropDatabase();
	}
	
	@Test(expected = java.lang.NullPointerException.class)
	public void testExecutorWithNull() throws SQLException {
		executor.execute(null, true);
	}
	
	@Test
	public void testExecution() throws SQLException {
		Version dbVersion = executor.execute(collection, true);
		checkDatabaseAndCleanup(dbVersion);
	}
	
	@Test
	public void testDoubleExecution() throws SQLException {
		// Execute once.
		Version dbVersion = executor.execute(collection, true);
		
		// Execute twice.
		dbVersion = executor.execute(collection, true);
		
		checkDatabaseAndCleanup(dbVersion);
	}
	
	@Test(expected = java.lang.IllegalStateException.class)
	public void testInvalidVersioning() throws SQLException {
		// Execute the complete list of revision.
		executor.execute(collection, true);
		
		/*
		 * Execute only the first revision.
		 * This should fail because this executor has only one revision while we already
		 * have executed two upon the database. This cannot be a valid executor.
		 */
		executor.execute(invalidCollection, true);
	}

	private void checkDatabaseAndCleanup(Version dbVersion) {
		Assert.assertEquals(dbVersion.getRevision(), 2);
		Assert.assertEquals(dbVersion.getType(), databaseType);
		
		List<Object[]> results = util.loadTestTable();
		
		Assert.assertEquals(results.size(), 1);
		Assert.assertEquals(results.get(0)[0], 1);
		Assert.assertEquals(results.get(0)[1], "Hello World!");
	}
	
}
