package nl.minicom.evenexus.inventory;

import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.persistence.dao.Profit;
import nl.minicom.evenexus.persistence.dao.TransactionMatch;
import nl.minicom.evenexus.persistence.dao.WalletTransaction;

import org.junit.Ignore;

@Ignore
public final class InventoryTestCase {

	private final List<WalletTransaction> initialTransactions;
	private final List<WalletTransaction> finalTransactions;
	private final List<TransactionMatch> initialMatches;
	private final List<Profit> generatedProfitEntries;

	InventoryTestCase() {
		this.initialTransactions = new ArrayList<WalletTransaction>();
		this.initialMatches = new ArrayList<TransactionMatch>();
		this.finalTransactions = new ArrayList<WalletTransaction>();
		this.generatedProfitEntries = new ArrayList<Profit>();
	}

	public List<WalletTransaction> getInitialTransactions() {
		return initialTransactions;
	}

	public List<TransactionMatch> getInitialMatches() {
		return initialMatches;
	}

	public List<WalletTransaction> getFinalTransactions() {
		return finalTransactions;
	}

	public List<Profit> getGeneratedProfitEntries() {
		return generatedProfitEntries;
	}

}