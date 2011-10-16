package nl.minicom.evenexus.persistence.versioning;

import java.util.List;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RevisionCollectionTest {

	private static final Logger LOG = LoggerFactory.getLogger(RevisionCollectionTest.class);
	
	private RevisionCollection instance;
	private String revisionType;
	
	/**
	 * This method sets up a test case on which we can run tests.
	 */
	@Before
	public void setup() {
		revisionType = "database";
		instance = new RevisionCollection(revisionType);
	}
	
	private Revision createRevision(int revisionNumber) {
		return new Revision(revisionNumber) {
			public void execute(Session session) {
				LOG.debug("Executing revision: " + getRevisionNumber());
			}
		};
	}
	
	/**
	 * Test that we get the same revisionType via the getRevisionType() method
	 * as we provided via the constructor to the {@link RevisionCollection}.
	 */
	@Test
	public void testRevisionType() {
		Assert.assertEquals(revisionType, instance.getRevisionType());
	}
	
	/**
	 * Test that when we add {@link IRevision} objects with out of order
	 * revision numbers, an {@link IllegalArgumentException} is thrown. 
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testRegisterRevisionsOutOfOrder() {
		instance.registerRevision(createRevision(5));
		instance.registerRevision(createRevision(4));
	}
	
	/**
	 * Check that an {@link IllegalArgumentException} is thrown when 
	 * we try to add another {@link IRevision} with an already known revision number.
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testRegisterRevisionNumberTwice() {
		instance.registerRevision(createRevision(5));
		instance.registerRevision(createRevision(5));
	}
	
	/**
	 * Assert that when {@link IRevision} objects are added in order of their respective
	 * revisionNumbers, that the test passes and no exceptions are thrown.
	 */
	@Test
	public void testRegisterRevisionsInOrder() {
		instance.registerRevision(createRevision(4));
		instance.registerRevision(createRevision(5));
	}
	
	/**
	 * Make sure that registering a null results in an {@link AssertionError}.
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testRegisterNullValue() {
		instance.registerRevision(null);
	}
	
	/**
	 * Make sure that when we add an {@link IRevision} to the {@link RevisionCollection}, that
	 * the size() method returns a correct value.
	 */
	@Test
	public void testGetRevisions() {
		List<IRevision> revisions = instance.getRevisions();
		instance.registerRevision(createRevision(4));		
		Assert.assertEquals(revisions.size(), 1);
	}
	
	/**
	 * Make sure that we can not illegally register {@link IRevision} object using
	 * the getRevisions() method. This should return an unmodifiable {@link List} of
	 * {@link IRevisions}. So we're expecting the {@link UnsupportedOperationException}
	 * to be thrown here.
	 */
	@Test(expected = java.lang.UnsupportedOperationException.class)
	public void testAddRevisionIllegally() {
		List<IRevision> revisions = instance.getRevisions();
		revisions.add(createRevision(1));
	}
	
	/**
	 * Test that by default a {@link RevisionCollection} is empty.
	 */
	@Test
	public void testIsEmpty() {
		Assert.assertTrue(instance.isEmpty());
	}
	
	/**
	 * Test that after adding an {@link IRevision} to the {@link RevisionCollection} 
	 * it is no longer empty.
	 */	
	@Test
	public void testIsNotEmpty() {
		Assert.assertTrue(instance.isEmpty());
		instance.registerRevision(createRevision(4));
		Assert.assertFalse(instance.isEmpty());
	}
	
	/**
	 * Assert that the lastRevisionNumber() method returns -1 when no 
	 * {@link IRevision} objects have been registered.
	 */
	@Test
	public void testLastRevisionNumberWithoutRegisteredRevisions() {
		Assert.assertEquals(instance.getLastRevisionNumber(), -1);
	}
	
	/**
	 * Test that the lastRevisionNumber() method returns a correct value,
	 * when {@link IRevision} objects have been registered.
	 */
	@Test
	public void testLastRevisionNumberWithRegisteredRevisions() {
		instance.registerRevision(createRevision(4));
		instance.registerRevision(createRevision(5));
		
		Assert.assertEquals(instance.getLastRevisionNumber(), 5);
	}
	
}
