package net.canadensys.query.interpreter;

import net.canadensys.query.SearchQueryPart;

import org.hibernate.criterion.Criterion;

/**
 * Generic interface  that defines a command in the Query Part Interpreter.
 * @author canadensys
 *
 */
public interface QueryPartInterpreter {
	
	public boolean canHandleSearchQueryPart(SearchQueryPart searchQueryPart);
	
	/**
	 * @param searchQueryPart a valid SearchQueryPart see SearchQueryPartValidator
	 * @return
	 */
	public Criterion toCriterion(SearchQueryPart searchQueryPart);
	public String toSQL(SearchQueryPart searchQueryPart);
}
