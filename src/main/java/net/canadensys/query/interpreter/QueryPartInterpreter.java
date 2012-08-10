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
	 * @param searchQueryPart a valid SearchQueryPart see SearchQueryPartValidator
	 * @return
	 */
	public Criterion toCriterion(SearchQueryPart searchQueryPart);
	public String toSQL(SearchQueryPart searchQueryPart);
}
