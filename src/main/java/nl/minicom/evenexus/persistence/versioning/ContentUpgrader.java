package nl.minicom.evenexus.persistence.versioning;

public class ContentUpgrader extends RevisionCollection {

	public ContentUpgrader() throws Exception {
		super("content");
		
		super.registerRevision(new JsonContentRevision("content.json"));
	}
	
	@Override
	public void registerRevision(IRevision revision) {
		throw new UnsupportedOperationException("Not allowed to add new revisions this way!");
	}

}
