package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity is used to keep track of which revisions have been executed on the database.
 *
 * @author michael
 */
@Entity
@Table(name = "versioning")
public class Version implements Serializable {
	
	private static final long serialVersionUID = -2962367693722376311L;

	public static final String TYPE = "type";
	public static final String VERSION = "version";
	public static final String REVISION = "revision";
	
	@Id
	@Column(name = TYPE)
	private String type;
	
	@Column(name = VERSION)
	private int version;
	
	@Column(name = REVISION)
	private int revision;

	/**
	 * @return
	 * 		The type of versioning.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * This method sets the type of versioning.
	 * 
	 * @param type
	 * 		The versioning type.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return
	 * 		The version number of this {@link Version} object.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * This method sets the version number of this {@link Version} object.
	 * 
	 * @param version
	 * 		The new version number.
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return
	 * 		The revision number of this {@link Version} object.
	 */
	public int getRevision() {
		return revision;
	}

	/**
	 * This method sets the revision number of this {@link Version} object.
	 * 
	 * @param revision
	 * 		The revision number of this {@link Version} object.
	 */
	public void setRevision(int revision) {
		this.revision = revision;
	}
	
}
