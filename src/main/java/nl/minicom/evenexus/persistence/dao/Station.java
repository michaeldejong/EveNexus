package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;

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

	@Column(name = "security")
	private long security;

	@Column(name = "dockingcostpervolume")
	private double dockingCostPerVolume;

	@Column(name = "maxshipvolumedockable")
	private double maxShipVolumeDockable;

	@Column(name = "officerentalcost")
	private long officeRentalCost;

	@Column(name = "operationid")
	private int operationId;

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

	@Column(name = "x")
	private double x;

	@Column(name = "y")
	private double y;

	@Column(name = "z")
	private double z;

	@Column(name = "reprocessingefficiency")
	private BigDecimal reprocessingEfficiency;

	@Column(name = "reprocessingstationstake")
	private BigDecimal reprocessingStationsTake;

	@Column(name = "reprocessinghangarflag")
	private int reprocessingHangarFlag;
	
	public long getStationId() {
		return stationId;
	}

	public long getSecurity() {
		return security;
	}

	public double getDockingCostPerVolume() {
		return dockingCostPerVolume;
	}

	public double getMaxShipVolumeDockable() {
		return maxShipVolumeDockable;
	}

	public long getOfficeRentalCost() {
		return officeRentalCost;
	}

	public int getOperationId() {
		return operationId;
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

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public BigDecimal getReprocessingEfficiency() {
		return reprocessingEfficiency;
	}

	public BigDecimal getReprocessingStationsTake() {
		return reprocessingStationsTake;
	}

	public int getReprocessingHangarFlag() {
		return reprocessingHangarFlag;
	}
	
}
