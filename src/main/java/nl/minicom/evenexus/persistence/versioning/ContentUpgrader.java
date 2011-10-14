package nl.minicom.evenexus.persistence.versioning;

/**
 * This class executes a database content upgrade.
 * 
 * @author michael
 */
public class ContentUpgrader extends RevisionCollection {

	/**
	 * This constructs a new {@link ContentUpgrader} object.
	 * 
	 * @throws Exception
	 * 		When the revision could not be executed.
	 */
	public ContentUpgrader() throws Exception {
		super("content");
		super.registerRevision(new JsonContentRevision("content.json"));
	}
	
	@Override
	public void registerRevision(IRevision revision) {
		throw new UnsupportedOperationException("Not allowed to add new revisions this way!");
	}

}
