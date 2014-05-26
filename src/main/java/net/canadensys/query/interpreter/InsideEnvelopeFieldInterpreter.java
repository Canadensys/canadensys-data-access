package net.canadensys.query.interpreter;

import java.util.ArrayList;
import java.util.List;

import net.canadensys.databaseutils.PostgisUtils;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableField;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

/**
 * Interprets a SearchQueryPart representing a geospatial query where we target an
 * envelope. Envelope is defined by 2 coordinates: minimum and maximum lat,lng 
 * @author canadensys
 *
 */
public class InsideEnvelopeFieldInterpreter extends InsidePolygonFieldInterpreter{
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(InsideEnvelopeFieldInterpreter.class);
	
	/**
	 * Check if we have exactly 2 elements in value list (envelope is defined by 2 points)
	 * @param searchQueryPart
	 * @return
	 */
	@Override
	public boolean canHandleSearchQueryPart(SearchQueryPart searchQueryPart) {
		if(!super.canHandleSearchQueryPart(searchQueryPart)){
			return false;
		}
		List<String> valueList = searchQueryPart.getValueList();
		return (valueList.size() == 2);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toSQL(SearchQueryPart searchQueryPart) {
		
		if(!canHandleSearchQueryPart(searchQueryPart)){
			LOGGER.error("Can't handle QueryPart : " + searchQueryPart);
			return null;
		}	
		
		List<Pair<String,String>> envelope = new ArrayList<Pair<String,String>>();
		SearchableField searchableField = searchQueryPart.getSearchableField();
		
		List<String> valueList = searchQueryPart.getValueList();
		Object parsedValue = null;

		//North East point
		parsedValue = searchQueryPart.getParsedValue(valueList.get(0));
		envelope.add((Pair<String,String>)parsedValue);

		//South West point
		parsedValue = searchQueryPart.getParsedValue(valueList.get(1));
		envelope.add((Pair<String,String>)parsedValue);

		String geomColumn = searchableField.getRelatedField();
		return PostgisUtils.getInsideEnvelopeSQLClause(geomColumn, envelope, true);
	}
}
