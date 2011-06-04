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

	@Id
	@Column(name = "stationid")
	private long stationId;

	@Column(name = "stationtypeid")
	private long stationTypeId;

	@Column(name = "corporationid")
	private long corporationId;

	@Column(name = "solarsystemid")
	private long solarSystemId;

	@Column(name = "constellationid")
	private long constellationId;
	
	@Column(name = "regionid")
	private long regionId;

	@Column(name = "stationname")
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

}
