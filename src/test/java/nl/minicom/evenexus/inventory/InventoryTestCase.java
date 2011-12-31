package nl.minicom.evenexus.inventory;

import java.util.ArrayList;
import java.util.List;

import nl.minicom.evenexus.persistence.dao.Profit;
import nl.minicom.evenexus.persistence.dao.TransactionMatch;
import nl.minicom.evenexus.persistence.dao.WalletTransaction;

import org.junit.Ignore;

/**
 * This class describes a list of {@link WalletTransaction}s and their outcomes, when they have been 
 * processed by an InventoryWorker.
 * 
 * @author michael
 */
@Ignore
public final class InventoryTestCase {

	private final List<WalletTransaction> initialTransactions;
	private final List<WalletTransaction> finalTransactions;
	private final List<TransactionMatch> initialMatches;
	private final List<Profit> generatedProfitEntries;

	/**
	 * Constructs a new {@link InventoryTestCase} object.
	 */
	InventoryTestCase() {
		this.initialTransactions = new ArrayList<WalletTransaction>();
		this.initialMatches = new ArrayList<TransactionMatch>();
		this.finalTransactions = new ArrayList<WalletTransaction>();
		this.generatedProfitEntries = new ArrayList<Profit>();
	}

	/**
	 * @return
	 * 		A {@link List} of intial {@link WalletTransaction}s.
	 */
	public List<WalletTransaction> getInitialTransactions() {
		return initialTransactions;
	}

	/**
	 * @return
	 * 		A {@link List} of intially matched {@link WalletTransaction}s in {@link TransactionMatch} objects.
	 */
	public List<TransactionMatch> getInitialMatches() {
		return initialMatches;
	}

	/**
	 * @return
	 * 		A {@link List} of {@link WalletTransaction} which should be present in the database after processing.
	 */
	public List<WalletTransaction> getFinalTransactions() {
		return finalTransactions;
	}

	/**
	 * @return
	 * 		A {@link List} of {@link Profit} entries, generated while processing.
	 */
	public List<Profit> getGeneratedProfitEntries() {
		return generatedProfitEntries;
	}

}