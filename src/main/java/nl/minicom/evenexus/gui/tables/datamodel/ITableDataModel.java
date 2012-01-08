package nl.minicom.evenexus.gui.tables.datamodel;

import java.util.List;
import java.util.Map;

public interface ITableDataModel {
	
	List<Object[]> reload();

	String[] getFields();

	void delete(Map<String, Object> row);

}
