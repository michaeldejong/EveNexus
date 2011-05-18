package nl.minicom.evenexus.gui.tables.renderers;

import javax.swing.table.DefaultTableCellRenderer;

public class AlignRightRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -6500464769718674586L;
	
	public AlignRightRenderer() {
		super();
		setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
	}
		
	@Override
	public String getText() {
		return(" " + super.getText() + " ");
	}
	
}
