package net.canadensys.query.interpreter;

import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableFieldTypeEnum;

/**
 * This class is a Resolver to translate a SearchableFieldTypeEnum value into the proper
 * QueryPartInterpreter.
 * @author canadensys
 *
 */
public class QueryPartInterpreterResolver {
	
	//use the same objects for everyone, they are thread safe
	private static final QueryPartInterpreter SINGLE_VALUE_INTERPRETER = new SingleValueFieldInterpreter();
	private static final QueryPartInterpreter MIN_MAX_NUMBER_INTERPRETER = new MinMaxNumberFieldInterpreter();
	private static final QueryPartInterpreter START_END_DATE_INTERPRETER = new StartEndDateFieldInterpreter();
	private static final QueryPartInterpreter INSIDE_ENVELOPE_INTERPRETER = new InsideEnvelopeFieldInterpreter();
	private static final QueryPartInterpreter INSIDE_POLYGON_INTERPRETER = new InsidePolygonFieldInterpreter();
	private static final QueryPartInterpreter WITHIN_RADIUS_INTERPRETER = new WithinRadiusFieldInterpreter();
		
	public static QueryPartInterpreter getQueryPartInterpreter(SearchableFieldTypeEnum searchableFieldTypeEnum){
		switch(searchableFieldTypeEnum){
			case SINGLE_VALUE : return SINGLE_VALUE_INTERPRETER;
			case MIN_MAX_NUMBER : return MIN_MAX_NUMBER_INTERPRETER;
			case START_END_DATE : return START_END_DATE_INTERPRETER;
			case INSIDE_ENVELOPE_GEO : return INSIDE_ENVELOPE_INTERPRETER;
			case INSIDE_POLYGON_GEO : return INSIDE_POLYGON_INTERPRETER;
			case WITHIN_RADIUS_GEO : return WITHIN_RADIUS_INTERPRETER;
			default : return null;
		}
	}
	
	public static QueryPartInterpreter getQueryPartInterpreter(SearchQueryPart queryPart){
		return getQueryPartInterpreter(queryPart.getSearchableField().getSearchableFieldTypeEnum());
	}
}
