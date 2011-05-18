package nl.minicom.evenexus.gui.utils.dialogs;

import java.io.File;

import javax.swing.JFileChooser;

import nl.minicom.evenexus.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.Session;


public class ExportDatabaseDialog extends DatabaseFileChooser {

	private static final long serialVersionUID = -2633245343435662634L;
	
	@Override
	public void onFileSelect(final File file) {
		if (file == null) {
			return;
		}
		
		new Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				SQLQuery statement = session.createSQLQuery("SCRIPT TO ? COMPRESSION ZIP");
				statement.setString(0, file.getAbsolutePath());
				statement.executeUpdate();
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
