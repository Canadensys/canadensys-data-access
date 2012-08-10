package net.canadensys.query.interpreter;

import java.util.List;

import net.canadensys.databaseutils.SQLHelper;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchableField;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Interprets a SearchQueryPart on more than one numeric database field.
 * Example : We search for field IntField1 but in the database this fields is stored as a range IntFieldMin and IntFieldMax.
 * @author canadensys
 *
 */
public class MinMaxNumberFieldInterpreter implements QueryPartInterpreter{
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(MinMaxNumberFieldInterpreter.class);
	
	@Override
	public boolean canHandleSearchableField(SearchableField searchableField) {
		return (searchableField.getRelatedFields() != null && 
				searchableField.getRelatedFields().size() == 2 &&
				searchableField.getType() != null &&
				Number.class.isAssignableFrom(searchableField.getType()));
	}

	@Override
	public boolean canHandleSearchQueryPart(SearchQueryPart searchQueryPart) {
		return canHandleSearchableField(searchQueryPart.getSearchableField());
	}
	
	@Override
	public Criterion toCriterion(SearchQueryPart searchQueryPart) {

		if(!canHandleSearchQueryPart(searchQueryPart)){
			LOGGER.error("Can't handle QueryPart : " + searchQueryPart);
			return null;
		}
		
		SearchableField searchableField = searchQueryPart.getSearchableField();
		List<String> valueList = searchQueryPart.getValueList();
		Object parsedValueMin = searchQueryPart.getParsedValue(valueList.get(0), searchableField.getRelatedFields().get(0));
		
		switch(searchQueryPart.getOp()){
			case EQ : return Restrictions.and(
					Restrictions.le(searchableField.getRelatedFields().get(0), parsedValueMin),
					Restrictions.ge(searchableField.getRelatedFields().get(1), parsedValueMin));
			case BETWEEN : 
				Object parsedValueMax = searchQueryPart.getParsedValue(valueList.get(1), searchableField.getRelatedFields().get(1));
				return Restrictions.or(
					Restrictions.and(
							Restrictions.ge(searchableField.getRelatedFields().get(0), parsedValueMin),
							Restrictions.le(searchableField.getRelatedFields().get(0), parsedValueMax)),
					Restrictions.and(
							Restrictions.ge(searchableField.getRelatedFields().get(1), parsedValueMin),
							Restrictions.le(searchableField.getRelatedFields().get(1), parsedValueMax))
				);
			default : 
				LOGGER.fatal("Impossible to interpret " + searchQueryPart);
				return null;
		}
	}

	@Override
	public String toSQL(SearchQueryPart searchQueryPart) {
		
		SearchableField searchableField = searchQueryPart.getSearchableField();
		List<String> valueList = searchQueryPart.getValueList();
		Object parsedValueMin = searchQueryPart.getParsedValue(valueList.get(0), searchableField.getRelatedFields().get(0));
		
		if(searchQueryPart.getOp() == QueryOperatorEnum.EQ){
			return SQLHelper.between(parsedValueMin, searchableField.getRelatedFields().get(0), searchableField.getRelatedFields().get(1));
		}
		else if(searchQueryPart.getOp() == QueryOperatorEnum.BETWEEN){
			Object parsedValueMax = searchQueryPart.getParsedValue(valueList.get(1), searchableField.getRelatedFields().get(1));
			return SQLHelper.or(
				SQLHelper.between(parsedValueMin, searchableField.getRelatedFields().get(0), searchableField.getRelatedFields().get(1)),
				SQLHelper.between(parsedValueMax, searchableField.getRelatedFields().get(0), searchableField.getRelatedFields().get(1))
			);
		}
		else{
			LOGGER.fatal("Impossible to interpret " + searchQueryPart);
			return null;
		}
	}
}
