package nl.minicom.evenexus.gui.utils.dialogs;


import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFileChooser;

import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.versioning.RevisionExecutor;
import nl.minicom.evenexus.persistence.versioning.StructureUpgrader;

import org.hibernate.Session;


public class ImportDatabaseDialog extends DatabaseFileChooser {

	private static final long serialVersionUID = -2633245343435662634L;
	
	@Override
	public void onFileSelect(final File file) {
		if (file == null) {
			return;
		}
		
		new Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				try {
					session.createSQLQuery("DROP ALL OBJECTS").executeUpdate();
					Connection connection = session.connection();
					CallableStatement statement = connection.prepareCall("RUNSCRIPT FROM ? COMPRESSION ZIP");
					statement.setString(1, file.getAbsolutePath());
					statement.execute();
				} 
				catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.doQuery();
		
		new RevisionExecutor(new StructureUpgrader()).execute();
		// TODO: Reload all tabs and importers!
	}

	@Override
	public void setAdditionalParameters(JFileChooser chooser) {
		setTitle("Import database from...");
		chooser.setApproveButtonText("Import file");
	}

}
