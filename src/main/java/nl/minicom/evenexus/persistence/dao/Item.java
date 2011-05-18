package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "invtypes")
public class Item implements Serializable {
	
	private static final long serialVersionUID = 6386960971498786733L;

	@Id
	@Column(name = "typeid", nullable = false)
	private long typeId;

	@Column(name = "groupid")
	private Long groupId;

	@Column(name = "typename")
	private String typeName;

	@Column(name = "description")
	private String description;

	@Column(name = "graphicid")
	private Long graphicId;

	@Column(name = "radius")
	private Double radius;

	@Column(name = "mass")
	private Double mass;

	@Column(name = "volume")
	private Double volume;

	@Column(name = "capacity")
	private Double capacity;

	@Column(name = "portionsize")
	private Long portionSize;

	@Column(name = "raceid")
	private Integer raceId;

	@Column(name = "baseprice")
	private Double basePrice;

	@Column(name = "published")
	private Boolean published;

	@Column(name = "marketgroupid")
	private Long marketGroupId;

	@Column(name = "chanceofduplicating")
	private Double chanceOfDuplicating;
	
	public long getTypeId() {
		return typeId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getDescription() {
		return description;
	}

	public Long getGraphicId() {
		return graphicId;
	}

	public Double getRadius() {
		return radius;
	}

	public Double getMass() {
		return mass;
	}

	public Double getVolume() {
		return volume;
	}

	public Double getCapacity() {
		return capacity;
	}

	public Long getPortionSize() {
		return portionSize;
	}

	public Integer getRaceId() {
		return raceId;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public Boolean getPublished() {
		return published;
	}

	public Long getMarketGroupId() {
		return marketGroupId;
	}

	public Double getChanceOfDuplicating() {
		return chanceOfDuplicating;
	}	
	
}
