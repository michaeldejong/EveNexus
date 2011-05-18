package nl.minicom.evenexus.core.report.persistence.expressions;

import nl.minicom.evenexus.core.report.persistence.QueryBuilder;

/**
 * <p>This class constructs and SQL expression consisting of two
 * sepearate {@link Expression}s objects which are provided via 
 * the constructor. These {@link Expression}s are combined using
 * the {@link String} operator field.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 *
 * @author Michael
 */
public class DoubleArgumentExpression extends Expression {
	
	private final Expression leftExpression;
	private final Expression rightExpression;
	private final String operator;
	
	/**
	 * This constructor will create a DoubleArgumentExpression. One example of this would
	 * be the AND expression => (leftSide AND rightSide). Where left- and rightSide are
	 * respectively both {@link Expression} objects, seperated by the {@link String} operator.
	 * 
	 * @param operator		A {@link String} operator we need to seperate the leftSide and 
	 * 						rightSide {@link Expression}s with. One example is "AND".
	 * 
	 * @param leftSide		The {@link Expression} which will be on the left side of the
	 * 						operator. This is guaranteed to stay on the left side.
	 * 
	 * @param rightSide		The {@link Expression} which will be on the right side of the
	 * 						operator. This is guaranteed to stay on the right side.
	 * 
	 */
	protected DoubleArgumentExpression(String operator, Expression leftSide, Expression rightSide) {
		assert leftSide != null;
		assert rightSide != null;
		assert operator != null;
		
		this.leftExpression = leftSide;
		this.rightExpression = rightSide;
		this.operator = operator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeTranslation(QueryBuilder query) {
		if (leftExpression instanceof Column || leftExpression instanceof Value) {
			leftExpression.writeTranslation(query);
		}
		else {
			query.append("(");
			leftExpression.writeTranslation(query);
			query.append(")");
		}
		
		query.append(operator);
		
		if (rightExpression instanceof Column || rightExpression instanceof Value) {
			rightExpression.writeTranslation(query);
		}
		else {
			query.append("(");
			rightExpression.writeTranslation(query);
			query.append(")");
		}
	}	

}
