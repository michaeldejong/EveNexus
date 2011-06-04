package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reftypes")
public class RefType implements Serializable {

	private static final long serialVersionUID = -4912521169103523974L;

	@Id
	@Column(name = "reftypeid", nullable = false)
	private long refTypeId;

	@Column(name = "description", nullable = false)
	private String description;

	public long getRefTypeId() {
		return refTypeId;
	}

	public void setRefTypeId(long refTypeId) {
		this.refTypeId = refTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
