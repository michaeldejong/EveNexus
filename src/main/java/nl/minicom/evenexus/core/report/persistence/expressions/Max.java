package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This class constructs and SQL-MAX expression from 
 * {@link Expression} object provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Max extends SingleArgumentExpression {

	/**
	 * This constructor will create a SQL-MAX expression.
	 * 
	 * @param argument		The {@link Expression} to apply MAX to.
	 */
	public Max(Expression argument) {
		super("MAX", argument);
	}

}
