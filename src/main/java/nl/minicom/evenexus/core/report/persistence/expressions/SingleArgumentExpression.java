package nl.minicom.evenexus.core.report.persistence.expressions;

import nl.minicom.evenexus.core.report.persistence.QueryBuilder;

/**
 * <p>This class constructs and SQL-expression built out of a
 * {@link String} operator and a single {@link Expression} object 
 * provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class SingleArgumentExpression extends Expression {
	
	private final Expression argument;
	private final String operator;
	
	/**
	 * This constructor will create a SingleArgumentExpression. One example of this would
	 * be the NOT expression => NOT(argument). 
	 * 
	 * @param operator		A {@link String} operator we need to prefix the {@link Expression} 
	 * 						argument. One example is "NOT".
	 * 
	 * @param argument		The {@link Expression} which will be operated on.
	 */
	protected SingleArgumentExpression(String operator, Expression argument) {
		this.argument = argument;
		this.operator = operator;
	}

	@Override
	public void writeTranslation(QueryBuilder query) {
		query.append(operator);
		query.append("(");
		argument.writeTranslation(query);
		query.append(")");
	}	

}
