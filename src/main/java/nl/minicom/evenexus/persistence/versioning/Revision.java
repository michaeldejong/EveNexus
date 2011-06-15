package nl.minicom.evenexus.persistence.versioning;

import com.google.common.base.Preconditions;

public abstract class Revision implements IRevision {
	
	private final int revisionNumber;
	
	public Revision(int revision) {
		Preconditions.checkArgument(revision >= 0);
		this.revisionNumber = revision;
	}

	public int getRevisionNumber() {
		return revisionNumber;
	}

}
