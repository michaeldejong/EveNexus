package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bugreports")
public class BugReport implements Serializable {

	private static final long serialVersionUID = 8621446725055992893L;

	@Id
	@Column(name = "issueNumber", nullable = false)
	private long issueNumber;

	public long getIssueNumber() {
		return issueNumber;
	}

	public void setIssueNumber(long issueNumber) {
		this.issueNumber = issueNumber;
	}

}
