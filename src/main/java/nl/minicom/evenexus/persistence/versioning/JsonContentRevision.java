package nl.minicom.evenexus.persistence.versioning;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import nl.minicom.evenexus.persistence.dao.Item;
import nl.minicom.evenexus.persistence.dao.Station;

import com.google.gson.Gson;

public class JsonContentRevision implements IRevision {
	
	private final Container container;
	
	public JsonContentRevision(String fileName) throws Exception {
		Gson gson = new Gson();
		InputStream in = JsonContentRevision.class.getResourceAsStream(fileName);
		container = gson.fromJson(new InputStreamReader(in), Container.class);
	}
	
	@Override
	public void execute(Connection connection) throws SQLException {
		persistStations(connection);
		persistItems(connection);
	}

	private void persistItems(Connection connection) throws SQLException {
		connection.createStatement().execute("TRUNCATE TABLE invtypes");

		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO invtypes (typeId, typeName, volume, marketGroupId) VALUE (?, ?, ?, ?)");
		PreparedStatement statement = connection.prepareStatement(builder.toString());
		
		for (Item item : container.getItems()) {
			persistItem(statement, item);
		}
	}

	private void persistItem(PreparedStatement statement, Item item) throws SQLException {
		statement.setLong(1, item.getTypeId());
		statement.setString(2, item.getTypeName());
		statement.setDouble(3, item.getVolume());
		statement.setLong(4, item.getMarketGroupId());
		statement.execute();
	}

	private void persistStations(Connection connection) throws SQLException {
		connection.createStatement().execute("TRUNCATE TABLE stastations");

		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO stastations (stationId, stationTypeId, corporationId, solarSystemId, ");
		builder.append("constellationId, regionId, stationName) VALUE (?, ?, ?, ?, ?, ?, ?)");
		PreparedStatement statement = connection.prepareStatement(builder.toString());
		
		for (Station station : container.getStations()) {
			persistStation(statement, station);
		}
	}

	private void persistStation(PreparedStatement statement, Station station) throws SQLException {
		statement.setLong(1, station.getStationId());
		statement.setLong(2, station.getStationTypeId());
		statement.setLong(3, station.getCorporationId());
		statement.setLong(4, station.getSolarSystemId());
		statement.setLong(5, station.getConstellationId());
		statement.setLong(6, station.getRegionId());
		statement.setString(7, station.getStationName());
		statement.execute();
	}

	@Override
	public int getRevisionNumber() {
		return container.getVersion();
	}

}
