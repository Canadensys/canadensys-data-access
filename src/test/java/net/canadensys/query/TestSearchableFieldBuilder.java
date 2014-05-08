package net.canadensys.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to build SearchableField for testing purpose.
 * @author canadensys
 *
 */
public class TestSearchableFieldBuilder {
	
	public static SearchableField buildMinMaxIntegerSearchableField(){
		return new SearchableField(){
			@Override
			public SearchableFieldTypeEnum getSearchableFieldTypeEnum() {
				return SearchableFieldTypeEnum.MIN_MAX_NUMBER;
			}

			@Override
			public Class<?> getType() {
				return Integer.class;
			}

			@Override
			public List<QueryOperatorEnum> getSupportedOperator() {
				List<QueryOperatorEnum> supportedOperator = new ArrayList<QueryOperatorEnum>();
				supportedOperator.add(QueryOperatorEnum.EQ);
				supportedOperator.add(QueryOperatorEnum.IN);
				return supportedOperator;
			}

			@Override
			public List<String> getRelatedFields() {
				List<String> relatedFields = new ArrayList<String>();
				relatedFields.add("min");
				relatedFields.add("max");
				return relatedFields;
			}

			@Override
			public int getSearchableFieldId() {
				return 1;
			}

			@Override
			public String getSearchableFieldName() {
				return "testMinMax";
			}

			@Override
			public String getRelatedField() {
				return null;
			}
		};
	}
	
	public static SearchableField buildStartEndDateSearchableField(){
		return new SearchableField(){
			@Override
			public SearchableFieldTypeEnum getSearchableFieldTypeEnum() {
				return SearchableFieldTypeEnum.START_END_DATE;
			}

			@Override
			public Class<?> getType() {
				return null;
			}

			@Override
			public List<QueryOperatorEnum> getSupportedOperator() {
				List<QueryOperatorEnum> supportedOperator = new ArrayList<QueryOperatorEnum>();
				supportedOperator.add(QueryOperatorEnum.EQ);
				return supportedOperator;
			}

			@Override
			public List<String> getRelatedFields() {
				List<String> relatedFields = new ArrayList<String>();
				relatedFields.add("syear");
				relatedFields.add("smonth");
				relatedFields.add("sday");
				return relatedFields;
			}

			@Override
			public int getSearchableFieldId() {
				return 1;
			}

			@Override
			public String getSearchableFieldName() {
				return "testStartEndDate";
			}

			@Override
			public String getRelatedField() {
				return null;
			}
		};
	}
	
	public static SearchableField buildSingleValueSearchableField(final int searchableFieldId,final String searchableFieldName, final String relatedField){
		return new SearchableField(){

			@Override
			public int getSearchableFieldId() {
				return searchableFieldId;
			}

			@Override
			public String getSearchableFieldName() {
				return searchableFieldName;
			}

			@Override
			public SearchableFieldTypeEnum getSearchableFieldTypeEnum() {
				return SearchableFieldTypeEnum.SINGLE_VALUE;
			}

			@Override
			public Class<?> getType() {
				return String.class;
			}

			@Override
			public List<QueryOperatorEnum> getSupportedOperator() {
				List<QueryOperatorEnum> supportedOperator = new ArrayList<QueryOperatorEnum>();
				supportedOperator.add(QueryOperatorEnum.EQ);
				supportedOperator.add(QueryOperatorEnum.SLIKE);
				supportedOperator.add(QueryOperatorEnum.ELIKE);
				supportedOperator.add(QueryOperatorEnum.CLIKE);
				supportedOperator.add(QueryOperatorEnum.IN);
				return supportedOperator;
			}

			@Override
			public List<String> getRelatedFields() {
				List<String> relatedFields = new ArrayList<String>();
				relatedFields.add(relatedField);
				return relatedFields;
			}

			@Override
			public String getRelatedField() {
				return relatedField;
			}
			
		};
	}
	
	public static SearchableField buildNumericSingleValueSearchableField(final int searchableFieldId,final String searchableFieldName, final String relatedField, final Class<? extends Number> clazz){
		return new SearchableField(){

			@Override
			public int getSearchableFieldId() {
				return searchableFieldId;
			}

			@Override
			public String getSearchableFieldName() {
				return searchableFieldName;
			}

			@Override
			public SearchableFieldTypeEnum getSearchableFieldTypeEnum() {
				return SearchableFieldTypeEnum.SINGLE_VALUE;
			}

			@Override
			public Class<?> getType() {
				return clazz;
			}

			@Override
			public List<QueryOperatorEnum> getSupportedOperator() {
				List<QueryOperatorEnum> supportedOperator = new ArrayList<QueryOperatorEnum>();
				supportedOperator.add(QueryOperatorEnum.EQ);
				supportedOperator.add(QueryOperatorEnum.IN);
				return supportedOperator;
			}

			@Override
			public List<String> getRelatedFields() {
				List<String> relatedFields = new ArrayList<String>();
				relatedFields.add(relatedField);
				return relatedFields;
			}

			@Override
			public String getRelatedField() {
				return relatedField;
			}
			
		};
	}
	
	public static SearchableField buildBooleanSingleValueSearchableField(final int searchableFieldId,final String searchableFieldName, final String relatedField){
		return new SearchableField(){

			@Override
			public int getSearchableFieldId() {
				return searchableFieldId;
			}

			@Override
			public String getSearchableFieldName() {
				return searchableFieldName;
			}

			@Override
			public SearchableFieldTypeEnum getSearchableFieldTypeEnum() {
				return SearchableFieldTypeEnum.SINGLE_VALUE;
			}

			@Override
			public Class<?> getType() {
				return Boolean.class;
			}

			@Override
			public List<QueryOperatorEnum> getSupportedOperator() {
				List<QueryOperatorEnum> supportedOperator = new ArrayList<QueryOperatorEnum>();
				supportedOperator.add(QueryOperatorEnum.EQ);
				return supportedOperator;
			}

			@Override
			public List<String> getRelatedFields() {
				List<String> relatedFields = new ArrayList<String>();
				relatedFields.add(relatedField);
				return relatedFields;
			}

			@Override
			public String getRelatedField() {
				return relatedField;
			}
			
		};
	}
	
	public static SearchableField buildInsidePolygonSearchableField(){
		return new SearchableField(){

			@Override
			public int getSearchableFieldId() {
				return 6; //random number
			}

			@Override
			public String getSearchableFieldName() {
				return "bounding box";
			}

			@Override
			public SearchableFieldTypeEnum getSearchableFieldTypeEnum() {
				return SearchableFieldTypeEnum.INSIDE_POLYGON_GEO;
			}

			@Override
			public Class<?> getType() {
				return null;
			}

			@Override
			public List<QueryOperatorEnum> getSupportedOperator() {
				List<QueryOperatorEnum> supportedOperator = new ArrayList<QueryOperatorEnum>();
				supportedOperator.add(QueryOperatorEnum.IN);
				return supportedOperator;
			}

			@Override
			public List<String> getRelatedFields() {
				List<String> relatedFields = new ArrayList<String>();
				relatedFields.add("the_geom");
				return relatedFields;
			}

			@Override
			public String getRelatedField() {
				return "the_geom";
			}
			
		};
	}
}
