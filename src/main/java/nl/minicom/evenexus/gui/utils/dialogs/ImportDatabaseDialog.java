package nl.minicom.evenexus.gui.utils.dialogs;


import java.io.File;

import javax.swing.JFileChooser;

import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.versioning.RevisionExecutor;
import nl.minicom.evenexus.persistence.versioning.StructureUpgrader;

import org.hibernate.SQLQuery;
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
				session.createSQLQuery("DROP ALL OBJECTS").executeUpdate();
				SQLQuery statement = session.createSQLQuery("RUNSCRIPT FROM ? COMPRESSION ZIP");
				statement.setString(0, file.getAbsolutePath());
				statement.executeUpdate();
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
