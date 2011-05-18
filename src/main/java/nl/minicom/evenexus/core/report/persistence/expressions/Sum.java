package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This class constructs and SQL-SUM expression from 
 * {@link Expression} object provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Sum extends SingleArgumentExpression {

	/**
	 * This constructor will create a SQL-SUM expression.
	 * 
	 * @param argument		The {@link Expression} to apply SUM to.
	 */
	public Sum(Expression argument) {
		super("SUM", argument);
	}

}
