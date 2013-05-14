package net.canadensys.dataportal.vascan.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.canadensys.dataportal.vascan.dao.NameDAO;
import net.canadensys.dataportal.vascan.model.NameModel;

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
	
	private static final int DEFAULT_PAGE_SIZE = 20;
	private int pageSize = DEFAULT_PAGE_SIZE;
	
	@Autowired
	@Qualifier("esClient")
	private Client client;

	@Override
	public List<NameModel> search(String text) {
		SearchResponse response = client.prepareSearch("vascan")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.matchQuery("name.ngrams", text))
		        .execute()
		        .actionGet();
		return searchHitsToNameModelList(response.getHits());
	}
	
	@Override
	public List<NameModel> search(String text, int pageNumber) {
		SearchResponse response = client.prepareSearch("vascan")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setFrom(pageNumber)
		        .setSize(pageSize)
		        .setQuery(QueryBuilders.matchQuery("name.ngrams", text))
		        .execute()
		        .actionGet();
		return searchHitsToNameModelList(response.getHits());
	}
	
	/**
	 * Creates a List<NameModel> from ES SearchHits object.
	 * @param hits
	 * @return
	 */
	private List<NameModel>searchHitsToNameModelList(SearchHits hits){
		List<NameModel> newNameModelList = new ArrayList<NameModel>();
		NameModel newNameModel;
		for(SearchHit currHit: hits.hits()){
			newNameModel = new NameModel();
			newNameModel.setTaxonId((Integer)currHit.sourceAsMap().get("taxonid"));
			newNameModel.setName((String)currHit.sourceAsMap().get("name"));
			newNameModelList.add(newNameModel);
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
