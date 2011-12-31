package nl.minicom.evenexus.persistence.versioning;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.hibernate.Session;

import com.google.gson.Gson;

/**
 * The {@link JsonContentRevision} class is a special type of {@link IRevision},
 * which persists data to the database.
 * 
 * @author michael
 */
public class JsonContentRevision implements IRevision {
	
	private final Container container;
	
	/**
	 * This constructs a new {@link JsonContentRevision} object.
	 * 
	 * @param fileName
	 * 		The name of the file containing the JSON serialized data.
	 * 
	 * @throws Exception
	 * 		If something went wrong while executing.
	 */
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
