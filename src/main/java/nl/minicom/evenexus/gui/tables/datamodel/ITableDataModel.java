package nl.minicom.evenexus.gui.tables.datamodel;

import java.util.List;

public interface ITableDataModel {
	
	public List<Object[]> reload();

	public String[] getFields();

}
