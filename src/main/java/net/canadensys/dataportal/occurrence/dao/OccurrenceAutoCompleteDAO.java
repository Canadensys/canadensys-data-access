package net.canadensys.dataportal.occurrence.dao;

import java.util.List;

import net.canadensys.model.SuggestedValue;

/**
 * Interface to add a AutoComplete feature to some occurrence data.
 * @author canadensys
 *
 */
public interface OccurrenceAutoCompleteDAO {
	
	/**
	 * Returns suggestions as SuggestedValue list for a field and a current value.
	 * @param field
	 * @param currValue
	 * @param useSanitizedValue compare with sanitized value instead of real value
	 * @return result SuggestedValue list
	 */
	public List<SuggestedValue> getSuggestionsFor(String field, String currValue, boolean useSanitizedValue);
	
	/**
	 * Returns all possible values as SuggestedValue list.
	 * @param field
	 * @return SuggestedValue list
	 */
	public List<SuggestedValue> getAllPossibleValues(String field);

}
