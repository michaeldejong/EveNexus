package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The {@link Importer} class is an entity which contains information about API importers.
 *
 * @author michael
 */
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
	
	/**
	 * @return
	 * 		The id of the importer.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return
	 * 		The name of the importer.
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method sets the name of the importer.
	 * 
	 * @param name
	 * 		The new name of the importer.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 * 		The cooldown of the importer.
	 */
	public long getCooldown() {
		return cooldown;
	}

	/**
	 * This method sets the cooldown of this importer.
	 * 
	 * @param cooldown
	 * 		The new cooldown.
	 */
	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	/**
	 * @return
	 * 		The URL postfix of this importer.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * This method sets the URL postfix of this importer.
	 * 
	 * @param path
	 * 		The new URL postfix of this importer.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
}
