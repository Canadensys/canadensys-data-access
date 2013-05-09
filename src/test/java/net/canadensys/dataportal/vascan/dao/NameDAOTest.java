package net.canadensys.dataportal.vascan.dao;

import static org.elasticsearch.client.Requests.refreshRequest;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import net.canadensys.databaseutils.ElasticSearchTestInstance;
import net.canadensys.dataportal.vascan.model.NameModel;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.indices.IndexMissingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test Coverage : 
 * -Search different names on ElasticSearch server
 * @author canadensys
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-spring.xml" })
public class NameDAOTest {
	
	@Autowired
	private ElasticSearchTestInstance elasticSearchTestInstance;
	
	@Autowired
	private Client client;
	
	@Autowired
	private NameDAO nameDAO;
	
	@Before
	public void setupES() throws Exception{
		try{
			client.admin().indices().prepareDelete("vascan").execute().actionGet();
		}
		catch(IndexMissingException imEx){}//ignore
		
		client.admin().indices().prepareCreate("vascan")
		    .setSource(FileUtils.readFileToString(new File("src/test/resources/vascan_index_creation.txt")))
		    .execute()
		    .actionGet();

		client.prepareIndex("vascan", "namebag", "1")
		        .setSource(jsonBuilder()
		                    .startObject()
		                        .field("taxonid", 951)
		                        .field("name", "Carex")
		                    .endObject()
		                  )
		        .execute()
		        .actionGet();

		client.prepareIndex("vascan", "namebag", "2")
        .setSource(jsonBuilder()
                    .startObject()
                        .field("taxonid", 4864)
                        .field("name", "Carex feta")
                    .endObject()
                  )
        .execute()
        .actionGet();
		
		client.prepareIndex("vascan", "namebag", "3")
        .setSource(jsonBuilder()
                    .startObject()
                        .field("taxonid", 7890)
                        .field("name", "épinette")
                    .endObject()
                  )
        .execute()
        .actionGet();
		//refresh the index
		client.admin().indices().refresh(refreshRequest()).actionGet();
	}
	
	@Test
	public void testSearch(){
		//Test asscifolding filter
		List<NameModel> nameModeList = nameDAO.search("epi");
		assertEquals(1,nameModeList.size());
		assertEquals(7890, nameModeList.get(0).getTaxonId());
		
		nameModeList = nameDAO.search("care");
		assertEquals(2,nameModeList.size());
		//Carex should be before Carex feta
		assertEquals(951, nameModeList.get(0).getTaxonId());
		assertEquals(4864, nameModeList.get(1).getTaxonId());
	}
}
