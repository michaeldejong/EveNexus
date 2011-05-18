package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This class constructs and SQL-NOT expression from 
 * {@link Expression} object provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Not extends SingleArgumentExpression {
	
	/**
	 * This constructor will create a SQL-NOT expression.
	 * 
	 * @param argument		The {@link Expression} to apply NOT to.
	 */
	public Not(Expression argument) {
		super("Not", argument);
	}

}
