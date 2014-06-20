package net.canadensys.dataportal.occurrence.dao;

import static org.elasticsearch.client.Requests.refreshRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.List;

import net.canadensys.databaseutils.ElasticSearchTestInstance;
import net.canadensys.dataportal.vascan.dao.NameDAOTest;
import net.canadensys.model.SuggestedValue;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.indices.IndexMissingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test autocomplete feature using ElasticSearch.
 * @author cgendreau
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-spring.xml" })
public class ESOccurrenceAutoCompleteDAOTest {
	
	private static final String INDEX_NAME = "portal";
	private static final String DOC_TYPE = "occurrence";
	
	@Autowired
	private ElasticSearchTestInstance elasticSearchTestInstance;
	
	@Autowired
	private Client client;
	
	@Autowired
	private OccurrenceAutoCompleteDAO esAutoCompleteOccurrenceDAO;
	
	@Before
	public void setupES() throws Exception{
		try{
			client.admin().indices().prepareDelete(INDEX_NAME).execute().actionGet();
		}
		catch(IndexMissingException imEx){}//ignore
		
		URL indexCreationScriptURL = NameDAOTest.class.getResource("/script/occurrence/occurrence_index_creation.txt");
		client.admin().indices().prepareCreate(INDEX_NAME)
		    .setSource(FileUtils.readFileToString(new File(indexCreationScriptURL.toURI())))
		    .execute()
		    .actionGet();
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		
		URL dataScriptURL = NameDAOTest.class.getResource("/occurrence/occurrence-ES-testData-dao.json");		
		ObjectMapper om = new ObjectMapper();
		JsonNode node = om.readTree(dataScriptURL);

		if (node.isArray()) {
			Integer i=0;
		    for (final JsonNode currNode : node) {
		    	bulkRequest.add(client.prepareIndex(INDEX_NAME, DOC_TYPE,i.toString()).setSource(currNode.toString()));
		    	i++;
		    }
		}

		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
		    fail("bulkResponse contains failure(s)");
		}

		//refresh the index
		client.admin().indices().refresh(refreshRequest()).actionGet();
	}
	
	@Test
	public void testCountrySuggestion(){
		List<SuggestedValue> suggestions = esAutoCompleteOccurrenceDAO.getSuggestionsFor("country", "ca", true);
		
		assertEquals("Canada", suggestions.get(0).getValue());
		assertEquals(2, suggestions.get(0).getCount());
		
		//aggregation is case sensitive (by our configuration)
		assertEquals("canada", suggestions.get(1).getValue());
		assertEquals(1, suggestions.get(1).getCount());
		
		//for now, standard search_analyzer is used. Meaning that search string is broken into tokens.
		suggestions = esAutoCompleteOccurrenceDAO.getSuggestionsFor("country", "test united", true);
		assertEquals("United States", suggestions.get(0).getValue());
	}
	
	@Test
	public void testCountryPossibleValue(){
		List<SuggestedValue> possibleValues = esAutoCompleteOccurrenceDAO.getAllPossibleValues("country");
		assertEquals("Canada", possibleValues.get(0).getValue());
		assertEquals(3, possibleValues.size());
	}

}
