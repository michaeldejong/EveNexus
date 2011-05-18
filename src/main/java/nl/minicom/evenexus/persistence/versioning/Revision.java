package nl.minicom.evenexus.persistence.versioning;

import org.apache.commons.lang.Validate;


public abstract class Revision implements IRevision {
	
	private final int revisionNumber;
	
	public Revision(int revision) {
		Validate.isTrue(revision >= 0);
		this.revisionNumber = revision;
	}

	public int getRevisionNumber() {
		return revisionNumber;
	}

}
