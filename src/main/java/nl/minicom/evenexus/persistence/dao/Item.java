package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity describes an item in the game.
 * 
 * @author michael
 */
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
	
	/**
	 * @return
	 * 		The id of the item.
	 */
	public long getTypeId() {
		return typeId;
	}

	/**
	 * @return
	 * 		The name of the item.
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @return
	 * 		The volume of the item.
	 */
	public Double getVolume() {
		return volume;
	}

	/**
	 * @return
	 * 		The id of the marketgroup of this item.
	 */
	public Long getMarketGroupId() {
		return marketGroupId;
	}

	/**
	 * This method sets the id of this item.
	 * 
	 * @param typeId
	 * 		The new id of this item.
	 */
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	/**
	 * This method sets the name of this item.
	 * 
	 * @param typeName
	 * 		The name of this item.
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * This method sets the volume of this item.
	 * 
	 * @param volume
	 * 		The volume of this item.
	 */
	public void setVolume(Double volume) {
		this.volume = volume;
	}

	/**
	 * This method sets the id of marketgroup of this item.
	 * 
	 * @param marketGroupId
	 * 		The id of the marketgroup of this item.
	 */
	public void setMarketGroupId(Long marketGroupId) {
		this.marketGroupId = marketGroupId;
	}

}
