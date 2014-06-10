package net.canadensys.dataportal.occurrence.dao.impl;

import java.util.List;

import net.canadensys.dataportal.occurrence.dao.OccurrenceAutoCompleteDAO;
import net.canadensys.dataportal.occurrence.model.UniqueValuesModel;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * WORK IN PROGRESS
 * Requires ElasticSearch > 1.0
 * @author cgendreau
 *
 */
public class ElasticSearchOccurrenceAutoCompleteDAO implements OccurrenceAutoCompleteDAO{
	
	private static final String INDEX_NAME = "portal";
	private static final String OCCURRENCE_TYPE = "occurrence";
	
	private static final int MAX_SIZE = 10;
	
	@Autowired
	@Qualifier("esClient")
	private Client client;
	
//	curl -X GET 'localhost:9200/portal/_search?pretty' -d '{
//	  "query":{
//	    "match" : {
//	        "country.autocomplete" : "Ca"
//	    }
//	  },
//	  "aggs" : {
//	      "countries" : {
//	          "terms" : { "field" : "country" }
//	      }
//	  }
//	}'

	@Override
	public String getSuggestionsFor(String field, String currValue, boolean useSanitizedValue) {
		
		SearchRequestBuilder srb = client.prepareSearch(INDEX_NAME)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.matchQuery("country.autocomplete", currValue))
		        //TODO add aggregation, requires ES > 1.0
	            .setSize(MAX_SIZE)
	            .setTypes(OCCURRENCE_TYPE);

		SearchResponse response = srb
		        .execute()
		        .actionGet();
		response.getHits();
		
		return null;
	}

	@Override
	public List<UniqueValuesModel> getAllPossibleValues(String field) {
		// TODO Auto-generated method stub
		return null;
	}

}
