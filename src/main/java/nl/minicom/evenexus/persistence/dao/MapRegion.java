package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity describes a region on the map of eve.
 *
 * @author michael
 */
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
	private Long factionId;

	/**
	 * @return
	 * 		The id of this region.
	 */
	public long getRegionId() {
		return regionId;
	}
	
	/**
	 * This method sets the id of this region.
	 * 
	 * @param regionId
	 * 		The id of this region.
	 */
	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	/**
	 * @return
	 * 		The name of this region.
	 */
	public String getRegionName() {
		return regionName;
	}
	
	/**
	 * This method sets the name of this region.
	 * 
	 * @param regionName
	 * 		The new name of this region.
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	/**
	 * @return
	 * 		The id of the faction operating in this region.
	 */
	public Long getFactionId() {
		return factionId;
	}
	
	/**
	 * This method sets the id of the faction operating this region.
	 * 
	 * @param factionId
	 * 		The id of the faction.
	 */
	public void setFactionId(Long factionId) {
		this.factionId = factionId;
	}

}
