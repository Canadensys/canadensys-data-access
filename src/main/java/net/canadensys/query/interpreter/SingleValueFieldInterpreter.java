package net.canadensys.query.interpreter;

import net.canadensys.databaseutils.SQLHelper;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableField;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * Interprets a SearchQueryPart on a single database field.
 * @author canadensys
 *
 */
public class SingleValueFieldInterpreter implements QueryPartInterpreter{
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(SingleValueFieldInterpreter.class);

	@Override
	public boolean canHandleSearchQueryPart(SearchQueryPart searchQueryPart) {
		SearchableField searchableField = searchQueryPart.getSearchableField();
		return (searchableField.getRelatedFields() != null && 
				searchableField.getRelatedFields().size() == 1);
	}
	
	@Override
	public Criterion toCriterion(SearchQueryPart searchQueryPart) {
		
		if(!canHandleSearchQueryPart(searchQueryPart)){
			LOGGER.error("Can't handle QueryPart : " + searchQueryPart);
			return null;
		}
		
		SearchableField searchableField = searchQueryPart.getSearchableField();
		Object singleParsedValue = searchQueryPart.getParsedValue(searchQueryPart.getSingleValue(), searchableField.getRelatedField());
		switch(searchQueryPart.getOp()){
			case EQ : return Restrictions.eq(searchableField.getRelatedField(), singleParsedValue);
			case NEQ : return Restrictions.ne(searchableField.getRelatedField(), singleParsedValue);
			case SLIKE : return Restrictions.ilike(searchableField.getRelatedField(), searchQueryPart.getSingleValue(), MatchMode.START);
			case ELIKE : return Restrictions.ilike(searchableField.getRelatedField(), searchQueryPart.getSingleValue(), MatchMode.END);
			case CLIKE : return Restrictions.ilike(searchableField.getRelatedField(), searchQueryPart.getSingleValue(), MatchMode.ANYWHERE);
			case IN : return Restrictions.in(searchableField.getRelatedField(), searchQueryPart.getValueList());
		}
		return null;
	}

	@Override
	public String toSQL(SearchQueryPart searchQueryPart) {
		if(!canHandleSearchQueryPart(searchQueryPart)){
			LOGGER.warn("Can't handle QueryPart : " + searchQueryPart);
			return null;
		}
		
		return convertIntoSQL(searchQueryPart);
	}
	
	
	/**
	 * Convert a SearchQueryPart into a SQL fragment.
	 * @param queryPart a valid SearchQueryPart object
	 * @return SQL fragment representing this SearchQueryPart
	 */
	public String convertIntoSQL(SearchQueryPart queryPart){
		//this function is not able to convert composed fields
		if(queryPart.getFieldList() == null || queryPart.getFieldList().size() > 1){
			return null;
		}

		SearchableField searchableField = queryPart.getSearchableField();
		String value = "";
		if(Number.class.isAssignableFrom(searchableField.getType())){
			value = StringUtils.join(queryPart.getValueList(),",");
		}
		else{
			for(String currValue : queryPart.getValueList()){
				value += "'" + SQLHelper.escapeSQLString(currValue) + "',";
			}
			value = StringUtils.removeEnd(value, ",");
		}
		
		switch(queryPart.getOp()){
			case EQ : return searchableField.getRelatedField()+"="+value;
			case NEQ : return searchableField.getRelatedField()+"<>"+value;
			case SLIKE : return searchableField.getRelatedField()+" ILIKE '"+SQLHelper.escapeSQLString(queryPart.getSingleValue())+"%'";
			case ELIKE : return searchableField.getRelatedField()+" ILIKE '%"+SQLHelper.escapeSQLString(queryPart.getSingleValue())+"'";
			case CLIKE : return searchableField.getRelatedField()+" ILIKE '%"+SQLHelper.escapeSQLString(queryPart.getSingleValue())+"%'";
			case IN : return searchableField.getRelatedField()+" IN (" + value + ")";
		}
		return null;
	}

}
