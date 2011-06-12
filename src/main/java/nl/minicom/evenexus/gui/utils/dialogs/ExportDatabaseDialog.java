package nl.minicom.evenexus.gui.utils.dialogs;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFileChooser;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExportDatabaseDialog extends DatabaseFileChooser {

	private static final Logger LOG = LoggerFactory.getLogger(ExportDatabaseDialog.class);
	
	private static final long serialVersionUID = -2633245343435662634L;
	
	@Override
	public void onFileSelect(final File file) {
		if (file == null) {
			return;
		}
		
		new nl.minicom.evenexus.persistence.Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				try {
					Connection connection = session.connection();
					CallableStatement statement = connection.prepareCall("SCRIPT TO ? COMPRESSION ZIP");
					statement.setString(1, file.getAbsolutePath());
					statement.execute();
				} 
				catch (SQLException e) {
					LOG.error(e.getLocalizedMessage(), e);
				}
				return null;
			}
		}.doQuery();
	}

	@Override
	public void setAdditionalParameters(JFileChooser chooser) {
		setTitle("Export database to...");
		chooser.setApproveButtonText("Export file");
	}

}
