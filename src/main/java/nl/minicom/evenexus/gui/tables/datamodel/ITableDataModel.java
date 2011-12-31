package nl.minicom.evenexus.gui.tables.datamodel;

import java.util.List;

/**
 * This interface defines the basic data container used in tables.
 *
 * @author michael
 */
public interface ITableDataModel {
	
	/**
	 * This method reloads the internal data, and returns it in a table format.
	 * 
	 * @return
	 * 		The reloaded data.
	 */
	List<Object[]> reload();

	/**
	 * @return
	 * 		An array of column names.
	 */
	String[] getFields();

}
