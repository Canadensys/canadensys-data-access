package net.canadensys.query;

/**
 * Enumeration representing an operator in a search query.
 * The operator must define the minimum and maximum number of value it can handle.
 * @author canadensys
 *
 */
public enum QueryOperatorEnum {
	
	EQ(1,1),
	NEQ(1,1),	//not equals
	SLIKE(1,1), //Starts like ...
	ELIKE(1,1), //Ends like ...
	CLIKE(1,1), //Contains like 
	IN(1,Integer.MAX_VALUE),
	BETWEEN(2,2);
	
    private final int minNumberOfValue;
    private final int maxNumberOfValue;
    
    private QueryOperatorEnum(int minNumberOfValue, int maxNumberOfValue) {
        this.minNumberOfValue = minNumberOfValue;
        this.maxNumberOfValue = maxNumberOfValue;
    }
    
    public int getMinNumberOfValue() { return minNumberOfValue; }
    public int getMaxNumberOfValue() { return maxNumberOfValue; }
    
    /**
     * You should use this function instead of valueOf if you need more flexibility.
     * This function is case insensitive and will return null instead an throwing an exception
     * when a value is not found.
     * @param text
     * @return the matching QueryOperatorEnum value or null if not found
     */
    public static QueryOperatorEnum fromString(String text) {
    	if (text != null) {
    		for (QueryOperatorEnum op : QueryOperatorEnum.values()) {
    			if (text.equalsIgnoreCase(op.toString())) {
    				return op;
    			}
    		}
    	}
        return null;
    }

}
