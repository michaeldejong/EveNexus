package nl.minicom.evenexus.core.report.definition.components;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import nl.minicom.evenexus.core.report.persistence.expressions.Expression;
import nl.minicom.evenexus.core.report.persistence.expressions.Table;

/**
 * The {@link ReportGroup} class represents a grouping which will group SQL
 * data after having queried the requested data.
 *
 * @author Michael
 */
public class ReportGroup {
	
	private final String key;
	private final Map<Table, Expression> expressions;
	private final GroupTranslator translator;
	
	/**
	 * This constructor constructs a new {@link ReportGroup} object.
	 * There is no {@link GroupTranslator}, so the groupings will not be translated
	 * into something more user-readable.
	 * 
	 * @param key	The unique alias for this {@link ReportGroup}.
	 */
	public ReportGroup(String key) {
		this(key, null);
	}

	/**
	 * This constructor constructs a new {@link ReportGroup} object.
	 *
	 * @param key			The unique alias for this {@link ReportGroup}.
	 * @param translator	The {@link GroupTranslator}, which will translate 
	 * 						values to more user-readable values.
	 */
	public ReportGroup(String key, GroupTranslator translator) {
		this.key = key;
		this.translator = translator;
		this.expressions = new LinkedHashMap<Table, Expression>();
	}
	
	/**
	 * @return	The unique alias for this {@link ReportGroup}.
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * @param input		The input to translate.
	 * @return			The translation of the grouping value.
	 */
	public String translate(String input) {
		if (translator != null) {
			return translator.translate(input);
		}
		return input;
	}
	
	/**
	 * @return	A {@link Collection} of {@link Table} objects.
	 */
	public Collection<Table> getSupportedTables() {
		return Collections.unmodifiableCollection(expressions.keySet());
	}
	
	/**
	 * @return	A {@link Map} of {@link Table} and {@link Expression} objects.
	 */
	public Map<Table, Expression> getExpressions() {
		return Collections.unmodifiableMap(expressions);
	}
	
	/**
	 * This method defines a grouping for a certain {@link Table} and a {@link Expression}.
	 * 
	 * @param table		The {@link Table} to define an {@link Expression} for.
	 * @param groupBy	The {@link Expression} for said {@link Table}.
	 * @return			A pointer to this, so we can apply the Builder pattern.
	 */
	public ReportGroup defineExpression(Table table, Expression groupBy) {
		expressions.put(table, groupBy);
		return this;
	}

	/**
	 * This method will return the grouping {@link Expression} for a certain {@link Table}.
	 * 
	 * @param table		The {@link Table} for which we want an {@link Expression}.
	 * @return			The {@link Expression} for the provided {@link Table}.
	 */
	public Expression getExpression(Table table) {
		return expressions.get(table);
	}
	
}
