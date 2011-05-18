package nl.minicom.evenexus.core.report.persistence.expressions;

import nl.minicom.evenexus.core.report.persistence.QueryBuilder;

/**
 * <p>This class constructs and SQL expression consisting of multiple 
 * {@link Expression} objects.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class MultiArgumentExpression extends Expression {
	
	private final String expression;
	private final Expression[] expressions;
	
	/**
	 * This constructor creates a {@link MultiArgumentExpression} object from 
	 * multiple {@link Expression} objects.
	 * 
	 * @param expression		The operator to prefix all {@link Expression} objects.
	 * 
	 * @param expressions		An array of {@link Expression} object to apply the operation on.
	 */
	protected MultiArgumentExpression(String expression, Expression... expressions) {
		this.expression = expression;
		this.expressions = expressions;
	}

	@Override
	public void writeTranslation(QueryBuilder query) {
		query.append(expression);
		query.append("(");
		int count = 0;
		for (Expression expression : expressions) {
			if (count > 0) {
				query.append(",");
			}
			if (expression instanceof Column || expression instanceof Value) {
				expression.writeTranslation(query);
			}
			else {
				query.append("(");
				expression.writeTranslation(query);
				query.append(")");
			}
			count++;
		}
		query.append(")");
	}	

}
