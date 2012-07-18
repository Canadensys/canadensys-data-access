package net.canadensys.query;

import java.util.List;

/**
 * Interface of a SearchableField. The implementation is application specific.
 * @author canadensys
 *
 */
public interface SearchableField {
	public int getSearchableFieldId();
	public String getSearchableFieldName();
	
	public SearchableFieldTypeEnum getSearchableFieldTypeEnum();
	/**
	 * Type of data needed to create the query
	 * @return
	 */
	public Class<?> getType();
	public List<QueryOperatorEnum> getSupportedOperator();
	public List<String> getRelatedFields();
	public String getRelatedField();
}
