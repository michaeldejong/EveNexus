package nl.minicom.evenexus.core.report.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.definition.components.ReportGroup.Type;
import nl.minicom.evenexus.core.report.definition.components.ReportItem;
import nl.minicom.evenexus.core.report.persistence.QueryBuilder;
import nl.minicom.evenexus.core.report.persistence.Select;
import nl.minicom.evenexus.core.report.persistence.expressions.And;
import nl.minicom.evenexus.core.report.persistence.expressions.Column;
import nl.minicom.evenexus.core.report.persistence.expressions.Equals;
import nl.minicom.evenexus.core.report.persistence.expressions.Expression;
import nl.minicom.evenexus.core.report.persistence.expressions.Table;
import nl.minicom.evenexus.core.report.persistence.expressions.Value;
import nl.minicom.evenexus.persistence.Database;
import nl.minicom.evenexus.persistence.interceptor.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * This class is responsible for executing the report and drawing it. 
 *
 * @author Michael
 */
public class ReportExecutor {
	
	private final Database database;

	private ReportModel model;
	private Table table;

	/**
	 * This constructor creates a new {@link ReportExecutor} based on a 
	 * provided {@link ReportModel}.
	 * 
	 * @param database	A reference to the {@link Database} connection.
	 */
	@Inject
	public ReportExecutor(Database database) {
		this.database = database;
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
	 * This method deletes the current report from the database.
	 */
	@Transactional
	public void deleteReport() {
		if (table != null) {
			QueryBuilder builder = new QueryBuilder();
			builder.append("DROP TABLE IF EXISTS " + table.getTableName());
			builder.createStatement(database.getCurrentSession()).executeUpdate();
		}
	}
	
	/**
	 * This method returns a {@link Dataset} object containing the queried
	 * result for an array of specified ReportGroup object values.
	 * 
	 * @param groupValues			The specified values to which the data must hold.
	 * @return						The {@link Dataset} containing data from the query.
	 * @throws HibernateException	Will be thrown if the query is invalid.
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Dataset createDataSet(Expression... groupValues) throws HibernateException {
		ensureReportTableExists();
		
		Select select = new Select(table);
		
		// Add field for grouping		
		ReportGroup currentGroup = model.getReportGroups().get(groupValues.length);
		if (groupValues.length < model.getReportGroups().size()) {
			select.addExpression(new Column(currentGroup.getKey()), currentGroup.getKey());
			select.addGroup(new Column(currentGroup.getKey()));
			select.addOrder(new Column(currentGroup.getKey()));
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
		List<String> aliases = Lists.newArrayList();
		for (ReportItem item : model.getReportItems()) {
			select.addExpression(item.getAggregate().createExpression(new Column(item.getKey())), item.getKey());
			aliases.add(item.getKey());
		}
		
		QueryBuilder builder = new QueryBuilder();
		select.writeTranslation(builder);
		
		Dataset dataset = createDataset(currentGroup);
		SQLQuery statement = builder.createStatement(database.getCurrentSession());
		
		List<Object[]> results = null;
		try {
			results = statement.list();
			for (Object[] row : results) {
				String key = row[0].toString();
				for (int i = 0; i < aliases.size(); i++) {
					BigDecimal value = (BigDecimal) row[i + 1];
					String groupValue = currentGroup.translate(key);
					dataset.setValue(aliases.get(i), groupValue, value);
				}
			}
		}
		catch (HibernateException e) {
			throw e;
		}
		
		return dataset;
	}

	private Dataset createDataset(ReportGroup currentGroup) {
		if (Type.DAY == currentGroup.getType()) {
			Date start = model.getStartDate().getValue();
			Date end = model.getEndDate().getValue();
			
			Calendar c = Calendar.getInstance();
			c.setTime(start);
			
			Set<String> groupKeys = Sets.newHashSet();
			groupKeys.add(c.get(Calendar.YEAR) + "-" + c.get(Calendar.DAY_OF_YEAR));
			while (c.getTime().before(end)) {
				groupKeys.add(c.get(Calendar.YEAR) + "-" + c.get(Calendar.DAY_OF_YEAR));
				c.add(Calendar.DAY_OF_YEAR, 1);
			}
			
			return new Dataset(groupKeys, false);
		}
		
		return new Dataset(true);
	}
	
	/**
	 * This method ensures that the table we need to write the results to, is present in the database.
	 * 
	 * @throws HibernateException
	 * 		If something went wrong.
	 */
	@Transactional
	void ensureReportTableExists() throws HibernateException {
		if (table == null) {
			table = new Table("report_" + System.currentTimeMillis());
			
			QueryBuilder builder = new QueryBuilder();
			builder.append("CREATE TABLE " + table.getTableName() + " AS (");
			createDataSelect().writeTranslation(builder);
			builder.append(")");
			
			builder.createStatement(database.getCurrentSession()).executeUpdate();
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
		if (currentItem.getCondition() != null) {
			subSelect.setCondition(currentItem.getCondition());
		}
		
		return subSelect;
	}
	
}
