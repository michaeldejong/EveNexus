package nl.minicom.evenexus.persistence.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;

import com.beimin.eveapi.core.ApiAuth;

@Entity
@Table(name = "apisettings")
public class ApiKey extends ApiAuth<ApiKey> implements Serializable {

	private static final long serialVersionUID = -5243514709906664430L;

	@SuppressWarnings("unchecked")
	public static List<ApiKey> getAll(Session session) {
		return session.createCriteria(ApiKey.class).list();
	}

	@Id
	@Column(name = "charid", nullable = false)
	private long charId;

	@Column(name = "apikey", nullable = false)
	private String apiKey;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "userid", nullable = false)
	private int userId;
	
	public long getCharacterID() {
		return charId;
	}

	public void setCharacterID(long charId) {
		this.charId = charId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getCharacterName() {
		return name;
	}

	public void setCharacterName(String name) {
		this.name = name;
	}

	public int getUserID() {
		return userId;
	}

	public void setUserID(int userId) {
		this.userId = userId;
	}

	@Override
	public int compareTo(ApiKey o) {
		return 0;
	}
	
	public int getSkillLevel(Session session, long skillId) {
		int level = 0;
		SkillIdentifier skillIdentifier = new SkillIdentifier(getCharacterID(), skillId);
		Skill skill = (Skill) session.get(Skill.class, skillIdentifier);
		if (skill != null) {
			level = skill.getLevel();
		}
		
		return level;
	}

	public BigDecimal getCorporationStanding(Session session, long stationId) {
		BigDecimal returnValue = BigDecimal.ZERO;
		Station station = (Station) session.get(Station.class, stationId);
		if (station != null) {
			StandingIdentifier id = new StandingIdentifier(getCharacterID(), station.getCorporationId());
			Standing standing = (Standing) session.get(Standing.class, id);
			if (standing != null) {
				returnValue = standing.getStanding();
			}
		}

		return returnValue;
	}

	public BigDecimal getFactionStanding(Session session, long mapRegionId) {
		BigDecimal returnValue = BigDecimal.ZERO;
		MapRegion region = (MapRegion) session.get(MapRegion.class, mapRegionId);
		if (region != null) {
			StandingIdentifier id = new StandingIdentifier(getCharacterID(), region.getFactionId());
			Standing standing = (Standing) session.get(Standing.class, id);
			if (standing != null) {
				returnValue = standing.getStanding();
			}
		}

		return returnValue;
	}

}
