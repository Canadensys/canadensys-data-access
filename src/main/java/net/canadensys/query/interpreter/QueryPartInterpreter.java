package net.canadensys.query.interpreter;

import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableField;

import org.hibernate.criterion.Criterion;

/**
 * Generic interface that defines a Query Part Interpreter.
 * Since an Interpreter can be complicated, each interpreter must be able to test if it can handle
 * a SearchableField and a SearchQueryPart.
 * @author canadensys
 *
 */
public interface QueryPartInterpreter {
	
	/**
	 * Test if this Interpreter can handle this SearchableField.
	 * @param searchableField
	 * @return
	 */
	public boolean canHandleSearchableField(SearchableField searchableField);
	
	/**
	 * Test if this Interpreter can handle this SearchQueryPart
	 * @param searchQueryPart
	 * @return
	 */
	public boolean canHandleSearchQueryPart(SearchQueryPart searchQueryPart);
	
	/**
	 * Express SearchQueryPart as Hibernate Criterion
	 * @param searchQueryPart a valid SearchQueryPart see SearchQueryPartValidator
	 * @return Criterion or null if impossible to achieve.
	 */
	public Criterion toCriterion(SearchQueryPart searchQueryPart);
	
	/**
	 * Express SearchQueryPart as SQL. Be aware that this function may allow SQL injection, please
	 * see concrete implementation and use carefully (e.g. read-only user)
	 * @param searchQueryPart  a valid SearchQueryPart see SearchQueryPartValidator
	 * @return sql statement or null if impossible to achieve.
	 */
	public String toSQL(SearchQueryPart searchQueryPart);
}
