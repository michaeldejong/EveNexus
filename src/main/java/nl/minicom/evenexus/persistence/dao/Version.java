package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}
	
}
