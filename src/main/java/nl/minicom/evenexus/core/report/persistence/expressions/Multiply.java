package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This class constructs and SQL multiply expression from the two
 * {@link Expression}s objects provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Multiply extends DoubleArgumentExpression {

	/**
	 * This constructor will create an {@link Expression} which is the same as the
	 * * expression used by relational databases.
	 * 
	 * @param left		The {@link Expression} to be on the left side of the *-operator.
	 * @param right		The {@link Expression} to be on the right side of the *-operator.
	 */
	public Multiply(Expression left, Expression right) {
		super("*", left, right);
	}

}
