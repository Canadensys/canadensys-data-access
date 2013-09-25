package net.canadensys.dataportal.vascan.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.canadensys.dataportal.vascan.dao.NameDAO;
import net.canadensys.dataportal.vascan.model.NameConceptModelIF;
import net.canadensys.dataportal.vascan.model.NameConceptTaxonModel;
import net.canadensys.dataportal.vascan.model.NameConceptVernacularNameModel;
import net.canadensys.dataportal.vascan.model.TaxonLookupModel;
import net.canadensys.query.LimitedResult;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Optional feature implementation of the data access layer.
 * Add ElasticSearch to your classpath to enable it.
 * @author canadensys
 *
 */
@Repository("nameDAO")
public class ElasticSearchNameDAO implements NameDAO{
	
	private static final String INDEX_NAME = "vascan";
	
	private static final String TAXON_TYPE = "taxon";
	private static final String VERNACULAR_TYPE = "vernacular";
	
	private static final String SORT_NAME_FIELD = "sortname";
	
	private static final String TAXON_NAME_FIELD = "taxonname";
	private static final String VERNACULAR_NAME_FIELD = "vernacularname";
	
	private static final String TAXON_NAME_NGRAM_FIELD = "taxonname.ngrams";
	private static final String TAXON_NAME_EPITHET_FIELD = "taxonname.epithet";
	private static final String TAXON_NAME_GENUS_FIRST_LETTER_FIELD = "taxonname.genusfirstletter";
	
	private static final String VERNACULAR_NAME_NGRAM_FIELD = "vernacularname.ngrams";
	private static final String VERNACULAR_NAME_SPLIT_NGRAM_FIELD = "vernacularname.split_ngrams";
	private static final String VERNACULAR_NAME_SPLIT_NAME_FIELD = "vernacularname.split_name";
	
	private static final int DEFAULT_PAGE_SIZE = 50;
	private int pageSize = DEFAULT_PAGE_SIZE;
	
	@Autowired
	@Qualifier("esClient")
	private Client client;

	@Override
	public LimitedResult<List<NameConceptModelIF>> search(String text, boolean useAutocompletion){
		SearchRequestBuilder srb;
		if(useAutocompletion){
			srb = buildSearchRequestBuilderFromText(text,pageSize);
		}
		else{
			srb = buildSearchRequestBuilderFromTextNoNGram(text,pageSize);
		}
		
		SearchResponse response = srb
		        .execute()
		        .actionGet();
		LimitedResult<List<NameConceptModelIF>> qr = new LimitedResult<List<NameConceptModelIF>>();
		qr.setRows(searchHitsToNameModelList(response.getHits()));
		qr.setTotal_rows(response.getHits().getTotalHits());
		return qr;
	}
	
	@Override
	public LimitedResult<List<NameConceptModelIF>> search(String text, boolean useAutocompletion, int pageNumber) {
		SearchRequestBuilder srb;
		if(useAutocompletion){
			srb = buildSearchRequestBuilderFromText(text,pageSize);
		}
		else{
			srb = buildSearchRequestBuilderFromTextNoNGram(text,pageSize);
		}
		srb.setFrom(pageNumber*pageSize);
		
		SearchResponse response = srb
		        .execute()
		        .actionGet();
		LimitedResult<List<NameConceptModelIF>> qr = new LimitedResult<List<NameConceptModelIF>>();
		qr.setRows(searchHitsToNameModelList(response.getHits()));
		qr.setTotal_rows(response.getHits().getTotalHits());
		return qr;
	}
	
	@Override
	public List<NameConceptModelIF> searchTaxon(String text) {
		SearchRequestBuilder srb = client.prepareSearch(INDEX_NAME)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders
	                .boolQuery()
	                .should(QueryBuilders.matchQuery(TAXON_NAME_FIELD,text))
	                //avoid giving the same score to "Carex" and "Carex Carex bigelowii"
		    		.should(
		    			QueryBuilders.boolQuery()
		    				.should(QueryBuilders.matchQuery(TAXON_NAME_NGRAM_FIELD,text))
		    				.should(QueryBuilders.matchQuery(TAXON_NAME_EPITHET_FIELD,text))
		    			)
	                .should(QueryBuilders.matchQuery(TAXON_NAME_GENUS_FIRST_LETTER_FIELD,text)))
	            .setSize(pageSize)
	            .addSort(SortBuilders.scoreSort())
	            .addSort(SortBuilders.fieldSort(TAXON_NAME_FIELD).order(SortOrder.ASC))
	            .setTypes(TAXON_TYPE);

