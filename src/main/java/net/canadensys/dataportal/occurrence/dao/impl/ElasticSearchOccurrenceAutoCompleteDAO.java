package net.canadensys.dataportal.occurrence.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.canadensys.dataportal.occurrence.dao.OccurrenceAutoCompleteDAO;
import net.canadensys.model.SuggestedValue;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
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
	private static final String AGG_SUFFIX = "_agg";
	private static final String AUTOCOMPLETE_FIELD_SUFFIX = ".autocomplete";
	
	@Autowired
	@Qualifier("esClient")
	private Client client;
	
	@Override
	public List<SuggestedValue> getSuggestionsFor(String field, String currValue, boolean useSanitizedValue) {
		String aggTerm = field + AGG_SUFFIX;
		SearchRequestBuilder srb = client.prepareSearch(INDEX_NAME)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.matchQuery(field + AUTOCOMPLETE_FIELD_SUFFIX, currValue))
		        .addAggregation(AggregationBuilders.terms(aggTerm).field(field))
	            .setSize(MAX_SIZE)
	            .setTypes(OCCURRENCE_TYPE);

		SearchResponse response = srb
		        .execute()
		        .actionGet();

		List<SuggestedValue> suggestions = esAggregationToSuggestedValues(response.getAggregations(), aggTerm);
		
		return suggestions;
	}

	@Override
	public List<SuggestedValue> getAllPossibleValues(String field) {
		String aggTerm = field + AGG_SUFFIX;
		SearchRequestBuilder srb = client.prepareSearch(INDEX_NAME)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .addAggregation(AggregationBuilders.terms(aggTerm).field(field))
	            .setTypes(OCCURRENCE_TYPE);

		SearchResponse response = srb
		        .execute()
		        .actionGet();

		List<SuggestedValue> possibleValues = esAggregationToSuggestedValues(response.getAggregations(), aggTerm);
		
		return possibleValues;
	}
	
	/**
	 * Transform an ElasticSearch Aggregation into a List<SuggestedValue> for a specific term.
	 * @param agg
	 * @param aggTerm
	 * @return
	 */
	private List<SuggestedValue> esAggregationToSuggestedValues(Aggregations agg, String aggTerm){
		List<SuggestedValue> suggestionList = new ArrayList<SuggestedValue>();
		
		Terms termAggregation = agg.get(aggTerm);
		Collection<Terms.Bucket> buckets = termAggregation.getBuckets();
		
		SuggestedValue suggestedValue;
		for(Terms.Bucket bucketItem : buckets){
			suggestedValue = new SuggestedValue();
			suggestedValue.setValue(bucketItem.getKey());
			suggestedValue.setCount(bucketItem.getDocCount());
			suggestionList.add(suggestedValue);
		}
		return suggestionList;
	}
	
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}

}
