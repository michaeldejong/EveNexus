package nl.minicom.evenexus.persistence.versioning;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import nl.minicom.evenexus.persistence.dao.Item;
import nl.minicom.evenexus.persistence.dao.MapRegion;
import nl.minicom.evenexus.persistence.dao.Station;

import org.hibernate.Session;

import com.google.gson.Gson;

public class JsonContentRevision implements IRevision {
	
	private final Container container;
	
	public JsonContentRevision(String fileName) throws Exception {
		Gson gson = new Gson();
		InputStream in = JsonContentRevision.class.getResourceAsStream(fileName);
		container = gson.fromJson(new InputStreamReader(in), Container.class);
	}
	
	@Override
	public void execute(Session session) {
		putData(session, "stastations", container.getStations());
		putData(session, "invtypes", container.getItems());
		putData(session, "mapregions", container.getRegions());
		putData(session, "solarsystems", container.getSolarSystems());
	}
	
	private <T> void putData(Session session, String tableName, List<T> entities) {
		int count = 0;
		session.createSQLQuery("TRUNCATE TABLE " + tableName).executeUpdate();
		for (T entity : entities) {
			session.save(entity);
			count++;
			
			if (count % 100 == 0) {
				session.flush();
				session.clear();
			}
		}
		
		session.flush();
		session.clear();
	}

	@Override
	public int getRevisionNumber() {
		return container.getVersion();
	}

}
