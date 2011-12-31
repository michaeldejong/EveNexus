package nl.minicom.evenexus.gui.utils.dialogs;


import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.swing.JFileChooser;

import nl.minicom.evenexus.gui.Gui;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.versioning.RevisionExecutor;
import nl.minicom.evenexus.persistence.versioning.StructureUpgrader;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 * This dialog allows the user to import an old backup of the database.
 * 
 * @author michael
 */
public class ImportDatabaseDialog extends DatabaseFileChooser {

	private static final long serialVersionUID = -2633245343435662634L;
	
	private final Gui gui;
	private final RevisionExecutor executor;
	private final Database database;
	
	/**
	 * Constructs a new {@link ImportDatabaseDialog} dialog.
	 * 
	 * @param gui
	 * 		The {@link Gui}.
	 * 
	 * @param executor
	 * 		The {@link RevisionExecutor}.
	 * 
	 * @param database
	 * 		The {@link Database}.
	 */
	@Inject
	public ImportDatabaseDialog(Gui gui, RevisionExecutor executor, Database database) {
		this.gui = gui;
		this.executor = executor;
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
				CallableStatement statement = connection.prepareCall("RUNSCRIPT FROM ? COMPRESSION ZIP");
				statement.setString(1, file.getAbsolutePath());
				statement.execute();
			}
		};
		
		Session session = database.getCurrentSession();
		session.createSQLQuery("DROP ALL OBJECTS").executeUpdate();
		session.doWork(work);
		
		executor.execute(new StructureUpgrader());
		
		gui.reload();
	}

	@Override
	public void setAdditionalParameters(JFileChooser chooser) {
		setTitle("Import database from...");
		chooser.setApproveButtonText("Import file");
	}

}
