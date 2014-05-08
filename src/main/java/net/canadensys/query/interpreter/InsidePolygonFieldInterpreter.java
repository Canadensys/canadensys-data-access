package net.canadensys.query.interpreter;

import java.util.ArrayList;
import java.util.List;

import net.canadensys.databaseutils.PostgisUtils;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableField;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Interprets a SearchQueryPart representing a geospatial query.
 * @author canadensys
 *
 */
public class InsidePolygonFieldInterpreter implements QueryPartInterpreter{
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(InsidePolygonFieldInterpreter.class);
	
	@Override
	public boolean canHandleSearchableField(SearchableField searchableField) {
		return (searchableField.getRelatedFields() != null && 
				searchableField.getRelatedFields().size() == 1);
	}
	
	@Override
	public boolean canHandleSearchQueryPart(SearchQueryPart searchQueryPart) {
		
		if(!canHandleSearchableField(searchQueryPart.getSearchableField()) || 
			!QueryOperatorEnum.IN.equals(searchQueryPart.getOp())){
			return false;
		}
		
		//validate that the parsedValue are in the right type(class)
		List<String> valueList = searchQueryPart.getValueList();
		Object parsedValue = null;
		for(String currValue : valueList){
			parsedValue = searchQueryPart.getParsedValue(currValue);
			if(Pair.class.equals(parsedValue.getClass())){
				Pair<?,?> a = (Pair<?,?>)parsedValue;
				if(!String.class.equals(a.getLeft().getClass()) ||
						!String.class.equals(a.getRight())){
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public Criterion toCriterion(SearchQueryPart searchQueryPart) {
		String sqlCmd = toSQL(searchQueryPart);
		if(sqlCmd == null){
			return null;
		}
		return Restrictions.sqlRestriction(sqlCmd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toSQL(SearchQueryPart searchQueryPart) {
		
		if(!canHandleSearchQueryPart(searchQueryPart)){
			LOGGER.error("Can't handle QueryPart : " + searchQueryPart);
			return null;
		}	
		
		List<Pair<String,String>> polygon = new ArrayList<Pair<String,String>>();
		SearchableField searchableField = searchQueryPart.getSearchableField();
		
		List<String> valueList = searchQueryPart.getValueList();
		Object parsedValue = null;
		for(String currValue : valueList){
			parsedValue = searchQueryPart.getParsedValue(currValue);
			polygon.add((Pair<String,String>)parsedValue);
		}
		String geomColumn = searchableField.getRelatedField();
		return PostgisUtils.getInsidePolygonSQLClause(geomColumn, polygon);
	}
}
