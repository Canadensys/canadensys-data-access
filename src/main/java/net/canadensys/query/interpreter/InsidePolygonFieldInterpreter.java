package net.canadensys.query.interpreter;

import java.util.ArrayList;
import java.util.List;

import net.canadensys.databaseutils.PostgisUtils;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableField;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Interprets a SearchQueryPart representing a geospatial query where we target a polygon.
 * If the polygon is crossing the IDL, make sure to add the hint IS_CROSSING_IDL_HINT so the 
 * interpreter can use the shifted_geom column.
 * @author canadensys
 *
 */
public class InsidePolygonFieldInterpreter implements QueryPartInterpreter{
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(InsidePolygonFieldInterpreter.class);
	
	public static final String IS_CROSSING_IDL_HINT = "isCrossingIDL";
	public static final int GEOM_FIELD_IDX = 0;
	public static final int SHIFTED_GEOM_FIELD_IDX = 1;
	
	/**
	 * Details: Needs at least one related field and type should not be specified.
	 */
	@Override
	public boolean canHandleSearchableField(SearchableField searchableField) {
		return (searchableField.getRelatedFields() != null && 
				searchableField.getRelatedFields().size() >= 1 &&
				searchableField.getType() == null);
	}
	
	/**
	 * Details: SearchQueryPart must contains at least 4 elements in its value list,
	 * only IN operator is accepted.
	 */
	@Override
	public boolean canHandleSearchQueryPart(SearchQueryPart searchQueryPart) {
		
		if(!canHandleSearchableField(searchQueryPart.getSearchableField()) || 
			!QueryOperatorEnum.IN.equals(searchQueryPart.getOp()) || 
			searchQueryPart.getValueList().size() < 4){
			LOGGER.debug("Can NOT handle SearchQueryPart.");
			return false;
		}
		
		//get the parsed value of the first SearchableField only (e.g. the_geom)
		String searchableFieldKey = searchQueryPart.getSearchableField().getRelatedFields().get(GEOM_FIELD_IDX);
		return validateParsedValue(searchQueryPart,searchableFieldKey);
	}
	
	/**
	 * Validate that the parsedValues are in the right type(class).
	 * @param searchQueryPart
	 * @param searchableFieldKey
	 * @return
	 */
	protected boolean validateParsedValue(SearchQueryPart searchQueryPart, String searchableFieldKey){
		List<String> valueList = searchQueryPart.getValueList();
		
		Object parsedValue = null;
		for(String currValue : valueList){
			parsedValue = searchQueryPart.getParsedValue(currValue, searchableFieldKey);
			if(parsedValue instanceof Pair){
				Pair<?,?> a = (Pair<?,?>)parsedValue;
				if(!String.class.equals(a.getLeft().getClass()) ||
						!String.class.equals(a.getRight().getClass())){
					return false;
				}
			}
			else{
				return false;
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
		String geomColumn = searchableField.getRelatedFields().get(GEOM_FIELD_IDX);
		List<String> valueList = searchQueryPart.getValueList();
		
		Object parsedValue = null;
		for(String currValue : valueList){
			parsedValue = searchQueryPart.getParsedValue(currValue,geomColumn);
			polygon.add((Pair<String,String>)parsedValue);
		}
		
		//If the polygon is crossing the IDL, we use the shiftedGeomColumn.
		if(BooleanUtils.toBoolean((Boolean)searchQueryPart.getHint(IS_CROSSING_IDL_HINT))){
			String shiftedGeomColumn = searchableField.getRelatedFields().get(SHIFTED_GEOM_FIELD_IDX);
			return PostgisUtils.getInsidePolygonSQLClause(shiftedGeomColumn, polygon, true);
		}
		return PostgisUtils.getInsidePolygonSQLClause(geomColumn, polygon, false);
	}
}
