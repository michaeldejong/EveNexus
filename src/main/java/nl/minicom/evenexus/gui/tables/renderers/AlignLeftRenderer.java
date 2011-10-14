package nl.minicom.evenexus.gui.tables.renderers;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * This class is a renderer which aligns the data to the left.
 * 
 * @author michael
 */
public class AlignLeftRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -6500464769718674586L;

	/**
	 * This constructs a new {@link AlignLeftRenderer} object.
	 */
	public AlignLeftRenderer() {
		setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
	}
		
	@Override
	public String getText() {
		return " " + super.getText() + " ";
	}
	
}
