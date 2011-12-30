package nl.minicom.evenexus.persistence.versioning;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import nl.minicom.evenexus.persistence.dao.Item;
import nl.minicom.evenexus.persistence.dao.MapRegion;
import nl.minicom.evenexus.persistence.dao.Station;

import org.sqlite.SQLiteConfig;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

/**
 * This class is responsible for creating a serialized form of the eve source database.
 *
 * @author michael
 */
public final class ContentCreator {

	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String URL = "jdbc:sqlite:/Users/michael/Downloads/inca10-sqlite3-v1.db";
	
	/**
	 * This main() method creates a serialized database dump of the eve source database.
	 * 
	 * @param args
	 * 		Any additional arguments. 
	 * 
	 * @throws JsonIOException
	 * 		If some object could not be serialized.
	 * 
	 * @throws SQLException
	 * 		If we had problems querying the database.
	 * 
	 * @throws IOException
	 * 		If we could not access the file system.
	 */
	public static void main(String[] args) throws JsonIOException, SQLException, IOException {
		new ContentCreator().execute();
	}
	
	private ContentCreator() {
		// Prevent instantiation.
	}
	
	private Connection conn = null;
	
	private void execute() throws SQLException, JsonIOException, IOException {
		connect();
		
		Container c = new Container();
		c.setVersion(1);
		addStations(c);
		addItems(c);
		addRegions(c);
		
		StringWriter writer = new StringWriter();
		new Gson().toJson(c, writer);
		
		FileWriter fileWriter = new FileWriter("content.json");
		fileWriter.write(writer.toString());
		fileWriter.flush();
		fileWriter.close();
		
		close();
	}

	private void connect() {
		try {
			Class.forName(DRIVER);
			SQLiteConfig config = new SQLiteConfig();
			config.setReadOnly(false);
			conn = DriverManager.getConnection(URL, config.toProperties());
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void addStations(Container c) throws SQLException {
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM stastations");
		ResultSet result = statement.executeQuery();
		
		while (result.next()) {
			Station station = new Station();
			station.setStationId(result.getLong("stationId"));
			station.setStationName(result.getString("stationName"));
			station.setStationTypeId(result.getLong("stationTypeId"));
			station.setRegionId(result.getLong("regionId"));
			station.setConstellationId(result.getLong("constellationId"));
			station.setSolarSystemId(result.getLong("solarSystemId"));
			station.setCorporationId(result.getLong("corporationId"));
			c.addStation(station);
		}
	}
	
	private void addItems(Container c) throws SQLException {
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM invtypes WHERE published = 1");
		ResultSet result = statement.executeQuery();
		
		while (result.next()) {
			Item item = new Item();
			item.setMarketGroupId(result.getLong("marketGroupId"));
			item.setTypeId(result.getLong("typeId"));
			item.setTypeName(result.getString("typeName"));
			item.setVolume(result.getDouble("volume"));
			c.addItem(item);
		}
	}
	
	private void addRegions(Container c) throws SQLException {
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM mapregions");
		ResultSet result = statement.executeQuery();
		
		while (result.next()) {
			MapRegion region = new MapRegion();
			region.setRegionId(result.getLong("regionId"));
			region.setRegionName(result.getString("regionName"));
			region.setFactionId(result.getLong("factionId"));
			c.addRegion(region);
		}
	}

	private void close() {
		try {
			conn.close();
		} 
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
