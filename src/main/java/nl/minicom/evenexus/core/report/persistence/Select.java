package nl.minicom.evenexus.core.report.persistence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.minicom.evenexus.core.report.persistence.expressions.Expression;
import nl.minicom.evenexus.core.report.persistence.expressions.Table;

/**
 * <p>{@link Select} is a subclass of {@link Expression}. {@link Select} can be 
 * executed by a {@link QueryBuilder}.</p>
 *
 * @author Michael
 */
public class Select extends Expression {
	
	private final Table table;
	private final Select parentQuery;
	private final String alias;
	
	private final List<Expression> unaliasedExpressions = new ArrayList<Expression>();
	private final Map<String, Expression> aliasedExpressions = new LinkedHashMap<String, Expression>();
	
	private final Map<Table, Expression> joins = new LinkedHashMap<Table, Expression>();
	private final List<Expression> groupings = new ArrayList<Expression>();
	private final List<Expression> orderings = new ArrayList<Expression>();
	private final List<Select> unionedQueries = new ArrayList<Select>();
	
	private Expression condition = null;
	
	/**
	 * This constructor creates a {@link Select} based on a {@link Table} object.
	 * 
	 * @param table		The {@link Table} to base the {@link Select} on.
	 */
	public Select(Table table) {
		this.table = table;
		this.parentQuery = null;
		this.alias = null;
	}
	
	/**
	 * This constructor creates a {@link Select} based on another {@link Select} with an alias.
	 * 
	 * @param parentQuery		The {@link Select} which represents the sub query.
	 * @param alias				The alias for the sub query.
	 */
	public Select(Select parentQuery, String alias) {
		this.table = null;
		this.parentQuery = parentQuery;
		this.alias = alias;
	}

	/**
	 * @return 	The {@link Table} on which this {@link Select} is based. 
	 * 			Returns null if the {@link Select} is based on {@link Select}.
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * This method adds an {@link Expression} to the {@link Select}.
	 * 
	 * @param expression	The {@link Expression} to add to the {@link Select}.
	 */
	public void addExpression(Expression expression) {
		addExpression(expression, null);
	}
	
	/**
	 * This method adds an {@link Expression} to the {@link Select}.
	 * 
	 * @param expression	The {@link Expression} to add to the {@link Select}. 
	 * @param alias			The alias for the {@link Expression}.
	 */
	public void addExpression(Expression expression, String alias) {
		if (alias == null) {
			unaliasedExpressions.add(expression);
		}
		else {
			aliasedExpressions.put(alias, expression);
		}
	}
	
	/**
	 * Sets a condition for the {@link Select}.
	 * 
	 * @param condition		to set for the {@link Select}.
	 */
	public void setCondition(Expression condition) {
		this.condition = condition;
	}
	
	/**
	 * @return		The condition for this {@link Select}.
	 */
	public Expression getCondition() {
		return condition;
	}
	
	/**
	 * Adds a join to the {@link Select}.
	 * 
	 * @param table			The name of the table to join.
	 * @param expression	The {@link Expression} to join this table to the {@link Select}.
	 */
	public void addJoin(String table, Expression expression) {
		addJoin(new Table(table), expression);
	}
	
	/**
	 * Adds a join to the {@link Select}. 
	 * 
	 * @param table			The name of the table to join.
	 * @param alias			The alias of the table to join.
	 * @param expression	The {@link Expression} to join this table to the {@link Select}.
	 */
	public void addJoin(String table, String alias, Expression expression) {
		addJoin(new Table(table, alias), expression);
	}
	
	/**
	 * Adds a join to the {@link Select}.
	 * 
	 * @param table			The {@link Table} to join.
	 * @param expression	The {@link Expression} to join this table to the {@link Select}.
	 */
	public void addJoin(Table table, Expression expression) {
		joins.put(table, expression);
	}

	/**
	 * Adds a grouping to the {@link Select}.
	 * 
	 * @param expression	The {@link Expression} to group on.
	 */
	public void addGroup(Expression expression) {
		groupings.add(expression);
	}

	/**
	 * Adds an order to the {@link Select}.
	 * 
	 * @param expression	The {@link Expression} to order by.
	 */
	public void addOrder(Expression expression) {
		orderings.add(expression);
	}
	
	/**
	 * Unions this {@link Select} to the provided {@link Select}.
	 *  
	 * @param union		The {@link Select} to union this {@link Select} with.
	 */
	public void addUnion(Select union) {
		unionedQueries.add(union);
	}

	@Override
	public void writeTranslation(QueryBuilder query) {
		
		if (!unionedQueries.isEmpty()) {
			query.append("(");
		}
		
		// Select clause
		query.append("SELECT");
		
		int index = 0;
		for (Entry<String, Expression> entry : aliasedExpressions.entrySet()) {
			if (index > 0) {
				query.append(",");
			}
			entry.getValue().writeTranslation(query);
			query.append(" AS " + entry.getKey());
			index++;
		}
		for (Expression expression : unaliasedExpressions) {
			if (index > 0) {
				query.append(",");
			}
			expression.writeTranslation(query);
			index++;
		}
		if (index == 0) {
			query.append("*");
		}
		
		// From clause
		query.append("FROM");
		if (table == null) {
			query.append("(");
			parentQuery.writeTranslation(query);
			query.append(") " + alias);
		}
		else {
			table.writeTranslation(query);
			for (Entry<Table, Expression> join : joins.entrySet()) {
				join.getKey().writeTranslation(query);
				query.append(" ON ");
				join.getValue().writeTranslation(query);
			}
		}
		
		// Where clause
		if (condition != null) {
			query.append("WHERE");
			condition.writeTranslation(query);
		}
		
		// Group by clause
		if (!groupings.isEmpty()) {
			index = 0;
			query.append("GROUP BY");
			for (Expression group : groupings) {
				if (index > 0) {
					query.append(",");
				}
				group.writeTranslation(query);
				index++;
			}
		}
		
		// Where clause
		if (!orderings.isEmpty()) {
			query.append("ORDER BY");
			
			index = 0;
			for (Expression order : orderings) {
				if (index > 0) {
					query.append(",");
				}
				order.writeTranslation(query);
				query.append("ASC");
				index++;
			}
		}
		
		if (!unionedQueries.isEmpty()) {
			query.append(")");
			for (Select unionedQuery : unionedQueries) {
				query.append("UNION");
				query.append("(");
				unionedQuery.writeTranslation(query);
				query.append(")");
			}
		}
	}

}
