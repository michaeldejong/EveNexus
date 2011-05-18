package nl.minicom.evenexus.persistence.versioning;

import java.util.List;

import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.Version;

import org.apache.commons.lang.Validate;
import org.hibernate.Session;

public class RevisionExecutor {
	
	private final RevisionCollection revisions;
	
	public RevisionExecutor(RevisionCollection revisions) {
		Validate.notNull(revisions);
		this.revisions = revisions;
	}
	
	public Version execute() {
		return new Query<Version>() {
			@Override
			protected Version doQuery(Session session) {
				Version dbVersion = getCurrentVersion(session);
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
		}.doQuery();
	}

	private Version getCurrentVersion(Session session) {
		String query = "SHOW TABLES";

		@SuppressWarnings("unchecked")
		List<Object[]> tables = (List<Object[]>) session.createSQLQuery(query).list();
		
		boolean tableExists = false;
		for (Object[] table : tables) {
			String tableName = table[0].toString().toLowerCase();
			if (tableName.equals("versioning")) {
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
