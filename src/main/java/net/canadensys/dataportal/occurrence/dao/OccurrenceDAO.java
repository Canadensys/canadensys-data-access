package net.canadensys.dataportal.occurrence.dao;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.query.LimitedResult;
import net.canadensys.query.SearchQueryPart;

/**
 * Interface for accessing occurrence data.
 * Because OccurrenceModel is an expensive Object to create, the OccurrenceDAO 
 * is offering some specific methods for searching.
 * @author canadensys
 *
 */
public interface OccurrenceDAO {
	
	public static final int DEFAULT_LIMIT = 200;
	public static final int DEFAULT_FLUSH_LIMIT = 1000;
	
	/**
	 * Load a specific Occurrence into an OccurrenceModel
	 * @param auto_id
	 * @return
	 */
	public OccurrenceModel load(int auto_id);
	
	/**
	 * Load a specific Occurrence into an OccurrenceModel
	 * @param sourceFileId
	 * @param occurrenceId
	 * @param deepLoad Do we need to load the raw model?
	 * @return
	 */
	public OccurrenceModel load(String sourceFileId, String occurrenceId, boolean deepLoad);
	
	/**
	 * Search based on the properties set in the searchCriteria
	 * @param searchCriteria
	 * @param limit (optional)
	 * @return
	 */
	public List<OccurrenceModel> search(OccurrenceModel searchCriteria, Integer limit);
	
	public List<OccurrenceModel> search(HashMap<String, List<String>> searchCriteria);
	
	
	
	/**
	 * Search the occurrences without any limit. The contract of this method is to return
	 * an iterator and move it internally to make sure all the OccurrenceModel can be load
	 * without OutOfMemoryException. This function will automatically close the internal source
	 * on the last element.
	 * @param searchCriteria
	 * @return
	 */
	public Iterator<OccurrenceModel> searchIterator(Map<String,List<SearchQueryPart>> searchCriteria);
	
	/**
	 * Same as searchIterator(...) but the rawModel will be loaded (not lazy loaded) inside
	 * the OccurrenceModel.
	 * @param searchCriteria
	 * @param orderBy (optional)
	 * @return
	 */
	public Iterator<OccurrenceModel> searchIteratorRaw(Map<String,List<SearchQueryPart>> searchCriteria, String orderBy);
	
	/**
	 * Search the occurrences with a limit (default 200).
	 * @param searchCriteria map of criteria as list of SearchQueryPart mapped by field name
	 * @param columnList list of columns to include in the result
	 * @return LimitedResult with rows as list of field:value map
	 */
	public LimitedResult<List<Map<String, String>>> searchWithLimit(Map<String,List<SearchQueryPart>> searchCriteria, List<String> columnList);
	

	/**
	 * Returns the count of all occurrences available for those criteria 
	 * @param searchCriteria
	 * @return
	 */
	public int getOccurrenceCount(Map<String,List<SearchQueryPart>> searchCriteria);
	
	/**
	 * Returns the different values and their count for this column and those criteria.
	 * Ordered by count (descending)
	 * @param searchCriteriaMap
	 * @param column
	 * @param max optional
	 * @return
	 */
	public List<AbstractMap.SimpleImmutableEntry<String,Integer>> getValueCount(Map<String, List<SearchQueryPart>> searchCriteriaMap, String column, Integer max);
	
	/**
	 * Returns the count of distinct value for this column with those criteria.
	 * Null values will not be included.
	 * @param searchCriteriaMap
	 * @param column
	 * @return
	 */
	public Integer getCountDistinct(Map<String,List<SearchQueryPart>> searchCriteriaMap, String column);
	
	/**
	 * Returns the distinct InstitutionCode for those criteria.
	 * @param searchCriteria
	 * @return
	 */
	public List<String> getDistinctInstitutionCode(Map<String,List<SearchQueryPart>> searchCriteria);
	
	/**
	 * Get a summary (only some columns) of an occurrence as Json string.
	 * Normally, a DOA should not format results but, for performance reasons we will tolerate this.
	 * We want to avoid Object[]->Model->JSON when we can put the Object[] into JSON directly.
	 * It's also easier to manage the column list and avoid the instantiation of unwanted column.
	 * A good tradeoff could be to return result in a map.
	 * @param auto_id Occurrence unique id
	 * @param columnList list of columns to include in the result
	 * @return a summary (only the columns from columnList) as Json string
	 */
	public String getOccurrenceSummaryJson(int auto_id, String idColumnName, List<String> columnList);
}
