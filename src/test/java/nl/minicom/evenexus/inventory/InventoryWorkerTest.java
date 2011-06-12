package nl.minicom.evenexus.inventory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.persistence.Query;
import nl.minicom.evenexus.persistence.dao.Profit;
import nl.minicom.evenexus.persistence.dao.WalletTransaction;
import nl.minicom.evenexus.persistence.versioning.RevisionExecutor;
import nl.minicom.evenexus.persistence.versioning.StructureUpgrader;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class InventoryWorkerTest {
	
	@Before
	public void setup() {
		dropDatabase();
		new RevisionExecutor(new StructureUpgrader()).execute();
	}
	
	@After
	public void tearDown() {
		dropDatabase();
	}
	
	@Test
	public void testBuyTransactionsOnly() throws IOException {
		runTestCase("/inventory/test-case-01.json");
	}
	
	@Test
	public void testMixedTransactions() throws IOException {
		runTestCase("/inventory/test-case-02.json");
	}
	
	@Test
	public void testInvalidlyOrderedTransactions() throws IOException {
		runTestCase("/inventory/test-case-03.json");
	}
	
	private void runTestCase(String fileName) throws IOException {
		// Read test case from file.
		InventoryTestCase testCase = parseTestCase(fileName);
		
		// Prepare test case in database.
		prepareTestCase(testCase);
		
		// Run InventoryWorker on test case.
		new InventoryWorker(1L).run();
		
		// Check results.
		checkTestCaseResults(testCase);
	}
	
	private InventoryTestCase parseTestCase(String fileName) throws IOException {
		String line = null;
		StringBuilder builder = new StringBuilder();
		InputStream stream = getClass().getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		
		Type type = new TypeToken<InventoryTestCase>() {}.getType();
		return new Gson().fromJson(builder.toString(), type);
	}
	
	private void prepareTestCase(final InventoryTestCase testCase) {
		new Query<Void>() {
			@Override
			protected Void doQuery(Session session) {
				for (WalletTransaction walletTransaction : testCase.getInitialTransactions()) {
					session.save(walletTransaction);
				}
				return null;
			}
		}.doQuery();
	}
	
	@SuppressWarnings("unchecked")
	private void checkTestCaseResults(final InventoryTestCase testCase) {
		new Query<Void>() {
			@Override
			public Void doQuery(Session session) {
			
				List<WalletTransaction> transactions = session
					.createCriteria(WalletTransaction.class)
					.addOrder(Order.asc("transactionId"))
					.list();
				
				List<Profit> profits = session
					.createCriteria(Profit.class)
					.addOrder(Order.asc("id"))
					.list();
				
				if (testCase.getFinalTransactions().size() != transactions.size()) {
					Assert.fail("Unequal amount of transactions in db and calculated!");
				}
				
				if (testCase.getGeneratedProfitEntries().size() != profits.size()) {
					Assert.fail("Unequal amount of profits in db (" + profits.size() + 
							") and calculated (" + testCase.getGeneratedProfitEntries().size() + ")!");
				}
				
				for (WalletTransaction fileTransaction : testCase.getFinalTransactions()) {
					boolean matched = false;
					for (WalletTransaction dbTransaction : transactions) {
						if (dbTransaction.equals(fileTransaction)) {
							matched = true;
							break;
						}
					}
					
					if (matched) {
						transactions.remove(fileTransaction);
					}
					else {
						Assert.fail("Failed to match transaction: " + 
								fileTransaction.getTransactionId() + "!");
					}
				}
				
				for (Profit fileProfit : testCase.getGeneratedProfitEntries()) {
					boolean matched = false;
					for (Profit dbProfit : profits) {
						if (dbProfit.equals(fileProfit)) {
							matched = true;
						}
					}
					
					if (matched) {
						profits.remove(fileProfit);
					}
					else {
						Assert.fail("Failed to match profit: (" + 
								fileProfit.getId().getBuyTransactionId() + "," + 
								fileProfit.getId().getSellTransactionId() + ")!");
					}
				}
				return null;
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
	
	private final class InventoryTestCase {

		private List<WalletTransaction> initialTransactions;
		private List<WalletTransaction> finalTransactions;
		private List<Profit> generatedProfitEntries;

		private InventoryTestCase() {
			this.initialTransactions = new ArrayList<WalletTransaction>();
			this.finalTransactions = new ArrayList<WalletTransaction>();
			this.generatedProfitEntries = new ArrayList<Profit>();
		}
		
		public List<WalletTransaction> getInitialTransactions() {
			return initialTransactions;
		}
		
		public List<WalletTransaction> getFinalTransactions() {
			return finalTransactions;
		}
		
		public List<Profit> getGeneratedProfitEntries() {
			return generatedProfitEntries;
		}
		
	}
	
}
