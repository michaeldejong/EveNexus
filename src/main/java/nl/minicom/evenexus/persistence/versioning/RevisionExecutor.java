package nl.minicom.evenexus.persistence.versioning;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.persistence.dao.Version;

import com.google.common.base.Preconditions;

public class RevisionExecutor {
	
	public Version execute(RevisionCollection revisions, boolean isTest) throws SQLException {
		Preconditions.checkNotNull(revisions);
		
		Connection conn = connect(isTest);

		Version dbVersion = getCurrentVersion(revisions, conn);
		List<IRevision> revisionList = revisions.getRevisions();
		IRevision lastRevision = revisionList.get(revisionList.size() - 1);
		if (dbVersion.getRevision() > lastRevision.getRevisionNumber()) {
			String errorMessage = "Database has higher revision number than in version control!";
			throw new IllegalStateException(errorMessage); 
		}
		for (IRevision revision : revisionList) {
			if (revision.getRevisionNumber() > dbVersion.getRevision()) {
				revision.execute(conn);
				dbVersion.setRevision(revision.getRevisionNumber());
				updateVersion(conn, dbVersion);
			}
		}
		
		close(conn);
		
		return dbVersion;
	}
	
	private Connection connect(boolean isTest) {
		try {
			Class.forName("org.h2.Driver");
			if (isTest) {
				return DriverManager.getConnection("jdbc:h2:database/test", "root", "");
			}
			return DriverManager.getConnection("jdbc:h2:database/database", "root", "");
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void close(Connection conn) {
		try {
			conn.close();
		} 
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Version getCurrentVersion(RevisionCollection revisions, Connection conn) throws SQLException {
		String query = "SHOW TABLES";
		
		ResultSet s = conn.createStatement().executeQuery(query);
		List<String> tables = new ArrayList<String>();
		
		while (s.next()) {
			tables.add(s.getString(1));
		}
		
		boolean tableExists = false;
		for (String table : tables) {
			if ("versioning".equalsIgnoreCase(table)) {
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
			
			conn.createStatement().execute(sql);
		}
		
		Version version = getVersion(conn, revisions.getRevisionType());
		if (version == null) {
			version = new Version();
			version.setType(revisions.getRevisionType());
			version.setRevision(-1);
			updateVersion(conn, version);
		}
		
		return version;
	}
	
	private void updateVersion(Connection conn, final Version version) throws SQLException {
		String query = "MERGE INTO versioning (type, revision, version) VALUES (?, ?, ?)";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, version.getType());
		statement.setInt(2, version.getRevision());
		statement.setInt(3, version.getVersion());
		statement.execute();
	}

	
	private Version getVersion(Connection conn, final String revisionType) throws SQLException {
		final Version dbVersion = new Version();
		dbVersion.setRevision(-1);
		dbVersion.setVersion(-1);
		dbVersion.setType(revisionType);
		
		new Query(conn) {
			@Override
			public PreparedStatement createStatement(Connection conn) throws SQLException {
				PreparedStatement statement = conn.prepareStatement("SELECT * FROM versioning WHERE type = ? LIMIT 1");
				statement.setString(1, revisionType);
				return statement;
			}
			
			@Override
			public void processResultSet(ResultSet resultSet) throws SQLException {
				if (resultSet.next()) {
					dbVersion.setRevision(resultSet.getInt("revision"));
					dbVersion.setVersion(resultSet.getInt("version"));
				}
			}
		};
		
		if (dbVersion.getVersion() >= 0) {
			return dbVersion;
		}
		
		return null;
	}
	
}
