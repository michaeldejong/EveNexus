package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This class constructs and MONTH expression from an
 * {@link Expression} object provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Month extends SingleArgumentExpression {

	/**
	 * This constructor creates a {@link Month} object.
	 * 
	 * @param argument
	 * 		An {@link Expression} containing a Date.
	 */
	public Month(Expression argument) {
		super("MONTH", argument);
	}

}
