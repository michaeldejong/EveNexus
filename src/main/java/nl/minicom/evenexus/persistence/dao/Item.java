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
	
	public static final String TYPE_ID = "typeId";
	public static final String TYPE_NAME = "typeName";
	public static final String VOLUME = "volume";
	public static final String MARKET_GROUP_ID = "marketGroupId";

	@Id
	@Column(name = TYPE_ID, nullable = false)
	private long typeId;

	@Column(name = TYPE_NAME)
	private String typeName;

	@Column(name = VOLUME)
	private Double volume;

	@Column(name = MARKET_GROUP_ID)
	private Long marketGroupId;
	
	public long getTypeId() {
		return typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public Double getVolume() {
		return volume;
	}

	public Long getMarketGroupId() {
		return marketGroupId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public void setMarketGroupId(Long marketGroupId) {
		this.marketGroupId = marketGroupId;
	}

}
