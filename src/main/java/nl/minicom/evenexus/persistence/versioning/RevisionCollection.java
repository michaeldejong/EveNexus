package nl.minicom.evenexus.persistence.versioning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * The {@link RevisionCollection} class holds many {@link IRevision}s, 
 * which will be executed sequentially if they have not yet been executed.
 * 
 * @author michael
 */
public class RevisionCollection {

	private final List<IRevision> revisions;
	private final String revisionType;
	
	/**
	 * This constructs a new {@link RevisionCollection} object.
	 * 
	 * @param revisionType
	 * 		The type of {@link RevisionCollection} this is.
	 */
	public RevisionCollection(String revisionType) {
		Preconditions.checkNotNull(revisionType);
		
		this.revisions = new ArrayList<IRevision>();
		this.revisionType = revisionType;
	}
	
	/**
	 * This method registers a new {@link IRevision} with this {@link RevisionCollection}.
	 * 
	 * @param revision
	 * 		The {@link IRevision} to add to the collection.
	 */
	public void registerRevision(IRevision revision) {
		Preconditions.checkArgument(revision != null, "Cannot add a NULL as a revision to the collection");

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
	
	/**
	 * @return
	 * 		The type of revision.
	 */
	public String getRevisionType() {
		return revisionType;
	}
	
	/**
	 * @return
	 * 		An unmodifiable list of all revisions in this {@link RevisionCollection}.
	 */
	public List<IRevision> getRevisions() {
		return Collections.unmodifiableList(revisions);
	}

	/**
	 * @return
	 * 		True if this {@link RevisionCollection} is empty.
	 */
	public boolean isEmpty() {
		return revisions.isEmpty();
	}

	/**
	 * @return
	 * 		The revision number of the last revision in this {@link RevisionCollection}.
	 */
	public int getLastRevisionNumber() {
		if (revisions.isEmpty()) {
			return -1;
		}
		return revisions.get(revisions.size() - 1).getRevisionNumber();
	}
	
}
