package nl.minicom.evenexus.inventory;

import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.persistence.dao.Profit;
import nl.minicom.evenexus.persistence.dao.WalletTransaction;

import org.junit.Ignore;

@Ignore
public class InventoryTestCase {

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