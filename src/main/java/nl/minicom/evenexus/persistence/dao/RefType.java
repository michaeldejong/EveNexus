package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class describes various types of {@link WalletJournal} entries.
 * 
 * @author michael
 */
@Entity
@Table(name = "reftypes")
public class RefType implements Serializable {

	private static final long serialVersionUID = -4912521169103523974L;

	public static final String REF_TYPE_ID = "refTypeId";
	public static final String DESCRIPTION = "description";
	
	@Id
	@Column(name = REF_TYPE_ID, nullable = false)
	private long refTypeId;

	@Column(name = DESCRIPTION, nullable = false)
	private String description;

	/**
	 * @return
	 * 		The id of this {@link RefType}.
	 */
	public long getRefTypeId() {
		return refTypeId;
	}

	/**
	 * This method sets the id of this {@link RefType}.
	 * 
	 * @param refTypeId
	 * 		The new id of this {@link RefType}.
	 */
	public void setRefTypeId(long refTypeId) {
		this.refTypeId = refTypeId;
	}

	/**
	 * @return
	 * 		The description of this {@link RefType}.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This method sets the description of this {@link RefType}.
	 * 
	 * @param description
	 * 		The description of this {@link RefType}.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
}
