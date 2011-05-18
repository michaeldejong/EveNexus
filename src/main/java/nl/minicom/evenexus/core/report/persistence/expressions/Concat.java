package nl.minicom.evenexus.core.report.persistence.expressions;

/**
 * <p>This is a special {@link Expression}. The aim of this class is to 
 * concatenate all given {@link Expression} objects.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 * 
 * @author Michael
 */
public class Concat extends MultiArgumentExpression {
	
	/**
	 * This constructor creates a {@link Concat} expression object.
	 * 
	 * @param expressions		An array of {@link Expression} objects which will 
	 * 							be concatenated into one {@link Expression}.
	 */
	public Concat(Expression... expressions) {
		super("CONCAT", expressions);
	}

}
