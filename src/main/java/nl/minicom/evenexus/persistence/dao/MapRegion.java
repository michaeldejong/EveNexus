package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mapregions")
public class MapRegion implements Serializable {
	
	private static final long serialVersionUID = 9214480288622380091L;

	@Id
	@Column(name = "regionid")
	private long regionId;

	@Column(name = "regionname")
	private String regionName;

	@Column(name = "factionid")
	private long factionId;

	public long getRegionId() {
		return regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public long getFactionId() {
		return factionId;
	}

}
