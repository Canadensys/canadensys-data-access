package net.canadensys.dataportal.occurrence.dao;

import java.util.List;

import net.canadensys.dataportal.occurrence.model.UniqueValuesModel;

/**
 * Interface to add a AutoComplete feature to some occurrence data.
 * @author canadensys
 *
 */
public interface OccurrenceAutoCompleteDAO {
	
	/**
	 * Returns suggestions as JSON string for a field and a current value.
	 * @param field
	 * @param currValue
	 * @param useSanitizedValue compare with sanitized value instead of real value
	 * @return result as JSON string
	 */
	public String getSuggestionsFor(String field, String currValue, boolean useSanitizedValue);
	
	/**
	 * Returns all possible values as a list of UniqueValuesModel
	 * @param field
	 * @return
	 */
	public List<UniqueValuesModel> getAllPossibleValues(String field);

}
