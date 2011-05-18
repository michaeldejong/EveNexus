package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This class constructs and SQL-AVG expression from 
 * {@link Expression} object provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Avg extends SingleArgumentExpression {

	/**
	 * This constructor creates an SQL-AVG expression.
	 * 
	 * @param argument	The {@link Expression} to operater AVG on.
	 */
	public Avg(Expression argument) {
		super("AVG", argument);
	}

}
