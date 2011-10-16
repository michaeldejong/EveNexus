package nl.minicom.evenexus.persistence.versioning;

import java.util.List;

import javax.inject.Inject;

import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.dao.Version;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.Session;

import com.google.common.base.Preconditions;

public class RevisionExecutor {
	
	private final Database database;
	
	@Inject
	public RevisionExecutor(Database database) {
		this.database = database;
	}
	
	@Transactional
	public Version execute(RevisionCollection revisions) {
		Preconditions.checkNotNull(revisions);
		Session session = database.getCurrentSession();
		Version dbVersion = getCurrentVersion(revisions, session);
		List<IRevision> revisionList = revisions.getRevisions();
		IRevision lastRevision = revisionList.get(revisionList.size() - 1);
		if (dbVersion.getRevision() > lastRevision.getRevisionNumber()) {
			String errorMessage = "Database has higher revision number than in version control!";
			throw new IllegalStateException(errorMessage); 
		}
		for (IRevision revision : revisionList) {
			if (revision.getRevisionNumber() > dbVersion.getRevision()) {
				revision.execute(session);
				dbVersion.setRevision(revision.getRevisionNumber());
				session.saveOrUpdate(dbVersion);
			}
		}
		return dbVersion;
	}

	private Version getCurrentVersion(RevisionCollection revisions, Session session) {
		String query = "SHOW TABLES";

		@SuppressWarnings("unchecked")
		List<Object[]> tables = (List<Object[]>) session.createSQLQuery(query).list();
		
		boolean tableExists = false;
		for (Object[] table : tables) {
			String tableName = table[0].toString().toLowerCase();
			if ("versioning".equals(tableName)) {
				tableExists = true;
			}
		}
		
		if (!tableExists) {
			String sql = new StringBuilder()
			.append("CREATE TABLE IF NOT EXISTS versioning (")
			.append("type VARCHAR(64) NOT NULL,")
			.append("version INT NOT NULL,")
			.append("revision INT NOT NULL,")
			.append("PRIMARY KEY(`type`))")
			.toString();
			
			session.createSQLQuery(sql).executeUpdate();
		}
		
		Version version = (Version) session.get(Version.class, revisions.getRevisionType());
		if (version == null) {
			version = new Version();
			version.setType(revisions.getRevisionType());
			version.setRevision(-1);
			session.saveOrUpdate(version);
		}
		
		return version;
	}
	
}
