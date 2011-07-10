package nl.minicom.evenexus.gui.utils.dialogs;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.swing.JFileChooser;

import nl.minicom.evenexus.persistence.Database;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;


public class ExportDatabaseDialog extends DatabaseFileChooser {

	private static final long serialVersionUID = -2633245343435662634L;
	
	private final Database database;
	
	@Inject
	public ExportDatabaseDialog(Database database) {
		this.database = database;
	}
	
	@Override
	public void onFileSelect(final File file) {
		if (file == null) {
			return;
		}
		
		Work work = new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				CallableStatement statement = connection.prepareCall("SCRIPT TO ? COMPRESSION ZIP");
				statement.setString(1, file.getAbsolutePath());
				statement.execute();
			}
		};
		
		Session session = database.getCurrentSession();
		session.doWork(work);
	}

	@Override
	public void setAdditionalParameters(JFileChooser chooser) {
		setTitle("Export database to...");
		chooser.setApproveButtonText("Export file");
	}

}
