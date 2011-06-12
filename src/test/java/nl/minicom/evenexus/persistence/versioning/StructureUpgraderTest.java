package nl.minicom.evenexus.persistence.versioning;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.ApiKey;
import nl.minicom.evenexus.persistence.dao.ImportLog;
import nl.minicom.evenexus.persistence.dao.Importer;
import nl.minicom.evenexus.persistence.dao.Item;
import nl.minicom.evenexus.persistence.dao.MapRegion;
import nl.minicom.evenexus.persistence.dao.MarketOrder;
import nl.minicom.evenexus.persistence.dao.Profit;
import nl.minicom.evenexus.persistence.dao.Skill;
import nl.minicom.evenexus.persistence.dao.Standing;
import nl.minicom.evenexus.persistence.dao.Station;
import nl.minicom.evenexus.persistence.dao.Version;
import nl.minicom.evenexus.persistence.dao.WalletJournal;
import nl.minicom.evenexus.persistence.dao.WalletTransaction;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StructureUpgraderTest {
	
	private StructureUpgrader instance;
	
	/**
	 * This method sets up a test case on which we can run tests.
	 * @throws SQLException 
	 */
	@Before
	public void setup() throws SQLException {
		instance = new StructureUpgrader();
		dropDatabase();
	}
	
	@After
	public void tearDown() throws SQLException {
		dropDatabase();
	}
	
	@Test
	public void testRevisionType() {
		Assert.assertEquals("database", instance.getRevisionType());
	}
	
	@Test(expected = java.lang.UnsupportedOperationException.class)
	public void testCannotRegisterAdditionalRevisions() {
		instance.registerRevision(null);
	}
	
	@Test
	public void testFinalStructure() throws SQLException {
		// Upgrade structure
		Version version = new RevisionExecutor(instance).execute();
		
		// Assert versioning
		Assert.assertEquals(version.getType(), "database");
		Assert.assertEquals(version.getRevision(), instance.getLastRevisionNumber());
		Assert.assertEquals(version.getVersion(), 0);
		
		// Assert consistency for all models
		Assert.assertTrue(structureCheck(ApiKey.class));
		Assert.assertTrue(structureCheck(Importer.class));
		Assert.assertTrue(structureCheck(ImportLog.class));
		Assert.assertTrue(structureCheck(Item.class));
		Assert.assertTrue(structureCheck(WalletJournal.class));
		Assert.assertTrue(structureCheck(MapRegion.class));
		Assert.assertTrue(structureCheck(MarketOrder.class));
		Assert.assertTrue(structureCheck(Profit.class));
		Assert.assertTrue(structureCheck(Skill.class));
		Assert.assertTrue(structureCheck(Standing.class));
		Assert.assertTrue(structureCheck(Station.class));
		Assert.assertTrue(structureCheck(WalletTransaction.class));
		Assert.assertTrue(structureCheck(Version.class));
		
	}
	
	private boolean structureCheck(Class<? extends Serializable> entityClass) {
		Table table = entityClass.getAnnotation(Table.class);
		Field[] fields = entityClass.getDeclaredFields();
		
		List<String> columnNames = listFields(fields);		
		Object[] dbColumnNames = listDbColumnNames(table.name());
		for (int i = 0; i < dbColumnNames.length; i++) {
			String dbColumnName = ((Object[]) dbColumnNames[i])[0].toString().toLowerCase();
			if (columnNames.contains(dbColumnName)) {
				columnNames.remove(dbColumnName);
			}
			else {
				Assert.fail("Could not match column: " + dbColumnName + " in entity: " + entityClass);
			}
		}
		
		return columnNames.isEmpty();
	}
	
	private List<String> listFields(Field[] fields) {
		List<String> columnNames = new ArrayList<String>();
		for (Field field : fields) {
			Column annotation = field.getAnnotation(Column.class);
			if (annotation != null) {
				columnNames.add(annotation.name().toLowerCase());
			}
			else {
				Id idAnnotation = field.getAnnotation(Id.class);
				if (idAnnotation != null) {
					columnNames.addAll(listFields(field.getType().getDeclaredFields()));
				}
			}
		}
		return columnNames;
	}
	
	private Object[] listDbColumnNames(final String tableName) {
		return new Query<Object[]>() {
			@Override
			protected Object[] doQuery(Session session) {
				String sql = "SHOW COLUMNS FROM " + tableName;
				SQLQuery query = session.createSQLQuery(sql);
				return query.list().toArray();
			}
		}.doQuery();
	}

	private void dropDatabase() {
		new Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				session.createSQLQuery("DROP ALL OBJECTS").executeUpdate();
				return null;
			}
		}.doQuery();
	}
	
}
