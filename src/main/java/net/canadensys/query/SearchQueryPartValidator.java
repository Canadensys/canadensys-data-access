package net.canadensys.query;


/**
 * SearchQueryPartValidator makes sure that a specific SearchQueryPart is well formed.
 * @author canadensys
 *
 */
public class SearchQueryPartValidator {
	
	/**
	 * Validates that a SearchQueryPart is well formed.
	 * @param sqp
	 * @return
	 */
	public static boolean validate(SearchQueryPart sqp){
		//we need at least the operator and 1 value
		if(sqp.getOp() == null || sqp.getSingleValue() == null){
			return false;
		}
		
		SearchableField searchableField = sqp.getSearchableField();
		//we need the SearchableField
		if(searchableField == null){
			return false;
		}
		
		int minNumberOfValue = sqp.getOp().getMinNumberOfValue();
		int maxNumberOfValue = sqp.getOp().getMaxNumberOfValue();
		int actualNumberOfValue = sqp.getValueList().size();
		
		if(actualNumberOfValue < minNumberOfValue || actualNumberOfValue > maxNumberOfValue){
			return false;
		}
		
		//make sure the SearchableField support the operator
		if(!searchableField.getSupportedOperator().contains(sqp.getOp())){
			return false;
		}
		
		return true;
	}
}
