package nl.minicom.evenexus.gui.tables.renderers;


import java.awt.Component;
import java.sql.Timestamp;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import nl.minicom.evenexus.gui.tables.formatters.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The {@link DateTimeRenderer} defines how we should render dates and times in the GUI.
 *  
 * @author michael
 */
public class DateTimeRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -3869075201627613304L;

	private static final Logger LOG = LoggerFactory.getLogger(DateTimeRenderer.class);
	private static final AbstractFormatter FORMATTER = new DateTimeFormatter();

	/**
	 * Constructs a new {@link DateTimeRenderer}.
	 */
	public DateTimeRenderer() {
		setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
	}

	@Override
	public String getText() {
		return (" " + super.getText() + " ");
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, 
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (!(value instanceof Timestamp)) {
			return null;
		}
		
		Timestamp timestamp = (Timestamp) value;
		try {
			setValue(FORMATTER.valueToString(timestamp));
		}
		catch (ParseException e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
		
		return c;
	}
	
}