		SearchResponse response = srb
		        .execute()
		        .actionGet();
		return searchHitsToNameModelList(response.getHits());
	}
	
	@Override
	public List<NameConceptModelIF> searchVernacular(String text) {
		SearchRequestBuilder srb = client.prepareSearch(INDEX_NAME)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders
	                .boolQuery()
	                .should(QueryBuilders.matchQuery(VERNACULAR_NAME_FIELD,text))
	                .should(QueryBuilders.matchQuery(VERNACULAR_NAME_NGRAM_FIELD,text))
	                .should(QueryBuilders.matchQuery(VERNACULAR_NAME_SPLIT_NGRAM_FIELD,text)))
	            .setSize(pageSize)
	            .addSort(SortBuilders.scoreSort())
	            .addSort(SortBuilders.fieldSort(VERNACULAR_NAME_FIELD).order(SortOrder.ASC))
	            .setTypes(VERNACULAR_TYPE);

		SearchResponse response = srb
		        .execute()
		        .actionGet();
		return searchHitsToNameModelList(response.getHits());
	}
	
	/**
	 * Build an ElasticSearch query using the name and name.ngrams fields.
	 * Sorted by score then name (maybe it should be provided by the caller?)
	 * @param text
	 * @param pageSize
	 * @return
	 */
	private SearchRequestBuilder buildSearchRequestBuilderFromText(String text, int pageSize){
		return client.prepareSearch(INDEX_NAME)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders
		    			.boolQuery()
		    			.should(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(TAXON_NAME_FIELD,text)).boost(1))
		    			//avoid giving the same score to "Carex" and "Carex Carex bigelowii"
		    			.should(QueryBuilders.constantScoreQuery(
		    				QueryBuilders.boolQuery()
		    					.should(QueryBuilders.matchQuery(TAXON_NAME_NGRAM_FIELD,text))
		    					.should(QueryBuilders.matchQuery(TAXON_NAME_EPITHET_FIELD,text))
		    				).boost(1)
		    			)
		    			.should(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(TAXON_NAME_GENUS_FIRST_LETTER_FIELD,text)).boost(1))
		    			.should(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(VERNACULAR_NAME_FIELD,text)).boost(1))
		    			//Avoid giving a better score to vernacular ngrams if both query match
		    			.should(QueryBuilders.constantScoreQuery(
		    					QueryBuilders.boolQuery()
		    						.should(QueryBuilders.matchQuery(VERNACULAR_NAME_NGRAM_FIELD,text))
		    						.should(QueryBuilders.matchQuery(VERNACULAR_NAME_SPLIT_NGRAM_FIELD,text))
		    					).boost(1)
		    			)
		    	)
		    	.setSize(pageSize)
	            .addSort(SortBuilders.scoreSort())
	            .addSort(SortBuilders.fieldSort(SORT_NAME_FIELD).order(SortOrder.ASC));
	}
	
	/**
	 * Build an ElasticSearch query using the name while ignoring the ngram (used for autocompletion).
	 * Perfect match on taxon or vernacular names will get higher scores.
	 * Sorted by score then name.
	 * @param text
	 * @param pageSize
	 * @return
	 */
	private SearchRequestBuilder buildSearchRequestBuilderFromTextNoNGram(String text, int pageSize){
		return client.prepareSearch(INDEX_NAME)
	        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
	        .setQuery(QueryBuilders
	    			.boolQuery()
	    			.disableCoord(true)//ignore coord similarity 
	    			.should(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(TAXON_NAME_FIELD,text)).boost(1))
	    			.should(QueryBuilders.constantScoreQuery(
    					QueryBuilders.boolQuery()
    						.should(QueryBuilders.matchQuery(TAXON_NAME_EPITHET_FIELD,text))
    						.should(QueryBuilders.matchQuery(TAXON_NAME_GENUS_FIRST_LETTER_FIELD,text))
    					).boost(0.5f)
		    		)
	    			.should(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(VERNACULAR_NAME_FIELD,text)).boost(1))
	    			//constant query with 0.5 boost only if we do not have a complete match so scores will be comparable with taxon
	    			.should(QueryBuilders.constantScoreQuery(
    					QueryBuilders.boolQuery()
    						.mustNot(QueryBuilders.matchQuery(VERNACULAR_NAME_FIELD,text))
    						.must(QueryBuilders.matchQuery(VERNACULAR_NAME_SPLIT_NAME_FIELD,text))
    					).boost(0.5f)
		    		)
	    	)
	    	.setSize(pageSize)
            .addSort(SortBuilders.scoreSort())
            .addSort(SortBuilders.fieldSort(SORT_NAME_FIELD).order(SortOrder.ASC));
	}
	
	/**
	 * Creates a List<NameModel> from ES SearchHits object.
	 * @param hits
	 * @return
	 */
	private List<NameConceptModelIF>searchHitsToNameModelList(SearchHits hits){
		List<NameConceptModelIF> newNameModelList = new ArrayList<NameConceptModelIF>();
		NameConceptTaxonModel tNameModel;
		NameConceptVernacularNameModel vNameModel;
		Map<String,Object> esHitData = null;
		for(SearchHit currHit: hits.hits()){
			esHitData = currHit.sourceAsMap();
			if(currHit.getType().equalsIgnoreCase(TAXON_TYPE)){
				tNameModel = new NameConceptTaxonModel();
				tNameModel.setScore(currHit.getScore());
				tNameModel.setTaxonId(Integer.valueOf(currHit.getId()));
				tNameModel.setName((String)esHitData.get("taxonname"));
				tNameModel.setStatus((String)esHitData.get("status"));
				tNameModel.setNamehtml((String)esHitData.get("namehtml"));
				tNameModel.setNamehtmlauthor((String)esHitData.get("namehtmlauthor"));
				tNameModel.setRankname((String)esHitData.get("rankname"));
				if(esHitData.get("parentid") != null){
					//we can have more than one parent for a synonym even if it's rare
					if(esHitData.get("parentid").getClass() == Integer.class){
						tNameModel.setParentid((Integer)esHitData.get("parentid"));
						tNameModel.setParentnamehtml((String)esHitData.get("parentnamehtml"));
					}else{
						tNameModel.setParentidlist((List<Integer>)esHitData.get("parentid"));
						tNameModel.setParentnamehtmllist((List<String>)esHitData.get("parentnamehtml"));
					}
				}
				newNameModelList.add(tNameModel);
			}
			else if(currHit.getType().equalsIgnoreCase(VERNACULAR_TYPE)){
				vNameModel = new NameConceptVernacularNameModel();
				vNameModel.setScore(currHit.getScore());
				vNameModel.setId(Integer.valueOf(currHit.getId()));
				vNameModel.setName((String)esHitData.get("vernacularname"));
				vNameModel.setStatus((String)esHitData.get("status"));
				vNameModel.setLang((String)esHitData.get("language"));
				vNameModel.setTaxonId((Integer)esHitData.get("taxonid"));
				vNameModel.setTaxonnamehtml((String)esHitData.get("taxonnamehtml"));
				newNameModelList.add(vNameModel);
			}
		}
		return newNameModelList;
	}
	
	/**
	 * Index a taxon
	 * @param tlm
	 * @return
	 */
	public boolean indexTaxon(TaxonLookupModel tlm){
		Map<String,Object> sourceData = new HashMap<String,Object>();
		sourceData.put("name", tlm.getCalname());
		sourceData.put("status", tlm.getStatus());
		sourceData.put("rank", tlm.getRank());
		IndexResponse response = client.prepareIndex(INDEX_NAME, TAXON_TYPE)
				.setSource(sourceData)
				.setId(Integer.toString(tlm.getTaxonId()))
		        .execute()
		        .actionGet();
		return (StringUtils.equals(response.getId(),Integer.toString(tlm.getTaxonId())));
	}
	
	@Override
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	@Override
	public int getPageSize() {
		return pageSize;
	}

	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	
}
