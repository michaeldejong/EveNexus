package nl.minicom.evenexus.persistence.versioning;

import com.google.common.base.Preconditions;

/**
 * This class is the default implementation of the {@link IRevision} interface.
 * 
 * @author michael
 */
public abstract class Revision implements IRevision {
	
	private final int revisionNumber;
	
	/**
	 * This constructs a new {@link Revision} object.
	 * 
	 * @param revision
	 * 		The revision number.
	 */
	public Revision(int revision) {
		Preconditions.checkArgument(revision >= 0);
		this.revisionNumber = revision;
	}

	/**
	 * @return
	 * 		The revision number.
	 */
	public int getRevisionNumber() {
		return revisionNumber;
	}

}
