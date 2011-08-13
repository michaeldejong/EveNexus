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
	
	public static final String REGION_ID = "regionId";
	public static final String REGION_NAME = "regionName";
	public static final String FACTION_ID = "factionId";
	
	@Id
	@Column(name = REGION_ID)
	private long regionId;

	@Column(name = REGION_NAME)
	private String regionName;

	@Column(name = FACTION_ID)
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
