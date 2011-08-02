package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "importers")
public class Importer implements Serializable {
	
	private static final long serialVersionUID = 1380783496560770755L;
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String COOLDOWN = "cooldown";
	public static final String PATH = "path";
	
	@Id
	@Column(name = ID, nullable = false)
	private long id;

	@Column(name = NAME, nullable = false)
	private String name;

	@Column(name = COOLDOWN, nullable = false)
	private long cooldown;

	@Column(name = PATH, nullable = false)
	private String path;
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCooldown() {
		return cooldown;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
