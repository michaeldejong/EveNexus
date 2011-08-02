package nl.minicom.evenexus.gui.tables.datamodel;

import java.util.List;

public interface ITableDataModel {
	
	List<Object[]> reload();

	String[] getFields();

}
