package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This class constructs and SQL-EQUALS expression consisting of 
 * two sepearate {@link Expression}s objects which are provided via 
 * the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Equals extends DoubleArgumentExpression {

	/**
	 * This constructor will create an EQUALS-expression.
	 * 
	 * @param leftSide		The {@link Expression} which will be on the left side of the
	 * 						operator. This is guaranteed to stay on the left side.
	 * 
	 * @param rightSide		The {@link Expression} which will be on the right side of the
	 * 						operator. This is guaranteed to stay on the right side.
	 */
	public Equals(Expression leftSide, Expression rightSide) {
		super("=", leftSide, rightSide);
	}

}
