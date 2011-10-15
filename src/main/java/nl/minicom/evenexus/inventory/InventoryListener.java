package nl.minicom.evenexus.inventory;

/**
 * This listener can be implemented to listen to specific events while matching transactions. 
 *
 * @author michael
 */
public interface InventoryListener {

	/**
	 * This method is called when an update is received.
	 * 
	 * @param event
	 * 		The {@link InventoryEvent} containing data about the event.
	 */
	void onUpdate(InventoryEvent event);
	
}
