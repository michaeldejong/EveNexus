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
	
	@Id
	@Column(name = ID, nullable = false)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "cooldown", nullable = false)
	private long cooldown;

	@Column(name = "path", nullable = false)
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
