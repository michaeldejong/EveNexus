package nl.minicom.evenexus.core.report.persistence.expressions;

import java.util.Date;

/**
 * <p>This class constructs and DAY_OF_YEAT-expression built 
 * with a {@link Expression} object provided via the constructor.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class DayOfYear extends SingleArgumentExpression {
	
	/**
	 * This constructor will create a DayOfYear {@link Expression}.
	 * 
	 * @param argument		The {@link Expression} containing a {@link Date}.
	 */
	public DayOfYear(Expression argument) {
		super("DAY_OF_YEAR", argument);
	}

}
