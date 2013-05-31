package net.canadensys.dataportal.vascan.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.canadensys.dataportal.vascan.dao.NameDAO;
import net.canadensys.dataportal.vascan.model.NameConceptModelIF;
import net.canadensys.dataportal.vascan.model.NameConceptTaxonModel;
import net.canadensys.dataportal.vascan.model.NameConceptVernacularNameModel;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
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
	
	private static final int DEFAULT_PAGE_SIZE = 20;
	private int pageSize = DEFAULT_PAGE_SIZE;
	
	@Autowired
	@Qualifier("esClient")
	private Client client;

	@Override
	public List<NameConceptModelIF> search(String text) {
		SearchRequestBuilder srb = client.prepareSearch(INDEX_NAME)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.matchQuery("name.ngrams", text))
		        .setSize(pageSize);
		SearchResponse response = srb
		        .execute()
		        .actionGet();
		return searchHitsToNameModelList(response.getHits());
	}
	
	@Override
	public List<NameConceptModelIF> searchTaxon(String text) {
		SearchRequestBuilder srb = client.prepareSearch(INDEX_NAME)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setTypes(TAXON_TYPE)
		        .setQuery(QueryBuilders.matchQuery("name.ngrams", text))
		        .setSize(pageSize);
		SearchResponse response = srb
		        .execute()
		        .actionGet();
		return searchHitsToNameModelList(response.getHits());
	}
	
	@Override
	public List<NameConceptModelIF> searchVernacular(String text) {
		SearchRequestBuilder srb = client.prepareSearch(INDEX_NAME)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setTypes(VERNACULAR_TYPE)
		        .setQuery(QueryBuilders.matchQuery("name.ngrams", text))
		        .setSize(pageSize);
		SearchResponse response = srb
		        .execute()
		        .actionGet();
		return searchHitsToNameModelList(response.getHits());
	}
	
	@Override
	public List<NameConceptModelIF> search(String text, int pageNumber) {
		
		SearchRequestBuilder srb = client.prepareSearch("vascan")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setFrom(pageNumber)
		        .setSize(pageSize)
		        .setQuery(QueryBuilders.matchQuery("name.ngrams", text));
		SearchResponse response = srb
		        .execute()
		        .actionGet();
		return searchHitsToNameModelList(response.getHits());
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
		for(SearchHit currHit: hits.hits()){
			if(currHit.getType().equalsIgnoreCase(TAXON_TYPE)){
				tNameModel = new NameConceptTaxonModel();
				tNameModel.setTaxonId(Integer.parseInt(currHit.getId()));
				tNameModel.setName((String)currHit.sourceAsMap().get("name"));
				tNameModel.setStatus((String)currHit.sourceAsMap().get("status"));
				newNameModelList.add(tNameModel);
			}
			else if(currHit.getType().equalsIgnoreCase(VERNACULAR_TYPE)){
				vNameModel = new NameConceptVernacularNameModel();
				vNameModel.setTaxonId((Integer)currHit.sourceAsMap().get("taxonid"));
				vNameModel.setName((String)currHit.sourceAsMap().get("name"));
				vNameModel.setStatus((String)currHit.sourceAsMap().get("status"));
				vNameModel.setLang((String)currHit.sourceAsMap().get("language"));
				newNameModelList.add(vNameModel);
			}
		}
		return newNameModelList;
	}
	
	@Override
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	
}
