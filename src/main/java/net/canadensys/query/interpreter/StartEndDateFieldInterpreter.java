package net.canadensys.query.interpreter;

import java.util.List;

import net.canadensys.databaseutils.SQLHelper;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableField;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class StartEndDateFieldInterpreter implements QueryPartInterpreter{
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(StartEndDateFieldInterpreter.class);

	@Override
	public boolean canHandleSearchQueryPart(SearchQueryPart searchQueryPart) {
		SearchableField searchableField = searchQueryPart.getSearchableField();

		return (searchableField.getRelatedFields() != null && 
				searchableField.getRelatedFields().size() >= 3);
	}

	@Override
	public Criterion toCriterion(SearchQueryPart searchQueryPart) {

		if(!canHandleSearchQueryPart(searchQueryPart)){
			LOGGER.error("Can't handle QueryPart : " + searchQueryPart);
			return null;
		}
		
		SearchableField searchableField = searchQueryPart.getSearchableField();
		List<String> relatedFieldList = searchableField.getRelatedFields();
		List<String> valueList = searchQueryPart.getValueList();
		Criterion criterion = null;
		
		switch(searchQueryPart.getOp()){
			case EQ : 
				String eqValue = valueList.get(0);
				Object parsedValue = null;
				
				//create Criterion by including the parsedValues of each relatedField
				for(String currRelatedField : relatedFieldList){
					parsedValue = searchQueryPart.getParsedValue(eqValue, currRelatedField);
					//could be optional
					if(parsedValue != null){
						Criterion innerCriterion = Restrictions.eq(currRelatedField, parsedValue);
						if(criterion != null){
							criterion = Restrictions.and(criterion, innerCriterion);
						}
						else{
							criterion = innerCriterion;
						}
					}
				}
				return criterion;
			case BETWEEN : 
				
				String startValue = valueList.get(0);
				String endValue = valueList.get(1);
				Object startValueParsed;
				Object endValueParsed;
				
				//create Criterion by including the parsedValues of each relatedField
				for(String currRelatedField : relatedFieldList){
					startValueParsed = searchQueryPart.getParsedValue(startValue, currRelatedField);
					endValueParsed = searchQueryPart.getParsedValue(endValue, currRelatedField);
					
					//could be optional
					if(startValueParsed != null && endValueParsed != null){
						Criterion innerCriterion = Restrictions.and(
									Restrictions.ge(currRelatedField, startValueParsed),
									Restrictions.le(currRelatedField, endValueParsed)
								);

						if(criterion != null){
							criterion = Restrictions.and(criterion, innerCriterion);
						}
						else{
							criterion = innerCriterion;
						}
					}
				}
				return criterion;
			default : 
				LOGGER.fatal("Impossible to interpret " + searchQueryPart);
				return null;
		}
	}

	@Override
	public String toSQL(SearchQueryPart searchQueryPart) {
		if(!canHandleSearchQueryPart(searchQueryPart)){
			LOGGER.error("Can't handle QueryPart : " + searchQueryPart);
			return null;
		}
		
		SearchableField searchableField = searchQueryPart.getSearchableField();
		List<String> relatedFieldList = searchableField.getRelatedFields();
		List<String> valueList = searchQueryPart.getValueList();
		String sql = null;
		
		switch(searchQueryPart.getOp()){
			case EQ : 
				String eqValue = valueList.get(0);
				Object parsedValue = null;
				
				//create Criterion by including the parsedValues of each relatedField
				for(String currRelatedField : relatedFieldList){
					parsedValue = searchQueryPart.getParsedValue(eqValue, currRelatedField);
					//could be optional
					if(parsedValue != null){
						String innerSql = SQLHelper.eq(currRelatedField,parsedValue);
						if(sql != null){
							sql = SQLHelper.and(sql, innerSql);
						}
						else{
							sql = innerSql;
						}
					}
				}
				return sql;
			case BETWEEN : 
				String startValue = valueList.get(0);
				String endValue = valueList.get(1);
				Object startValueParsed;
				Object endValueParsed;
				
				//create Criterion by including the parsedValues of each relatedField
				for(String currRelatedField : relatedFieldList){
					startValueParsed = searchQueryPart.getParsedValue(startValue, currRelatedField);
					endValueParsed = searchQueryPart.getParsedValue(endValue, currRelatedField);
					
					//could be optional
					if(startValueParsed != null && endValueParsed != null){
						String innerSql = SQLHelper.and(
									SQLHelper.ge(currRelatedField, startValueParsed),
									SQLHelper.le(currRelatedField, endValueParsed));

						if(sql != null){
							sql = SQLHelper.and(sql, innerSql);
						}
						else{
							sql = innerSql;
						}
					}
				}
				return sql;
			default : 
				LOGGER.fatal("Impossible to interpret " + searchQueryPart);
				return null;
		}
	}

}
