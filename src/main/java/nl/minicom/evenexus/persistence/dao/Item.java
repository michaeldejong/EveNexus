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
	
	public static final String TYPE_ID = "typeid";
	public static final String GROUP_ID = "groupid";
	public static final String TYPE_NAME = "typename";
	public static final String DESCRIPTION = "description";
	public static final String GRAPHIC_ID = "graphicid";
	public static final String RADIUS = "radius";
	public static final String MASS = "mass";
	public static final String VOLUME = "volume";
	public static final String CAPACITY = "capacity";
	public static final String PORTION_SIZE = "portionsize";
	public static final String RACE_ID = "raceid";
	public static final String BASE_PRICE = "baseprice";
	public static final String PUBLISHED = "published";
	public static final String MARKET_GROUP_ID = "marketgroupid";
	public static final String CHANCE_OF_DUPLICATING = "chanceofduplicating";

	@Id
	@Column(name = TYPE_ID, nullable = false)
	private long typeId;

	@Column(name = GROUP_ID)
	private Long groupId;

	@Column(name = TYPE_NAME)
	private String typeName;

	@Column(name = DESCRIPTION)
	private String description;

	@Column(name = GRAPHIC_ID)
	private Long graphicId;

	@Column(name = RADIUS)
	private Double radius;

	@Column(name = MASS)
	private Double mass;

	@Column(name = VOLUME)
	private Double volume;

	@Column(name = CAPACITY)
	private Double capacity;

	@Column(name = PORTION_SIZE)
	private Long portionSize;

	@Column(name = RACE_ID)
	private Integer raceId;

	@Column(name = BASE_PRICE)
	private Double basePrice;

	@Column(name = PUBLISHED)
	private Boolean published;

	@Column(name = MARKET_GROUP_ID)
	private Long marketGroupId;

	@Column(name = CHANCE_OF_DUPLICATING)
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
