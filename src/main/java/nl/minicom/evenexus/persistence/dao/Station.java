package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity describes a station in the game.
 * 
 * @author michael
 */
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

	/**
	 * @return
	 * 		The id of this {@link Station}.
	 */
	public long getStationId() {
		return stationId;
	}

	/**
	 * @return
	 * 		The id of the type of this {@link Station}.
	 */
	public long getStationTypeId() {
		return stationTypeId;
	}

	/**
	 * @return
	 * 		The id of the corporation to which this {@link Station} belongs.
	 */
	public long getCorporationId() {
		return corporationId;
	}

	/**
	 * @return
	 * 		The id of the solar system in which this {@link Station} resides.
	 */
	public long getSolarSystemId() {
		return solarSystemId;
	}

	/**
	 * @return
	 * 		The id of the constellation in which this {@link Station} resides.
	 */
	public long getConstellationId() {
		return constellationId;
	}

	/**
	 * @return
	 * 		The id of the region in which this {@link Station} resides.
	 */
	public long getRegionId() {
		return regionId;
	}

	/**
	 * @return
	 * 		The name of the {@link Station}.
	 */
	public String getStationName() {
		return stationName;
	}

	/**
	 * This method sets the id of the {@link Station}.
	 * 
	 * @param stationId
	 * 		The id of the {@link Station}.
	 */
	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	/**
	 * This method sets the type of {@link Station}.
	 * 
	 * @param stationTypeId
	 * 		The id of the type of {@link Station}.
	 */
	public void setStationTypeId(long stationTypeId) {
		this.stationTypeId = stationTypeId;
	}

	/**
	 * This method sets the id of the corporation owning this {@link Station}.
	 * 
	 * @param corporationId
	 * 		The id of the corporation.
	 */
	public void setCorporationId(long corporationId) {
		this.corporationId = corporationId;
	}

	/**
	 * This method sets the id of the solar system in which this {@link Station} resides.
	 * 
	 * @param solarSystemId
	 * 		The id of the solar system.
	 */
	public void setSolarSystemId(long solarSystemId) {
		this.solarSystemId = solarSystemId;
	}

	/**
	 * This method sets the id of the constellation in which this {@link Station} resides.
	 * 
	 * @param constellationId
	 * 		The id of the constellation.
	 */
	public void setConstellationId(long constellationId) {
		this.constellationId = constellationId;
	}


	/**
	 * This method sets the id of the region in which this {@link Station} resides.
	 * 
	 * @param regionId
	 * 		The id of the region.
	 */
	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	/**
	 * This method sets the name of the {@link Station}.
	 * 
	 * @param stationName
	 * 		The name of the {@link Station}.
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

}
