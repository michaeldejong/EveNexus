package nl.minicom.evenexus.gui.tables.renderers;

import javax.swing.table.DefaultTableCellRenderer;

public class AlignLeftRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -6500464769718674586L;
	
	public AlignLeftRenderer() {
		super();
		setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
	}
		
	@Override
	public String getText() {
		return(" " + super.getText() + " ");
	}
	
}
