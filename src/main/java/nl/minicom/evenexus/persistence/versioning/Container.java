package nl.minicom.evenexus.persistence.versioning;

import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.persistence.dao.Item;
import nl.minicom.evenexus.persistence.dao.Station;

public class Container {

	private int versionId;
	private final List<Station> stations;
	private final List<Item> items;
	
	public Container() {
		this.stations = new ArrayList<Station>();
		this.items = new ArrayList<Item>();
	}
	
	public int getVersion() {
		return versionId;
	}
	
	public void setVersion(int versionId) {
		this.versionId = versionId;
	}
	
	public List<Station> getStations() {
		return stations;
	}
	
	public void addStation(Station station) {
		stations.add(station);
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
}
