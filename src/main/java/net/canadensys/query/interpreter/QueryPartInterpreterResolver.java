package net.canadensys.query.interpreter;

import net.canadensys.query.SearchQueryPart;

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
	
	public static QueryPartInterpreter getQueryPartInterpreter(SearchQueryPart queryPart){
		switch(queryPart.getSearchableField().getSearchableFieldTypeEnum()){
			case SINGLE_VALUE : return SINGLE_VALUE_INTERPRETER;
			case MIN_MAX_NUMBER : return MIN_MAX_NUMBER_INTERPRETER;
			case START_END_DATE : return START_END_DATE_INTERPRETER;
			default : return null;
		}
	}
}
