package nl.minicom.evenexus.core.report.persistence.expressions;


/**
 * <p>This class constructs and QUARTER expression from an
 * {@link Expression} object provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Quarter extends SingleArgumentExpression {

	/**
	 * This constructor creates a {@link Quarter} object.
	 * 
	 * @param argument
	 * 		An {@link Expression} containing a Date.
	 */
	public Quarter(Expression argument) {
		super("QUARTER", argument);
	}

}
