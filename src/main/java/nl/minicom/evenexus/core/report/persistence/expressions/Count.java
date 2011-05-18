package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This class constructs and SQL-COUNT expression from 
 * {@link Expression} object provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Count extends SingleArgumentExpression {

	/**
	 * This constructor will create a SQL-COUNT expression.
	 * 
	 * @param argument		The {@link Expression} to apply COUNT to.
	 */
	public Count(Expression argument) {
		super("COUNT", argument);
	}

}
