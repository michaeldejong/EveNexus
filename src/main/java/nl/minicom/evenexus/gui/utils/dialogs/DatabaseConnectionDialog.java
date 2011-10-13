package nl.minicom.evenexus.gui.utils.dialogs;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * This class is responsible for displaying a message
 * (the application could not connect to the database)
 * to the user.
 *
 * @author michael
 */
public final class DatabaseConnectionDialog extends CustomDialog {

	private static final long serialVersionUID = 8860569373371189189L;
	
	/**
	 * This method displays the {@link DatabaseConnectionDialog}.
	 */
	public static void showFrame() {
		new DatabaseConnectionDialog();
	}
	
	private DatabaseConnectionDialog() {
		super(DialogTitle.DATABASE_CONNECTION_TITLE, 370, 190);
		buildGui();
		setVisible(true);
		toFront();
	}

	@Override
	public void createGui(JPanel guiPanel) {		
		guiPanel.setLayout(null);

		String message = new StringBuilder()
		.append("<html>EveNexus could not connect to the database, because it is locked. ")
		.append("Most likely it has been locked by another instance of EveNexus, ")
		.append("which is probably running in the background.</html>")
		.toString();
		
		JLabel label = new JLabel();
		label.setBounds(10, 10, 336, 80);
		label.setText(message);
		label.setVerticalAlignment(SwingConstants.TOP);
		guiPanel.add(label);
	}
	

}
