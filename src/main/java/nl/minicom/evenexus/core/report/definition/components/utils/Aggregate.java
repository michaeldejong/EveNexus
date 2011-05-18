package nl.minicom.evenexus.core.report.definition.components.utils;

import nl.minicom.evenexus.core.report.persistence.expressions.Avg;
import nl.minicom.evenexus.core.report.persistence.expressions.Count;
import nl.minicom.evenexus.core.report.persistence.expressions.Expression;
import nl.minicom.evenexus.core.report.persistence.expressions.Max;
import nl.minicom.evenexus.core.report.persistence.expressions.Min;
import nl.minicom.evenexus.core.report.persistence.expressions.Sum;

/**
 * Aggregates are ways on how we should operate on a given dataset.
 * 
 * @author Michael
 */
public enum Aggregate {

	SUM {
		@Override
		public Expression createExpression(Expression expression) {
			return new Sum(expression);
		}
	},
	
	AVG {
		@Override
		public Expression createExpression(Expression expression) {
			return new Avg(expression);
		}
	},
	
	MIN {
		@Override
		public Expression createExpression(Expression expression) {
			return new Min(expression);
		}
	},
	
	MAX {
		@Override
		public Expression createExpression(Expression expression) {
			return new Max(expression);
		}
	},
	
	COUNT {
		@Override
		public Expression createExpression(Expression expression) {
			return new Count(expression);
		}
	},
	
	NONE {
		@Override
		public Expression createExpression(Expression expression) {
			return expression;
		}
	};
	
	/**
	 * This method will envelop a given {@link Expression} into a aggregate {@link Expression}.
	 * 
	 * @param expression	The {@link Expression} to envelop.
	 * @return				An aggregate {@link Expression}.
	 */
	public abstract Expression createExpression(Expression expression);
	
}
