package nl.minicom.evenexus.core.report.definition.components;

import nl.minicom.evenexus.core.report.definition.components.utils.Aggregate;
import nl.minicom.evenexus.core.report.persistence.expressions.Expression;
import nl.minicom.evenexus.core.report.persistence.expressions.Table;

/**
 * The {@link ReportItem} class defines how to fetch and aggregate data from the database.
 *
 * @author Michael
 */
public class ReportItem {
	
	private final String key;
	private final Table table;
	private final Aggregate aggregate;
	private final Expression expression;
	private final Expression condition;

	/**
	 * This constructor will create a definition of a {@link ReportItem}.
	 * 
	 * @param key			The unique alias for this {@link ReportItem}.
	 * @param table			The {@link Table} to operator on.
	 * @param aggregate		How to aggregate the {@link Expression} itself.
	 * @param expression	The data {@link Expression}.
	 * @param condition		What data to include.
	 */
	public ReportItem(String key, Table table, Aggregate aggregate, Expression expression, Expression condition) {
		this.key = key;
		this.table = table;
		this.aggregate = aggregate;
		this.expression = expression;
		this.condition = condition;
	}
	
	/**
	 * @return	The unique alias for this {@link ReportItem}.
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * @return	The {@link Table} to operate on.
	 */
	public Table getRequiredTable() {
		return table;
	}
	
	/**
	 * @return	How the {@link ReportItem} has to be aggregated.
	 */
	public Aggregate getAggregate() {
		return aggregate;
	}
	
	/**
	 * @return	The {@link Expression} on how to calculate the data.
	 */
	public Expression getExpression() {
		return expression;
	}
	
	/**
	 * @return	The {@link Expression} which decides which data to use.
	 */
	public Expression getCondition() {
		return condition;
	}
	
}
