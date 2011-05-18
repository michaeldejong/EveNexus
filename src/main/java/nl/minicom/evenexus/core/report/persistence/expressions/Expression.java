package nl.minicom.evenexus.core.report.persistence.expressions;

import nl.minicom.evenexus.core.report.persistence.QueryBuilder;

/**
 * <p>This class represents a SQL-expression of some form.
 * Its main purpose is to serve as an AST node to build a
 * more complicated {@link Expression}, which can be used
 * in a query builder.</p>
 * 
 * <p>This class is meant to be Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public abstract class Expression {
	
	/**
	 * This method ensures that we write a translation of this 
	 * {@link Expression} to the {@link QueryBuilder}.
	 * 
	 * @param query 	The {@link QueryBuilder} we will append
	 * 					our translation to.
	 */
	public abstract void writeTranslation(QueryBuilder query);

}
