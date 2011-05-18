package nl.minicom.evenexus.core.report.persistence.expressions;

import java.util.Date;

import nl.minicom.evenexus.core.report.persistence.QueryBuilder;

/**
 * <p>This is a special class of {@link Expression}. This class given
 * a value, will translate to that value in a SQL-query.</p>
 * 
 * <p>This class is Immutable and thus thread-safe!</p>
 * 
 * @author Michael
 */
public class Value extends Expression {

	public static final Expression NULL = new Value();
	
	private final Object value;

	/**
	 * This constructor creates a {@link Value} object.
	 *  
	 * @param value		The boolean value.
	 */
	public Value(boolean value) {
		this.value = value;
	}

	/**
	 * This constructor creates a {@link Value} object.
	 *  
	 * @param value		The int value.
	 */
	public Value(int value) {
		this.value = value;
	}

	/**
	 * This constructor creates a {@link Value} object.
	 *  
	 * @param value		The long value.
	 */
	public Value(long value) {
		this.value = value;
	}

	/**
	 * This constructor creates a {@link Value} object.
	 *  
	 * @param value		The {@link Date} value.
	 */
	public Value(Date value) {
		this.value = value;
	}

	/**
	 * This constructor creates a {@link Value} object.
	 *  
	 * @param value		The {@link String} value.
	 */
	public Value(String value) {
		this.value = "'" + value + "'";
	}

	/**
	 * This constructor creates a {@link Value} object.
	 *  
	 * @param value		The {@link Object} value.
	 */
	public Value(Object value) {
		this.value = value;
	}

	/**
	 * This constructor creates a {@link Value} object with the
	 * value NULL.
	 */
	public Value() {
		this.value = null;
	}

	@Override
	public void writeTranslation(QueryBuilder query) {
		if (value == null) {
			query.append("NULL");
			return;
		}
		query.append(String.valueOf(value));
	}

}
