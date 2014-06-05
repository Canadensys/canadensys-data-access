package net.canadensys.query.interpreter;

import java.util.List;

import net.canadensys.databaseutils.PostgisUtils;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableField;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Interprets a SearchQueryPart representing a geospatial query where we target a point, with a radius. 
 * @author canadensys
 *
 */
public class WithinRadiusFieldInterpreter implements QueryPartInterpreter{
	
	public static final int POINT_IDX = 0;
	public static final int RADIUS_IDX = 1;

	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(WithinRadiusFieldInterpreter.class);
	
	/**
	 * Needs exactly one related field and type can not be specified.
	 */
	@Override
	public boolean canHandleSearchableField(SearchableField searchableField) {
		return (searchableField.getRelatedFields() != null && 
				searchableField.getRelatedFields().size() == 1 &&
				searchableField.getType() == null);
	}
	
	@Override
	public boolean canHandleSearchQueryPart(SearchQueryPart searchQueryPart) {
		
		if(!canHandleSearchableField(searchQueryPart.getSearchableField()) || 
			!QueryOperatorEnum.IN.equals(searchQueryPart.getOp())){
			return false;
		}
		
		//validate that the parsedValue are in the right type(class)
		List<String> valueList = searchQueryPart.getValueList();
		if(valueList.size() != 2){
			return false;
		}
		
		//check that we have a coordinate as Pair and a radius as Integer.
		Object parsedValue = null;
		parsedValue = searchQueryPart.getParsedValue(valueList.get(POINT_IDX));

		if(!(parsedValue instanceof Pair)){
			return false;
		}
		Pair<?,?> pv = (Pair<?,?>)parsedValue;
		Object leftValue = pv.getLeft();
		Object rightValue = pv.getRight();
			
		//isNumber is not perfect but will catch most of the possible issue
		if(!String.class.equals(leftValue.getClass()) || 
				!NumberUtils.isNumber((String)leftValue)){
			return false;
		}
		if(!String.class.equals(rightValue.getClass()) || 
				!NumberUtils.isNumber((String)rightValue)){
			return false;
		}

		parsedValue = searchQueryPart.getParsedValue(valueList.get(RADIUS_IDX));
		if(!Integer.class.equals(parsedValue.getClass())){
			return false;
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
		
		List<String> valueList = searchQueryPart.getValueList();
		
		Object parsedValue = null;
		parsedValue = searchQueryPart.getParsedValue(valueList.get(POINT_IDX));
		Pair<String,String> coord = (Pair<String,String>)parsedValue;
		parsedValue = searchQueryPart.getParsedValue(valueList.get(RADIUS_IDX));
		Integer radius = (Integer)parsedValue;
		SearchableField searchableField = searchQueryPart.getSearchableField();
		String geomColumn = searchableField.getRelatedField();
		return PostgisUtils.getFromWithinRadius(geomColumn, coord.getLeft(), coord.getRight(), radius);
	}
}
