package nl.minicom.evenexus.core.report.persistence.expressions;

import java.util.Date;

/**
 * <p>This class constructs and YEAR expression from an
 * {@link Expression} object provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class Year extends SingleArgumentExpression {

	/**
	 * This constructor creates a {@link Year} object.
	 * 
	 * @param argument		An {@link Expression} containing a {@link Date}.
	 */
	public Year(Expression argument) {
		super("YEAR", argument);
	}

}
