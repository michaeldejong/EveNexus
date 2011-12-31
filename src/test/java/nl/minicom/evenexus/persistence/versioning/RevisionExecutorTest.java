package nl.minicom.evenexus.persistence.versioning;

import java.util.List;

import junit.framework.Assert;
import nl.minicom.evenexus.TestModule;
import nl.minicom.evenexus.persistence.dao.Version;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * The {@link RevisionExecutorTest} class is responsible for testing the consistency of the {@link RevisionExecutor}.
 * 
 * @author michael
 */
public class RevisionExecutorTest {

	private String databaseType;
	private RevisionExecutor executor;
	private RevisionCollection collection;
	private RevisionCollection invalidCollection;
	private RevisionUtil util;
	
	/**
	 * This method sets up each test.
	 */
	@Before
	public void setup() {
		databaseType = "test";

		Injector injector = Guice.createInjector(new TestModule());
		executor = injector.getInstance(RevisionExecutor.class);
		util = injector.getInstance(RevisionUtil.class);
		collection = new RevisionCollection(databaseType);
		invalidCollection = new RevisionCollection(databaseType);
		
		Revision firstRevision = new Revision(1) {
			public void execute(Session session) {
				StringBuilder builder = new StringBuilder();
				builder.append("CREATE TABLE testtable (");
				builder.append("id INT NOT NULL, ");
				builder.append("value VARCHAR(255) NOT NULL, ");
				builder.append("PRIMARY KEY (id))");
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
		
		util.dropDatabase();
	}

	/**
	 * This method cleans up after each test.
	 */
	@After
	public void tearDown() {
		util.dropDatabase();
	}
	
	/**
	 * This tests that an {@link RevisionExecutor} with a NULL argument throws a {@link NullPointerException}.
	 */
	@Test(expected = java.lang.NullPointerException.class)
	public void testExecutorWithNull() {
		executor.execute(null);
	}
	
	/**
	 * This method tests the basic execution of the {@link RevisionExecutor}.
	 */
	@Test
	public void testExecution() {
		Version dbVersion = executor.execute(collection);
		checkDatabaseAndCleanup(dbVersion);
	}
	
	/**
	 * This method test that the same {@link RevisionCollection} can be executed twice.
	 */
	@Test
	public void testDoubleExecution() {
		// Execute once.
		Version dbVersion = executor.execute(collection);
		
		// Execute twice.
		dbVersion = executor.execute(collection);
		
		checkDatabaseAndCleanup(dbVersion);
	}
	
	/**
	 * This method test that an invalid {@link RevisionCollection} will throw an {@link IllegalStateException}.
	 */
	@Test(expected = java.lang.IllegalStateException.class)
	public void testInvalidVersioning() {
		// Execute the complete list of revision.
		executor.execute(collection);
		
		/*
		 * Execute only the first revision.
		 * This should fail because this executor has only one revision while we already
		 * have executed two upon the database. This cannot be a valid executor.
		 */
		executor.execute(invalidCollection);
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
