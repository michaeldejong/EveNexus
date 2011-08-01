package nl.minicom.evenexus.gui.tables.datamodel.implementations;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.minicom.evenexus.gui.tables.datamodel.ITableDataModel;
import nl.minicom.evenexus.gui.utils.dialogs.BugReportDialog;
import nl.minicom.evenexus.persistence.Database;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImporterTableDataModel implements ITableDataModel {

	private static final Logger LOG = LoggerFactory.getLogger(ImporterTableDataModel.class);
	
	private final Database database;
	private final BugReportDialog dialog;
	
	@Inject
	public ImporterTableDataModel(Database database, BugReportDialog dialog) {
		this.database = database;
		this.dialog = dialog;
	}
	
	@Override
	public List<Object[]> reload() {
		try {
			return createQuery();			
		}
		catch (HibernateException e) {
			LOG.error(e.getLocalizedMessage(), e);
			dialog.setVisible(true);
			throw e;
		}
	}

	private List<Object[]> createQuery() {
		final StringBuilder sql = new StringBuilder()
		.append("SELECT ")
		.append("importlogger.lastrun, ")
		.append("importers.name, ")
		.append("apisettings.name AS characterName, ")
		.append("DATEADD('MILLISECOND', importers.cooldown, importlogger.lastRun) AS nextRun ")
		.append("FROM importlogger, importers, apisettings ")
		.append("WHERE apisettings.charid = importlogger.characterid ")
		.append("AND importlogger.importer = importers.id ")
		.append("ORDER BY nextRun ASC");
		
		List<Object[]> result = new ArrayList<Object[]>();
		Session session = database.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql.toString());
		ScrollableResults resultSet = query.scroll();
		if (resultSet.first()) {
			do {
				result.add(resultSet.get().clone());
			}
			while (resultSet.next());
		}
		return result; 
	}

	@Override
	public String[] getFields() {
		return new String[] {};
	}

}
