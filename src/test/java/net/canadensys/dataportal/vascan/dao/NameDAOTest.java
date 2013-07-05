package net.canadensys.dataportal.vascan.dao;

import static org.elasticsearch.client.Requests.refreshRequest;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import net.canadensys.databaseutils.ElasticSearchTestInstance;
import net.canadensys.dataportal.vascan.dao.impl.ElasticSearchNameDAO;
import net.canadensys.dataportal.vascan.model.NameConceptModelIF;
import net.canadensys.dataportal.vascan.model.NameConceptTaxonModel;
import net.canadensys.query.LimitedResult;

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
		    .setSource(FileUtils.readFileToString(new File("script/vascan/vascan_index_creation.txt")))
		    .execute()
		    .actionGet();

		client.prepareIndex("vascan", "taxon", "951")
		        .setSource(jsonBuilder()
		                    .startObject()
		                        .field("name", "Carex")
		                        .field("status", "accepted")
		                        .field("namehtml", "<em>Carex</em>")
		                        .field("namehtmlauthor", "<em>Carex</em> Linnaeus")
		                        .field("rankname", "genus")
		                    .endObject()
		                  )
		        .execute()
		        .actionGet();

		client.prepareIndex("vascan", "taxon", "4864")
        .setSource(jsonBuilder()
                    .startObject()
                        .field("name", "Carex feta")
                        .field("status", "accepted")
                        .field("namehtml", "<em>Carex feta</em>")
                        .field("namehtmlauthor", "<em>Carex feta</em> L.H. Bailey")
                        .field("rankname", "species")
                    .endObject()
                  )
        .execute()
        .actionGet();
		
		client.prepareIndex("vascan", "taxon", "23238")
        .setSource(jsonBuilder()
                    .startObject()
                        .field("name", "Carex straminea var. mixta")
                        .field("status", "synonym")
                        .field("namehtml", "<em>Carex straminea</em> var. <em>mixta</em>>")
                        .field("namehtmlauthor", "<em>Carex straminea</em> var. <em>mixta</em> L.H. Bailey")
                        .field("parentid", 864)
                        .field("parentnamehtml", "<em>Carex feta</em>")
                    .endObject()
                  )
        .execute()
        .actionGet();
		
		client.prepareIndex("vascan", "vernacular", "3")
        .setSource(jsonBuilder()
                    .startObject()
                        .field("taxonid", 7174)
                        .field("taxonnamehtml", "<em>Picea mariana</em>")
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
		//Test asciifolding filter
		LimitedResult<List<NameConceptModelIF>> nameModeListLR = nameDAO.search("epi");
		assertEquals(1,nameModeListLR.getRows().size());
		assertEquals(1,nameModeListLR.getTotal_rows());
		assertEquals(new Integer(7174), nameModeListLR.getRows().get(0).getTaxonId());
		
		//search for carex feta
		List<NameConceptModelIF> nameModeList = nameDAO.searchTaxon("carex fe");
		assertEquals("<em>Carex feta</em> L.H. Bailey",((NameConceptTaxonModel)nameModeList.get(0)).getNamehtmlauthor());
		
		//Test with paging
		//We should not do this outside testing
		((ElasticSearchNameDAO)nameDAO).setPageSize(1);
		nameModeListLR = nameDAO.search("care",0);
		assertEquals(1,nameModeListLR.getRows().size());
		nameModeListLR = nameDAO.search("care",1);
		assertEquals(1,nameModeListLR.getRows().size());
		
		nameModeList = nameDAO.searchTaxon("epi");
		assertEquals(0,nameModeList.size());
	}
}
