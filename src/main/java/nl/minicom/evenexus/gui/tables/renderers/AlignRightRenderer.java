package nl.minicom.evenexus.gui.tables.renderers;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * This class is a renderer which renders all data to the right in the table.
 *
 * @author michael
 */
public class AlignRightRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -6500464769718674586L;
	
	/**
	 * This constructs a new {@link AlignRightRenderer} object.
	 */
	public AlignRightRenderer() {
		setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
	}
		
	@Override
	public String getText() {
		return " " + super.getText() + " ";
	}
	
}
