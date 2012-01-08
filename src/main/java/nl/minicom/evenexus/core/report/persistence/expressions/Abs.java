package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This class constructs and SQL-ABS expression from 
 * {@link Expression} object provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Abs extends SingleArgumentExpression {

	/**
	 * This constructor creates an SQL-ABS expression.
	 * 
	 * @param argument	The {@link Expression} to operater ABS on.
	 */
	public Abs(Expression argument) {
		super("ABS", argument);
	}

}
