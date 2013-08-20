package net.canadensys.dataportal.vascan.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import net.canadensys.dataportal.vascan.model.TaxonLookupModel;
import net.canadensys.dataportal.vascan.model.TaxonModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/vascan/vascan-test-context.xml" })
@TransactionConfiguration(transactionManager="hibernateTransactionManager")
public class TaxonDAOTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	private static final String MOCK_TAXON_NAME = "_Mock";
	
	@Autowired
	private TaxonDAO taxonDAO;
	
	private JdbcTemplate jdbcTemplate;
	
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Before
    public void testSetup(){
    	//Used for test : testDeleteTaxon()
    	jdbcTemplate.batchUpdate(new String[]{
    			"INSERT INTO taxon (id,uninomial,binomial,author,statusid,rankid,referenceid) VALUES" +
    			"(9470,'Verbena','×perriana','Moldenke',1,14,105),"+
    			"(9460,'Hybrid ','Parent 1','Schkuhr ex Willdenow',1,14,105),(9466,'Hybrid ','Parent 2','Marshall',1,14,105)",
    			"INSERT INTO taxonomy (parentid,childid) VALUES (73,9470)",
    			"INSERT INTO taxonomy (parentid,childid) VALUES (9466,1)",
    			"INSERT INTO taxonhybridparent (id,childid,parentid) VALUES (729,9470,9460),(730,9470,9466)",
    			"INSERT INTO taxonhabit (id,taxonid,habitid) VALUES (730,9470,1)"});
    }
		
	@Test
	public void testSaveLoadTaxonLookupModel(){
		TaxonLookupModel tlm = new TaxonLookupModel();
		tlm.setTaxonId(173);
		tlm.setCalname("Equisetopsida");
		tlm.setCalnameauthor("Equisetopsida C. Aghard");
		tlm.setCalnamehtml("<em>Equisetopsida</em>");
		tlm.setCalnamehtmlauthor("<em>Equisetopsida</em> C. Aghard");
		tlm.setStatus("accepted");
		tlm.setRank("class");
		tlm.setCalhabit("herb,shrub,tree,vine");
		
		tlm.setAB("native");
		tlm.setBC("native");
		tlm.setGL("native");
		tlm.setNL_L("native");
		tlm.setMB("native");
		tlm.setNB("native");
		tlm.setNL_N("native");
		tlm.setNT("native");
		tlm.setNS("native");
		tlm.setNU("native");
		tlm.setON("native");
		tlm.setPE("native");
		tlm.setQC("native");
		tlm.setPM("native");
		tlm.setSK("native");
		tlm.setYT("native");

		assertTrue(taxonDAO.saveTaxonLookup(tlm));
		
		TaxonLookupModel loadedTlm = taxonDAO.loadTaxonLookup(173);
		assertEquals("<em>Equisetopsida</em> C. Aghard",loadedTlm.getCalnamehtmlauthor());
		assertEquals("native",loadedTlm.getGL());
	}
	
	/**
	 * Data loaded from test/resources/vascan/vascan-insertData-dao.sql file
	 */
	@Test
	public void testLoadTaxonModel(){
		TaxonModel taxon = taxonDAO.loadTaxon(73);
		assertEquals("Equisetopsida",taxon.getUninomial());
		//validate joins
		assertEquals("accepted",taxon.getStatus().getStatus());
		assertEquals("Class",taxon.getRank().getRank());
		
		//validate taxonomy
		TaxonModel childTaxon = taxon.getChildren().get(0);
		assertEquals("Equisetidae",childTaxon.getUninomial());
		assertEquals("accepted",childTaxon.getStatus().getStatus());
		assertEquals("Subclass",childTaxon.getRank().getRank());
		
		assertEquals(new Integer(73),childTaxon.getParents().get(0).getId());
		
		List<TaxonModel> taxonList = taxonDAO.loadTaxonByName("Equisetopsida");
		assertEquals(new Integer(73),taxonList.get(0).getId());
	}
	
	@Test
	public void loadTaxonLookupModelCriteria(){
		int count = taxonDAO.countTaxonLookup(null, -1, null, null, null, new String[]{"class"}, false);
		assertTrue(count > 0);
	}
	
	@Test
	public void loadTaxonLookupModelTaxonIdCriteria(){
		int count = taxonDAO.countTaxonLookup(null, 73, null, null, null, null, false);
		assertTrue(count > 0);
	}
	
	@Test
	public void loadTaxonLookupModelStatusRegionCriteria(){
		
		//allof should be read : give me all the native and ephemere of AB and BC
		Iterator<TaxonLookupModel> it = taxonDAO.loadTaxonLookup(200, null, -1,"allof", new String[]{"AB","BC"}, new String[]{"native","ephemere"}, null, false, null);
		List<String> mockTaxonList = extractMockTaxon(it);
		assertTrue(mockTaxonList.containsAll(Arrays.asList(new String[]{"_Mock1","_Mock3"})));
		assertTrue(!mockTaxonList.contains("_Mock2"));

		//anyof should be read : give me any of the native and ephemere of AB or BC
		it = taxonDAO.loadTaxonLookup(200, null, -1, "anyof", new String[]{"AB","BC"}, new String[]{"native","ephemere"}, null, false, null);
		assertTrue(extractMockTaxon(it).containsAll(Arrays.asList(new String[]{"_Mock1","_Mock2","_Mock3"})));
		
		//only should be read : give me the native and ephemere that are only native or ephemere in AB or BC
		it = taxonDAO.loadTaxonLookup(200, null, -1,"only", new String[]{"AB","BC"}, new String[]{"native","ephemere"}, null, false, null);
		mockTaxonList = extractMockTaxon(it);
		assertTrue(mockTaxonList.contains("_Mock3"));
		assertEquals(1, mockTaxonList.size());
		
		//only_ca should be read : give me the native and ephemere that are only native or ephemere in AB or BC (ignoring Greenland and St-Pierre)
		it = taxonDAO.loadTaxonLookup(200, null, -1, "only_ca", new String[]{"AB","BC"}, new String[]{"native","ephemere"}, null, false, null);
		mockTaxonList = extractMockTaxon(it);
		assertTrue(mockTaxonList.containsAll(Arrays.asList(new String[]{"_Mock3","_Mock4"})));
		assertEquals(2, mockTaxonList.size());

	}
	
	private List<String> extractMockTaxon(Iterator<TaxonLookupModel> it){
		List<String> mockTaxonList = new ArrayList<String>();
		String calname;
		while(it.hasNext()){
			calname = it.next().getCalname();
			if(calname.startsWith(MOCK_TAXON_NAME)){
				mockTaxonList.add(calname);
			}
		}
		return mockTaxonList;
	}
	
	@Test
	public void testGetAcceptedTaxon(){
		//get all taxon for class and above
		List<Object[]> taxonInfo = taxonDAO.getAcceptedTaxon(1);
		assertEquals(1,taxonInfo.size());
		assertEquals(new Integer(73),taxonInfo.get(0)[0]);
	}
	
	@Test
	public void testDeleteTaxon(){
		boolean success = taxonDAO.deleteTaxon(9466);
		assertFalse("We should not be able to delete a taxon when this latter is used as a hybrid parent or a parent in taxonomy",success);
		
		//remove the hybridparent
		jdbcTemplate.execute("DELETE FROM taxonhybridparent WHERE parentid = 9466");
		success = taxonDAO.deleteTaxon(9466);
		assertFalse("We should not be able to delete a taxon when this latter is used as a hybrid parent or a parent in taxonomy",success);
		//remove it from taxonomy, this should never be done since it creates a broken tree. We only do this in testing for simplicity.
		jdbcTemplate.execute("DELETE FROM taxonomy WHERE parentid = 9466");
		
		//now it should work
		success = taxonDAO.deleteTaxon(9466);
		assertTrue("We should be able to delete a taxon when the it's not used.",success);
	}

}
