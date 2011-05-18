package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This class constructs and SQL-OR expression from the two
 * {@link Expression}s objects provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Or extends DoubleArgumentExpression {

	/**
	 * This constructor will create an {@link Expression} which is the same as the
	 * OR expression used by relational databases.
	 * 
	 * @param left		The {@link Expression} to be on the left side of the OR-operator.
	 * @param right		The {@link Expression} to be on the right side of the OR-operator.
	 */
	public Or(Expression left, Expression right) {
		super("OR", left, right);
	}

}
