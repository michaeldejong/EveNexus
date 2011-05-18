package nl.minicom.evenexus.core.report.definition.components;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import nl.minicom.evenexus.core.report.persistence.expressions.Expression;
import nl.minicom.evenexus.core.report.persistence.expressions.Table;

/**
 * This class allows us to define a certain filter. A filter must supply 
 * an expression for every Table it needs to support.
 * 
 * @author Michael
 */
public class ReportFilter {
	
	private final String key;
	private final Map<Table, Expression> expressions;

	/**
	 * Constructs a ReportFilter.
	 * 
	 * @param key a unique name for this filter.
	 */
	public ReportFilter(String key) {
		this.key = key;
		this.expressions = new LinkedHashMap<Table, Expression>();
	}
	
	/**
	 * Returns the unique identifying name of this ReportFilter.
	 * @return unique identifying name.
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Returns a Collection of {@link Table} objects which have expressions
	 * defined for this ReportFilter.
	 * @return a Collection of supported {@link Table}
	 */
	public Collection<Table> getSupportedTables() {
		return Collections.unmodifiableCollection(expressions.keySet());
	}
	
	/**
	 * This will return a mapping of all {@link Expression}s to their respective {@link Table}s.
	 * @return a mapping of all expressions to their {@link Table}
	 */
	public Map<Table, Expression> getExpressions() {
		return Collections.unmodifiableMap(expressions);
	}
	
	/**
	 * Defines a filter {@link Expression} for a certain {@link Table}.
	 * 
	 * @param table			The table on which we can apply this filter.
	 * @param expression	The expression to filter on.
	 * @return				this (Builder pattern).
	 */
	public ReportFilter defineExpression(Table table, Expression expression) {
		expressions.put(table, expression);
		return this;
	}
	
}
