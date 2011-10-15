package nl.minicom.evenexus.persistence.versioning;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import nl.minicom.evenexus.persistence.dao.Item;
import nl.minicom.evenexus.persistence.dao.Station;

import org.sqlite.SQLiteConfig;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

public class ContentCreator {

	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String URL = "jdbc:sqlite:/Users/michael/Downloads/inca10-sqlite3-v1.db";
	
	public static void main(String[] args) throws JsonIOException, SQLException, IOException {
		new ContentCreator().execute();
	}
	
	private Connection conn = null;
	
	private void execute() throws SQLException, JsonIOException, IOException {
		connect();
		
		Container c = new Container();
		c.setVersion(1);
		addStations(c);
		addItems(c);
		
		new Gson().toJson(c, new FileWriter("content.json"));
		
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
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM invtypes");
		ResultSet result = statement.executeQuery();
		
		while (result.next()) {
			Item item = new Item();
			item.setBasePrice(result.getDouble("basePrice"));
			item.setCapacity(result.getDouble("capacity"));
			item.setChanceOfDuplicating(result.getDouble("chanceOfDuplicating"));
			item.setDescription(result.getString("description"));
			item.setGraphicId(result.getLong("graphicId"));
			item.setGroupId(result.getLong("groupId"));
			item.setMarketGroupId(result.getLong("marketGroupId"));
			item.setMass(result.getDouble("mass"));
			item.setPortionSize(result.getLong("portionSize"));
			item.setPublished(result.getBoolean("published"));
			item.setRaceId(result.getInt("raceId"));
			item.setRadius(result.getDouble("radius"));
			item.setTypeId(result.getLong("typeId"));
			item.setTypeName(result.getString("typeName"));
			item.setVolume(result.getDouble("volume"));
			c.addItem(item);
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
