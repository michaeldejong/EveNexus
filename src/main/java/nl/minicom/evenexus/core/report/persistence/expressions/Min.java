package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This class constructs and SQL-MIN expression from 
 * {@link Expression} object provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Min extends SingleArgumentExpression {

	/**
	 * This constructor will create a SQL-MIN expression.
	 * 
	 * @param argument		The {@link Expression} to apply MIN to.
	 */
	public Min(Expression argument) {
		super("MIN", argument);
	}

}
