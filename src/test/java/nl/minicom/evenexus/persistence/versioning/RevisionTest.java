package nl.minicom.evenexus.persistence.versioning;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RevisionTest {

	private int revisionNumber;
	private Revision testRevision;
	private boolean hasRun;
	
	/**
	 * This method prepares a test case on which we can run all our tests.
	 */
	@Before
	public void setup() {
		hasRun = false;
		revisionNumber = 1337;
		testRevision = createRevision(revisionNumber);
	}
	
	/**
	 * A small convenience method to create a revision.
	 * 
	 * @param number the revision number
	 * @return a {@link Revision} which, when executed sets 'hasRun' to true.
	 */
	private Revision createRevision(int number) {
		return new Revision(number) {
			@Override
			public void execute(Connection conn) throws SQLException {
				hasRun = true;
			}
		};
	}
	
	/**
	 * Test that we the execute() method actually executes when called.
	 * 
	 * @throws SQLException
	 * 		If the upgrade could not be executed. 
	 */
	@Test
	public void testRunning() throws SQLException {
		Assert.assertFalse("hasRun should be false!", hasRun);
		testRevision.execute(null);
		Assert.assertTrue("hasRun should be true!", hasRun);
	}
	
	/**
	 * Check that we get the same revisionNumber back as we put in via the constructor.
	 */
	@Test
	public void testRevisionNumber() {
		Assert.assertEquals(testRevision.getRevisionNumber(), revisionNumber);
	}
	
	/**
	 * Check that the constructor throws an Exception when entering a negative revision number.
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testNegativeRevisionNumber() {		
		createRevision(-1);
	}
	
	/**
	 * Check that the constructor does not throw an exception when entering a zero-positive number.
	 */
	@Test
	public void testZeroPositiveRevisionNumber() {		
		createRevision(0);
		createRevision(1);
	}
	
}
