package nl.minicom.evenexus.core.report.persistence.expressions;

import nl.minicom.evenexus.core.report.persistence.QueryBuilder;

/**
 * <p>This is a special class of {@link Expression}. This class given
 * a columnName, will translate to a columnName in a SQL-query.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 * 
 * @author Michael
 */
public class Column extends Expression {

	private final String columnName;

	/**
	 * This constructor will create a {@link Column} expression.
	 * 
	 * @param columnName 	The name of the column.
	 */
	public Column(String columnName) {
		this.columnName = columnName;
	}

	@Override
	public void writeTranslation(QueryBuilder query) {
		query.append(columnName);
	}

}
