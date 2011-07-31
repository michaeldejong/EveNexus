package nl.minicom.evenexus.gui.tables.renderers;

import javax.inject.Inject;
import javax.swing.table.DefaultTableCellRenderer;

public class AlignRightRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -6500464769718674586L;
	
	@Inject
	public AlignRightRenderer() {
		setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
	}
		
	@Override
	public String getText() {
		return(" " + super.getText() + " ");
	}
	
}
