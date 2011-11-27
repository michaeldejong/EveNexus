package nl.minicom.evenexus.persistence.versioning;

import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.persistence.dao.Item;
import nl.minicom.evenexus.persistence.dao.MapRegion;
import nl.minicom.evenexus.persistence.dao.SolarSystem;
import nl.minicom.evenexus.persistence.dao.Station;

/**
 * The container class is a special data class which is serialized and deserialized by GSON.
 * 
 * @author michael
 */
public class Container {

	private int versionId;
	private final List<Station> stations;
	private final List<Item> items;
	private final List<MapRegion> regions;
	private final List<SolarSystem> solarSystems;
	
	/**
	 * This constructs a new {@link Container} object.
	 */
	public Container() {
		this.stations = new ArrayList<Station>();
		this.items = new ArrayList<Item>();
		this.regions = new ArrayList<MapRegion>();
		this.solarSystems = new ArrayList<SolarSystem>();
	}
	
	/**
	 * @return
	 * 		The version of the data in this container.
	 */
	public int getVersion() {
		return versionId;
	}
	
	/**
	 * This method sets the version of the data in this container.
	 * 
	 * @param versionId
	 * 		The version of the data in this container.
	 */
	public void setVersion(int versionId) {
		this.versionId = versionId;
	}
	
	/**
	 * @return
	 * 		A {@link List} of {@link Station}s in this container.
	 */
	public List<Station> getStations() {
		return stations;
	}
	
	/**
	 * This method adds a {@link Station} to the container.
	 * 
	 * @param station
	 * 		The {@link Station} to add to the container.
	 */
	public void addStation(Station station) {
		stations.add(station);
	}
	
	/**
	 * @return
	 * 		A {@link List} of {@link Item}s in this container.
	 */
	public List<Item> getItems() {
		return items;
	}
	
	/**
	 * This method adds an {@link Item} to the container.
	 * 
	 * @param item
	 * 		The {@link Item} to add to the container.
	 */
	public void addItem(Item item) {
		items.add(item);
	}

	/**
	 * @return
	 * 		A {@link List} of {@link MapRegion}s in this container.
	 */
	public List<MapRegion> getRegions() {
		return regions;
	}
	
	/**
	 * This method adds a {@link MapRegion} to the container.
	 * 
	 * @param region
	 * 		The {@link MapRegion} to add to the container.
	 */
	public void addRegion(MapRegion region) {
		regions.add(region);
	}

	/**
	 * @return
	 * 		A {@link List} of {@link SolarSystem}s in this container.
	 */
	public List<SolarSystem> getSolarSystems() {
		return solarSystems;
	}
	
	/**
	 * This method adds a {@link SolarSystem} to the container.
	 * 
	 * @param solarSystem
	 * 		The {@link SolarSystem} to add to the container.
	 */
	public void addSolarSystem(SolarSystem solarSystem) {
		solarSystems.add(solarSystem);
	}
	
}
