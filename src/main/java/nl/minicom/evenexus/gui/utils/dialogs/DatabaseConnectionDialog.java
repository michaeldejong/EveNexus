package nl.minicom.evenexus.gui.utils.dialogs;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public final class DatabaseConnectionDialog extends CustomDialog {

	private static final long serialVersionUID = 8860569373371189189L;
	
//	private static final Logger logger = LoggerFactory.getRootLogger();
	
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
		
		JLabel label = new JLabel();
		label.setBounds(10, 10, 336, 80);
		label.setText("<html>EveNexus could not connect to the database, because it is locked. Most likely it has been locked by another instance of EveNexus, which is probably running in the background.</html>");
		label.setVerticalAlignment(SwingConstants.TOP);
		guiPanel.add(label);
	}
	

}
