package nl.minicom.evenexus.core.report.engine;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;
import nl.minicom.evenexus.core.report.persistence.QueryBuilder;
import nl.minicom.evenexus.core.report.persistence.Select;
import nl.minicom.evenexus.core.report.persistence.expressions.And;
import nl.minicom.evenexus.core.report.persistence.expressions.Column;
import nl.minicom.evenexus.core.report.persistence.expressions.Equals;
import nl.minicom.evenexus.core.report.persistence.expressions.Expression;
import nl.minicom.evenexus.core.report.persistence.expressions.Table;
import nl.minicom.evenexus.core.report.persistence.expressions.Value;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

/**
 * This class is responsible for executing the report and drawing it. 
 *
 * @author Michael
 */
public class ReportExecutor {
	
	private final Session session;

	private ReportModel model;
	private Table table;

	/**
	 * This constructor creates a new {@link ReportExecutor} based on a 
	 * provided {@link ReportModel}.
	 * 
	 * @param session	A reference to the {@link Session} connection.
	 */
	@Inject
	public ReportExecutor(Session session) {
		this.session = session;
	}
	
	/**
	 * This method initializes the {@link ReportExecutor}.
	 * 
	 * @param reportModel
	 * 		The {@link ReportModel} to execute.
	 */
	public void initialize(ReportModel reportModel) {
		this.model = reportModel;
		this.table = null;
	}

	/**
	 * @return	The {@link ReportModel} provided to the constructor.
	 */
	public ReportModel getModel() {
		return model;
	}
	
	/**
	 * This method returns a {@link Dataset} object containing the queried
	 * result for an array of specified ReportGroup object values.
	 * 
	 * @param groupValues			The specified values to which the data must hold.
	 * @return						The {@link Dataset} containing data from the query.
	 * @throws HibernateException	Will be thrown if the query is invalid.
	 */
	public Dataset createDataSet(Expression[] groupValues) throws HibernateException {
		ensureReportTableExists();
		
		Select select = new Select(table);
		
		// Add field for grouping		
		if (groupValues.length < model.getReportGroups().size()) {
			ReportGroup group = model.getReportGroups().get(groupValues.length);
			select.addExpression(new Column(group.getKey()), group.getKey());
			select.addGroup(new Column(group.getKey()));
			select.addOrder(new Column(group.getKey()));
		}

		// Filter on selected groupings
		Expression condition = null;
		for (int i = 0; i < groupValues.length; i++) {
			ReportGroup group = model.getReportGroups().get(i);
			Expression groupCondition = new Equals(new Column(group.getKey()), groupValues[i]);
			
			if (condition == null) {
				condition = groupCondition;
			}
			else {
				condition = new And(condition, groupCondition);
			}
		}
		select.setCondition(condition);
		
		// Add report item aliasses
		for (ReportItem item : model.getReportItems()) {
			select.addExpression(item.getAggregate().createExpression(new Column(item.getKey())), item.getKey());
		}
		
		QueryBuilder builder = new QueryBuilder();
		select.writeTranslation(builder);
		
		Dataset dataset = new Dataset();
		SQLQuery statement = builder.createStatement(session);
		
		ScrollableResults results = null;
		try {
			results = statement.scroll();
			String[] aliases = statement.getReturnAliases();
			
			if (results.first()) {
				do {
					String key = results.getString(1);
					for (int i = 0; i <= aliases.length; i++) {
						dataset.setValue(key, i, results.getDouble(i));
					}
				}
				while (results.next());
			}
		}
		catch (HibernateException e) {
			throw e;
		}
		finally {
			results.close();
		}
		
		return dataset;
	}
		
	private void ensureReportTableExists() throws HibernateException {
		if (table == null) {
			table = new Table("report_" + System.currentTimeMillis());
			
			QueryBuilder builder = new QueryBuilder();
			builder.append("CREATE TABLE " + table.getTableName() + " AS (");
			createDataSelect().writeTranslation(builder);
			builder.append(")");
			
			builder.createStatement(session).executeUpdate();
		}
	}
	
	private Select createDataSelect() {
		if (model.getReportItems().isEmpty()) {
			throw new IllegalArgumentException("Cannot create a report with 0 report items.");
		}
		
		// Create all report item queries.
		List<Select> subQueries = new ArrayList<Select>();
		for (ReportItem item : model.getReportItems()) {
			subQueries.add(createReportItemQuery(item));
		}
		
		// Union all subqueries to the first subquery.
		Select parentSubQuery = subQueries.get(0);
		for (int i = 1; i < subQueries.size(); i++) {
			parentSubQuery.addUnion(subQueries.get(i));
		}
		
		// Create group query.
		Select rootQuery = new Select(parentSubQuery, "report_data");
		for (ReportGroup group : model.getReportGroups()) {
			rootQuery.addExpression(new Column(group.getKey()), group.getKey());
			rootQuery.addGroup(new Column(group.getKey()));
		}
		for (ReportItem item : model.getReportItems()) {
			Expression expression = item.getAggregate().createExpression(new Column(item.getKey()));
			rootQuery.addExpression(expression, item.getKey());
		}
				
		return rootQuery;
	}

	private Select createReportItemQuery(ReportItem currentItem) {
		Table tableToReportOn = currentItem.getRequiredTable();
		Select subSelect = new Select(tableToReportOn);
		
		// Add fields for groupings
		for (ReportGroup group : model.getReportGroups()) {
			subSelect.addExpression(group.getExpression(tableToReportOn), group.getKey());
		}
		
		// Add report item aliasses
		for (ReportItem item : model.getReportItems()) {
			subSelect.addExpression(new Value(0), item.getKey());
		}
		
		// Add the actual report item
		subSelect.addExpression(currentItem.getExpression(), currentItem.getKey());
		
		// Add the condition
		subSelect.setCondition(currentItem.getCondition());
		
		return subSelect;
	}
	
}
