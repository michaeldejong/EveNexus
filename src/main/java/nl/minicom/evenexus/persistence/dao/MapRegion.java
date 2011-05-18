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

	@Column(name = "x")
	private double x;

	@Column(name = "y")
	private double y;

	@Column(name = "z")
	private double z;

	@Column(name = "xmin")
	private double xMin;

	@Column(name = "ymin")
	private double yMin;

	@Column(name = "zmin")
	private double zMin;

	@Column(name = "xmax")
	private double xMax;

	@Column(name = "ymax")
	private double yMax;

	@Column(name = "zmax")
	private double zMax;

	@Column(name = "factionid")
	private long factionId;

	@Column(name = "radius")
	private double radius;
	
	public long getRegionId() {
		return regionId;
	}

	public String getRegionName() {
		return regionName;
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

	public double getxMin() {
		return xMin;
	}

	public double getyMin() {
		return yMin;
	}

	public double getzMin() {
		return zMin;
	}

	public double getxMax() {
		return xMax;
	}

	public double getyMax() {
		return yMax;
	}

	public double getzMax() {
		return zMax;
	}

	public long getFactionId() {
		return factionId;
	}

	public double getRadius() {
		return radius;
	}
}
