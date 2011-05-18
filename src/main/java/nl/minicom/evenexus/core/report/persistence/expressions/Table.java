package nl.minicom.evenexus.core.report.persistence.expressions;

import nl.minicom.evenexus.core.report.persistence.QueryBuilder;

/**
 * <p>The {@link Table} class will translate to a table in the 
 * {@link QueryBuilder} class. This class also provides an additional 
 * constructor to allow for aliassing.</p>
 * 
 * <p>This class is meant to be Immutable and thus thread-safe!</p>
 * 
 * @author Michael
 */
public class Table extends Expression {
	
	public static final Table TRANSACTIONS = new Table("transactions");
	public static final Table JOURNAL = new Table("journal");
	public static final Table PROFIT = new Table("profit");
	
	private final String tableName;
	private final String alias;
	
	/**
	 * This constructor creates a {@link Table} object.
	 * 
	 * @param tableName		The name of the table.
	 */
	public Table(String tableName) {
		this(tableName, null);
	}

	/**
	 * This constructor creates a {@link Table} object with an alias.
	 * 
	 * @param tableName		The name of the table.
	 * @param alias			The alias of this table.
	 */
	public Table(String tableName, String alias) {
		this.tableName = tableName;
		this.alias = alias;
	}

	@Override
	public void writeTranslation(QueryBuilder query) {
		query.append(tableName);
		if (alias != null) {
			query.append(" ");
			query.append(alias);
		}
	}
	
	@Override
	public int hashCode() {
		return tableName.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Table)) {
			return false;
		}
		
		String otherTableName = ((Table) other).tableName;
		if (otherTableName == null) {
			return tableName == null;
		}
		return otherTableName.equals(tableName);
	}

	/**
	 * @return the name of the table.
	 */
	public String getTableName() {
		return tableName;
	}

}
