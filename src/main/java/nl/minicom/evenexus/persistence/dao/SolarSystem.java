package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "solarsystems")
public class SolarSystem implements Serializable {
	
	private static final long serialVersionUID = 9214480288622380091L;
	
	public static final String SOLAR_SYSTEM_ID = "solarSystemId";
	public static final String SOLAR_SYSTEM_NAME = "solarSystemName";
	public static final String REGION_ID = "regionId";
	
	@Id
	@Column(name = SOLAR_SYSTEM_ID)
	private long solarSystemId;

	@Column(name = SOLAR_SYSTEM_NAME)
	private String solarSystemName;

	@Column(name = REGION_ID)
	private Long regionId;

	public long getSolarSystemId() {
		return solarSystemId;
	}
	
	public void setSolarSystemId(long solarSystemId) {
		this.solarSystemId = solarSystemId;
	}

	public String getSolarSystemName() {
		return solarSystemName;
	}
	
	public void setSolarSystemName(String solarSystemName) {
		this.solarSystemName = solarSystemName;
	}

	public Long getRegionId() {
		return regionId;
	}
	
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

}
