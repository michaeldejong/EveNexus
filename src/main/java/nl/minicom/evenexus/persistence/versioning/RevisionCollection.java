package nl.minicom.evenexus.persistence.versioning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;

public class RevisionCollection {

	private final List<IRevision> revisions;
	private final String revisionType;
	
	public RevisionCollection(String revisionType) {
		Validate.notNull(revisionType);
		
		this.revisions = new ArrayList<IRevision>();
		this.revisionType = revisionType;
	}
	
	public void registerRevision(IRevision revision) {
		assert revision != null : "Cannot add a NULL as a revision to the collection";

		if (!revisions.isEmpty()) {
			int lastRevisionNumber = getLastRevisionNumber();
			
			if (lastRevisionNumber == revision.getRevisionNumber()) {
				throw new IllegalArgumentException("Revision is already registered!");
			}
			else if (lastRevisionNumber > revision.getRevisionNumber()) {
				throw new IllegalArgumentException("Illegal ordering of revisions!");
			}
		}
		
		revisions.add(revision);
	}
	
	public String getRevisionType() {
		return revisionType;
	}
	
	public List<IRevision> getRevisions() {
		return Collections.unmodifiableList(revisions);
	}

	public boolean isEmpty() {
		return revisions.isEmpty();
	}

	public int getLastRevisionNumber() {
		if (revisions.isEmpty()) {
			return -1;
		}
		return revisions.get(revisions.size() - 1).getRevisionNumber();
	}
	
}
