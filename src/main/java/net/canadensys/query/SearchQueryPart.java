package net.canadensys.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SearchQueryPart represents a part of a search query that is not tied to a specific technology.
 * The main usage is to translate a search query received via URL parameters.
 * Value represents a value received from the user (raw text)
 * SearchableValue represents a value(or values) extracted from the user's value. In some cases, one user's
 * value is mapped to multiple SearchabelValue.
 * @author canadensys
 *
 */
public class SearchQueryPart {

	private SearchableField searchableField = null;
	private QueryOperatorEnum op = null;
	
	private int autoKey = 0;
	private List<Integer> usedKey = new ArrayList<Integer>();
	
	private TreeMap<Integer,String> valuesTree = null;
	private Map<String,Map<String,Object>> parsedValuesMap = null;
	private Map<String,Object> hints = null;
	
	/**
	 * Default constructor
	 */
	public SearchQueryPart(){
		valuesTree = new TreeMap<Integer,String>();
		parsedValuesMap = new HashMap<String,Map<String, Object>>();
	}
	
	/**
	 * Copy constructor
	 * Creates a new SearchQueryPart based on the provided SearchQueryPart.
	 * All the content of the provided object is copied except the parsed Object in parsedValuesMap
	 * since we don't know what it is so it must be immutable objects (even if we can not enforce it).
	 * @param toCopy
	 */
	public SearchQueryPart(SearchQueryPart toCopy){
		this();
		this.searchableField = toCopy.searchableField;
		this.op = toCopy.op;
		this.autoKey = toCopy.autoKey;
		this.usedKey.addAll(toCopy.usedKey);
		this.valuesTree.putAll(toCopy.valuesTree);
		Map<String,Object> values;
		for(String key: parsedValuesMap.keySet()){
			values = new HashMap<String, Object>();
			for(String parsedKey : parsedValuesMap.get(key).keySet()){
				values.put(parsedKey, parsedValuesMap.get(key).get(parsedKey));
			}
			this.parsedValuesMap.put(key, values);
		}
	}
	
	public void setSearchableField(SearchableField searchableField) {
		this.searchableField = searchableField;
	}
	public SearchableField getSearchableField() {
		return searchableField;
	}
	
	public Integer getSearchableFieldId() {
		if(searchableField == null){
			return null;
		}
		return searchableField.getSearchableFieldId();
	}
	public String getSearchableFieldName() {
		if(searchableField == null){
			return null;
		}
		return searchableField.getSearchableFieldName();
	}

	public void setOp(QueryOperatorEnum op) {
		this.op = op;
	}
	public QueryOperatorEnum getOp() {
		return op;
	}
	
	/**
	 * Adds a value at the next available index.
	 * @param value
	 */
	public void addValue(String value){
		addValue(value,null);
	}
	
	/**
	 * Add hint that could help the interpreter to be more efficient.
	 * See the matching Interpreter for available hint keys.
	 * @param value
	 */
	public void addHint(String hintKey, Object value){
		if(hints == null){
			hints = new HashMap<String, Object>();
		}
		hints.put(hintKey, value);
	}
	
	/**
	 * Get hint previously set on this SearchQueryPart.
	 * A hint is set to help the interpreter to be more efficient.
	 * @param hintKey
	 * @return hint as object or null is no hint is associated with the provided key
	 */
	public Object getHint(String hintKey){
		if(hints == null){
			return null;
		}
		return hints.get(hintKey);
	}
	
	/**
	 * Clear all the values from the SearchQueryPart
	 */
	public void clearValues(){
		valuesTree.clear();
		autoKey = 0;
		usedKey.clear();
	}

	/**
	 * Adds a value to the SearchQueryPart at a specific index.
	 * If an element was already at this index, this latter will be relocated at the next available index.
	 * If no index is specified, the value will be put at the next available index.
	 * @param value
	 * @param idx optional
	 */
	public void addValue(String value, Integer idx){
		if(idx == null){
			//find a free slot
			while(usedKey.contains(autoKey)){
				autoKey++;
			}
			usedKey.add(autoKey);
			valuesTree.put(autoKey, value);
		}
		else{
			usedKey.add(idx);
			String previousValue = valuesTree.put(idx, value);
			if(previousValue != null){
				//relocate the previous value
				addValue(previousValue);
			}
		}
	}
	
	/**
	 * Get the values as ordered list.
	 * @return the value list
	 */
	public List<String> getValueList(){		
		return new ArrayList<String>(valuesTree.values());
	}
	
	/**
	 * @return first field of the fields list or null if no SearchableField is set.
	 */
	public String getSingleField(){
		if(searchableField == null){
			return null;
		}
		if(searchableField.getRelatedFields().size() == 1){
			return searchableField.getRelatedFields().get(0);
		}
		return null;
	}
	
	/**
	 * Get the fields list
	 * @return the fields list or null if no SearchableField is set.
	 */
	public List<String> getFieldList(){
		if(searchableField == null){
			return null;
		}
		return searchableField.getRelatedFields();
	}
	
	/**
	 * @return first value of the value list or null if the list is empty
	 */
	public String getSingleValue(){		
		if(valuesTree.values().size() >= 1){
			return valuesTree.values().iterator().next();
		}
		return null;
	}
	
	/**
	 * Adds a parsed value for a SearchableField.
	 * To know how to store your parsed value, check the proper Interpreter.
	 * A single value in the valuesTree can lead to multiple values in the parsedValuesMap.
	 * @param value raw value of this search
	 * @param searchableFieldKey name of the SearchableField
	 * @param parsedValue should be immutable object (Integer, String, ...)
	 * @return the value was successfully added
	 */
	public boolean addParsedValue(String value, String searchableFieldKey, Object parsedValue){
		if(searchableField == null){
			return false;
		}
		if(searchableField.getRelatedFields().contains(searchableFieldKey)){
			if(parsedValuesMap.get(value) == null){
				parsedValuesMap.put(value, new HashMap<String, Object>());
			}
			parsedValuesMap.get(value).put(searchableFieldKey, parsedValue);
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the parsed value. Note that this is not a parser, it will only returns the already
	 * parsed value.
	 * @param value user's (raw) value
	 * @param searchableFieldKey SearchableField name
	 * @return
	 */
	public Object getParsedValue(String value, String searchableFieldKey){
		if(parsedValuesMap.get(value) == null){
			return null;
		}
		return parsedValuesMap.get(value).get(searchableFieldKey);
	}
	
	/**
	 * @see getParsedValue(String, String)
	 * Returns the parsed value using the 'searchableField.relatedField' as searchableFieldKey.
	 * Do not use this function on SearchableField with more than 1 relatedField.
	 * @param value
	 * @return the parsed value or null if the value could not be found.
	 */
	public Object getParsedValue(String value){
		if(searchableField.getRelatedFields().size() == 1){
			return parsedValuesMap.get(value).get(searchableField.getRelatedField());
		}
		return null;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("fieldId:"+getSearchableFieldId()).append(",");
		sb.append("fieldName:"+getSearchableFieldName()).append(",");
		sb.append("op:"+op).append(",");
		sb.append("value list:"+getValueList());
		return sb.toString();
	}
}
