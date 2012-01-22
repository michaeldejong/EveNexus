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
 * The {@link ContentCreator} class is responsible for building a serialized text file,
 * which EveNexus can parse on the user's computer, and persist to the database.
 *
 * @author michael
 */
public final class ContentCreator {

	private static final Integer VERSION = 3;
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String URL = "jdbc:sqlite:/Users/michael/Downloads/cruc101-sqlite3-v1.db";
	
	/**
	 * This creates a new serialized text file using data from the specified SQLite database.
	 * 
	 * @param args
	 * 		None.
	 * 
	 * @throws JsonIOException
	 * 		If the data could not be serialized by GSON.
	 * 
	 * @throws SQLException
	 * 		If we had problems contacting the database.
	 * 
	 * @throws IOException
	 * 		If we had problems writing the serialized file.
	 */
	public static void main(String[] args) throws JsonIOException, SQLException, IOException {
		new ContentCreator().execute();
	}
	
	private Connection conn = null;
	
	private ContentCreator() {
		// Prevent instantiation.
	}
	
	private void execute() throws SQLException, JsonIOException, IOException {
		connect();
		
		Container c = new Container();
		c.setVersion(VERSION);
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
