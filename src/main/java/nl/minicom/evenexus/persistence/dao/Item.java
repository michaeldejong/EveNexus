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

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGraphicId(Long graphicId) {
		this.graphicId = graphicId;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public void setMass(Double mass) {
		this.mass = mass;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public void setCapacity(Double capacity) {
		this.capacity = capacity;
	}

	public void setPortionSize(Long portionSize) {
		this.portionSize = portionSize;
	}

	public void setRaceId(Integer raceId) {
		this.raceId = raceId;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	public void setMarketGroupId(Long marketGroupId) {
		this.marketGroupId = marketGroupId;
	}

	public void setChanceOfDuplicating(Double chanceOfDuplicating) {
		this.chanceOfDuplicating = chanceOfDuplicating;
	}	
	
}
