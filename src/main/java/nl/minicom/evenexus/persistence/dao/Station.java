package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stastations")
public class Station implements Serializable {
	
	private static final long serialVersionUID = 1187459166827013799L;

	public static final String STATION_ID = "stationId";
	public static final String STATION_TYPE_ID = "stationTypeId";
	public static final String CORPORATION_ID = "corporationId";
	public static final String SOLAR_SYSTEM_ID = "solarSystemId";
	public static final String CONSTELLATION_ID = "constellationId";
	public static final String REGION_ID = "regionId";
	public static final String STATION_NAME = "stationName";
	
	@Id
	@Column(name = STATION_ID)
	private long stationId;

	@Column(name = STATION_TYPE_ID)
	private long stationTypeId;

	@Column(name = CORPORATION_ID)
	private long corporationId;

	@Column(name = SOLAR_SYSTEM_ID)
	private long solarSystemId;

	@Column(name = CONSTELLATION_ID)
	private long constellationId;
	
	@Column(name = REGION_ID)
	private long regionId;

	@Column(name = STATION_NAME)
	private String stationName;

	public long getStationId() {
		return stationId;
	}

	public long getStationTypeId() {
		return stationTypeId;
	}

	public long getCorporationId() {
		return corporationId;
	}

	public long getSolarSystemId() {
		return solarSystemId;
	}

	public long getConstellationId() {
		return constellationId;
	}

	public long getRegionId() {
		return regionId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public void setStationTypeId(long stationTypeId) {
		this.stationTypeId = stationTypeId;
	}

	public void setCorporationId(long corporationId) {
		this.corporationId = corporationId;
	}

	public void setSolarSystemId(long solarSystemId) {
		this.solarSystemId = solarSystemId;
	}

	public void setConstellationId(long constellationId) {
		this.constellationId = constellationId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

}
