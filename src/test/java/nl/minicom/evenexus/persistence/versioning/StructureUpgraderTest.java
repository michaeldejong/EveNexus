package nl.minicom.evenexus.persistence.versioning;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import nl.minicom.evenexus.TestModule;
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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class StructureUpgraderTest {
	
	private RevisionExecutor executor;
	private RevisionUtil util;
	private StructureUpgrader instance;
	
	/**
	 * This method sets up a test case on which we can run tests.
	 * @throws SQLException 
	 */
	@Before
	public void setup() throws SQLException {
		Injector injector = Guice.createInjector(new TestModule());
		executor = injector.getInstance(RevisionExecutor.class);
		util = injector.getInstance(RevisionUtil.class);
		instance = new StructureUpgrader();
		util.dropDatabase();
	}
	
	@After
	public void tearDown() throws SQLException {
		util.dropDatabase();
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
		Version version = executor.execute(instance, true);
		
		// Assert versioning
		Assert.assertEquals(version.getType(), "database");
		Assert.assertEquals(version.getRevision(), instance.getLastRevisionNumber());
		Assert.assertEquals(version.getVersion(), 0);
		
		// Assert consistency for all models
		Assert.assertTrue(structureCheck(ApiKey.class));
		Assert.assertTrue(structureCheck(Importer.class));
		Assert.assertTrue(structureCheck(ImportLog.class));
		Assert.assertTrue(structureCheck(Item.class));
		Assert.assertTrue(structureCheck(MapRegion.class));
		Assert.assertTrue(structureCheck(MarketOrder.class));
		Assert.assertTrue(structureCheck(Profit.class));
		Assert.assertTrue(structureCheck(Skill.class));
		Assert.assertTrue(structureCheck(Standing.class));
		Assert.assertTrue(structureCheck(Station.class));
		Assert.assertTrue(structureCheck(WalletJournal.class));
		Assert.assertTrue(structureCheck(WalletTransaction.class));
		Assert.assertTrue(structureCheck(Version.class));
	}
	
	private boolean structureCheck(Class<? extends Serializable> entityClass) {
		Table table = entityClass.getAnnotation(Table.class);
		Field[] fields = entityClass.getDeclaredFields();
		
		List<String> columnNames = listFields(fields);		
		Object[] dbColumnNames = util.listDbColumnNames(table.name());
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
			Id idAnnotation = field.getAnnotation(Id.class);
			if (annotation != null) {
				columnNames.add(annotation.name().toLowerCase());
			}
			else if (idAnnotation != null) {
				columnNames.addAll(listFields(field.getType().getDeclaredFields()));
			}
		}
		return columnNames;
	}

}
