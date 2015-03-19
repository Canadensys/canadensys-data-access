package net.canadensys.dataportal.vascan.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.canadensys.dataportal.vascan.dao.query.RegionQueryPart;
import net.canadensys.dataportal.vascan.dao.query.RegionQueryPart.RegionSelector;
import net.canadensys.dataportal.vascan.model.DistributionModel;
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
	private static final String MOCK1_AUTHOR = "_Mock1 Author";
	private static final String MOCK2_AUTHOR = "_Mock2 Author";
	private static final String MOCK3_AUTHOR = "_Mock3 Author";
	private static final String MOCK4_AUTHOR = "_Mock4 Author";
	private static final String MOCK5_AUTHOR = "_Mock5 Author";
	private static final String MOCK6_AUTHOR = "_Mock6 Author";
	
	private static final String[] NATIVE_EPHEMERE_STATUSES = new String[]{"native","ephemere"};
	
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
    			"INSERT INTO taxon (id,uninomial,binomial,author,statusid,rankid,referenceid) VALUES"
    			+ "(9470,'Verbena','×perriana','Moldenke',1,14,105),"
    			+ "(9460,'Hybrid','Parent 1','Schkuhr ex Willdenow',1,14,105),"
    			+ "(9466,'Hybrid','Parent 2','Marshall',1,14,105)",
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
		TaxonModel childTaxon = null;
		Iterator<TaxonModel> taxonIt = taxon.getChildren().iterator();
		while(taxonIt.hasNext()){
			childTaxon = taxonIt.next();
			//try to find back Equisetidae (id == 26)
			if(childTaxon.getId().intValue() == 26){
				break;
			}
		}
		
		//validate distribution
		int qty = 0;
		Iterator<DistributionModel> distIt = taxon.getDistribution().iterator();
		while(distIt.hasNext()){
			distIt.next();
			qty++;
		}
		assertTrue(qty > 2);
		
		assertEquals("Equisetidae",childTaxon.getUninomial());
		assertEquals("accepted",childTaxon.getStatus().getStatus());
		assertEquals("Subclass",childTaxon.getRank().getRank());
		
		assertEquals(new Integer(73),childTaxon.getParents().get(0).getId());
		
		List<TaxonModel> taxonList = taxonDAO.loadTaxonByName("Equisetopsida");
		assertEquals(new Integer(73),taxonList.get(0).getId());
		
		//test loadTaxonList(...)
		List<TaxonModel> taxonModelList = taxonDAO.loadTaxonList(Arrays.asList(new Integer[]{73,26}));
		assertTrue(taxonModelList.get(0).getId().equals(73) || taxonModelList.get(0).getId().equals(26));
		assertTrue(taxonModelList.get(1).getId().equals(73) || taxonModelList.get(1).getId().equals(26));
	}
	
	/**
	 * Simple test to find the only rank='class' from the searchIterator function.
	 * 
	 */
	@Test
	public void testSearchIterator(){
		Iterator<TaxonModel> taxonIt = taxonDAO.searchIterator(-1, null, null, null, null, new String[]{"class"}, false, null);
		
		assertTrue(taxonIt.hasNext());
		TaxonModel t = taxonIt.next();
		
		Iterator<DistributionModel> distIt = t.getDistribution().iterator();
		int qty=0;
		while(distIt.hasNext()){
			distIt.next();
			qty++;
		}
		assertTrue(qty > 2);
		
		assertEquals("Class", t.getRank().getRank());
	}
	
	/**
	 * Simple test to find the only rank='class' from the searchIteratorDenormalized function.
	 * 
	 */
	@Test
	public void testSearchIteratorDenormalized(){
		Iterator<Map<String,Object>> taxonIt = taxonDAO.searchIteratorDenormalized(-1, null, null, null, null, new String[]{"class"}, false, null);
		
		assertTrue(taxonIt.hasNext());
		Map<String,Object> row = null;
		int qty=0;
		while(taxonIt.hasNext()){
			row = taxonIt.next();
			qty++;
		}
		assertEquals(1, qty);
		assertEquals(new Integer(73), (Integer)row.get("id"));
		assertEquals("class", (String)row.get("rank"));
	}
	
	/**
	 * Test the search iterator with denormalized taxon data.
	 * Same tests as loadTaxonLookupModelStatusRegionCriteria() 
	 */
	@Test
	public void testSearchIteratorDenormalizedFilter(){
		
		RegionQueryPart regionQueryPart = new RegionQueryPart();
		regionQueryPart.setRegion(new String[]{"AB","bc"});
		//allof should be read : give me all the native and ephemere of AB and BC
		regionQueryPart.setRegionSelector(RegionSelector.ALL_OF);
		
		Iterator<Map<String,Object>> taxonIt = taxonDAO.searchIteratorDenormalized(-1, null, null, regionQueryPart, NATIVE_EPHEMERE_STATUSES, null, false, null);
		assertTrue(taxonIt.hasNext());
		
		List<String> mockTaxonNameList = extractMockTaxonNameFromMap(taxonIt);
		assertTrue(mockTaxonNameList.containsAll(Arrays.asList(new String[]{MOCK1_AUTHOR,MOCK3_AUTHOR})));
		assertFalse(mockTaxonNameList.contains(MOCK2_AUTHOR));
		
		//anyof should be read : give me any of the native and ephemere of AB or BC
		regionQueryPart.setRegionSelector(RegionSelector.ANY_OF);
		taxonIt = taxonDAO.searchIteratorDenormalized(-1, null, -1, regionQueryPart, NATIVE_EPHEMERE_STATUSES, null, false, null);
		assertTrue(extractMockTaxonNameFromMap(taxonIt).containsAll(Arrays.asList(new String[]{MOCK1_AUTHOR,MOCK2_AUTHOR,MOCK3_AUTHOR})));
		
		//only_in should be read : give me the native and ephemere that are only native or ephemere in AB or BC
		regionQueryPart.setRegionSelector(RegionSelector.ONLY_IN);
		taxonIt = taxonDAO.searchIteratorDenormalized(-1, null, -1, regionQueryPart, NATIVE_EPHEMERE_STATUSES, null, false, null);
		mockTaxonNameList = extractMockTaxonNameFromMap(taxonIt);
		assertTrue(mockTaxonNameList.containsAll(Arrays.asList(new String[]{MOCK3_AUTHOR, MOCK5_AUTHOR})));
		assertEquals(2, mockTaxonNameList.size());
		
		//only_in and setSearchOnlyInCanada  should be read : give me the native and ephemere that are only native or ephemere in AB or BC (ignoring Greenland and St-Pierre)
		regionQueryPart.setRegionSelector(RegionSelector.ONLY_IN);
		regionQueryPart.setSearchOnlyInCanada(true);
		taxonIt = taxonDAO.searchIteratorDenormalized(-1, null, -1, regionQueryPart, NATIVE_EPHEMERE_STATUSES, null, false, null);
		mockTaxonNameList = extractMockTaxonNameFromMap(taxonIt);
		assertTrue(mockTaxonNameList.containsAll(Arrays.asList(new String[]{MOCK3_AUTHOR, MOCK4_AUTHOR, MOCK5_AUTHOR, MOCK6_AUTHOR})));
		assertEquals(4, mockTaxonNameList.size());
		
		//all of, only in and setSearchOnlyInCanada  should be read : give me the native,ephemere that are only native or ephemere in AB and BC (ignoring Greenland and St-Pierre status)
		regionQueryPart.setRegionSelector(RegionSelector.ALL_OF_ONLY_IN);
		regionQueryPart.setSearchOnlyInCanada(true);
		taxonIt = taxonDAO.searchIteratorDenormalized(-1, null, -1, regionQueryPart, NATIVE_EPHEMERE_STATUSES, null, false, null);
		mockTaxonNameList = extractMockTaxonNameFromMap(taxonIt);
		assertTrue(mockTaxonNameList.containsAll(Arrays.asList(new String[]{MOCK3_AUTHOR, MOCK4_AUTHOR, MOCK5_AUTHOR})));
		assertEquals(3, mockTaxonNameList.size());
		
		//all of, only in should be read : give me the native,ephemere that are only native or ephemere in AB and BC (including Greenland and St-Pierre status)
		regionQueryPart.setRegionSelector(RegionSelector.ALL_OF_ONLY_IN);
		regionQueryPart.setSearchOnlyInCanada(false);
		taxonIt = taxonDAO.searchIteratorDenormalized(-1, null, -1, regionQueryPart, NATIVE_EPHEMERE_STATUSES, null, false, null);
		mockTaxonNameList = extractMockTaxonNameFromMap(taxonIt);
		assertTrue(mockTaxonNameList.containsAll(Arrays.asList(new String[]{MOCK3_AUTHOR, MOCK5_AUTHOR})));
		assertEquals(2, mockTaxonNameList.size());
		
		//test taxonid filter
		regionQueryPart.setRegionSelector(RegionSelector.ALL_OF);
		taxonIt = taxonDAO.searchIteratorDenormalized(-1, null, 1, regionQueryPart, NATIVE_EPHEMERE_STATUSES, null, false, null);
		mockTaxonNameList = extractMockTaxonNameFromMap(taxonIt);
		assertTrue(mockTaxonNameList.containsAll(Arrays.asList(new String[]{MOCK1_AUTHOR})));
	}
	
	@Test
	public void loadTaxonLookupModelCriteria(){
		int count = taxonDAO.countTaxonLookup(null, -1, null, null, new String[]{"class"}, false);
		assertTrue(count > 0);
	}
	
	@Test
	public void loadTaxonLookupModelTaxonIdCriteria(){
		int count = taxonDAO.countTaxonLookup(null, 73, null, null, null, false);
		assertTrue(count > 0);
	}	
	
	@Test
	public void loadTaxonLookupModelStatusRegionCriteria(){
		RegionQueryPart regionQueryPart = new RegionQueryPart();
		regionQueryPart.setRegion(new String[]{"AB","bc"});
		
		//always make sure that region is case insensitive
		//allof should be read : give me all the native and ephemere of AB and BC
		regionQueryPart.setRegionSelector(RegionSelector.ALL_OF);
		Iterator<TaxonLookupModel> it = taxonDAO.loadTaxonLookup(200, null, -1,regionQueryPart, new String[]{"native","ephemere"}, null, false, null);
		List<String> mockTaxonList = extractMockTaxonNameFromLookup(it);
		assertTrue(mockTaxonList.containsAll(Arrays.asList(new String[]{"_Mock1","_Mock3"})));
		assertTrue(!mockTaxonList.contains("_Mock2"));

		//anyof should be read : give me any of the native and ephemere of AB or BC
		regionQueryPart.setRegionSelector(RegionSelector.ANY_OF);
		it = taxonDAO.loadTaxonLookup(200, null, -1, regionQueryPart, new String[]{"native","ephemere"}, null, false, null);
		assertTrue(extractMockTaxonNameFromLookup(it).containsAll(Arrays.asList(new String[]{"_Mock1","_Mock2","_Mock3"})));
		
		//only_in should be read : give me the native and ephemere that are only native or ephemere in AB or BC
		regionQueryPart.setRegionSelector(RegionSelector.ONLY_IN);
		it = taxonDAO.loadTaxonLookup(200, null, -1, regionQueryPart, new String[]{"native","ephemere"}, null, false, null);
		mockTaxonList = extractMockTaxonNameFromLookup(it);
		assertTrue(mockTaxonList.containsAll(Arrays.asList(new String[]{"_Mock3","_Mock5"})));
		assertEquals(2, mockTaxonList.size());
		
		//only_in and setSearchOnlyInCanada  should be read : give me the native and ephemere that are only native or ephemere in AB or BC (ignoring Greenland and St-Pierre)
		regionQueryPart.setRegionSelector(RegionSelector.ONLY_IN);
		regionQueryPart.setSearchOnlyInCanada(true);
		it = taxonDAO.loadTaxonLookup(200, null, -1, regionQueryPart, new String[]{"native","ephemere"}, null, false, null);
		mockTaxonList = extractMockTaxonNameFromLookup(it);
		assertTrue(mockTaxonList.containsAll(Arrays.asList(new String[]{"_Mock3","_Mock4","_Mock5","_Mock6"})));
		assertEquals(4, mockTaxonList.size());
		
		//all of, only in and setSearchOnlyInCanada  should be read : give me the native,ephemere that are only native or ephemere in AB and BC (ignoring Greenland and St-Pierre status)
		regionQueryPart.setRegionSelector(RegionSelector.ALL_OF_ONLY_IN);
		regionQueryPart.setSearchOnlyInCanada(true);
		it = taxonDAO.loadTaxonLookup(200, null, -1, regionQueryPart, new String[]{"native","ephemere"}, null, false, null);
		mockTaxonList = extractMockTaxonNameFromLookup(it);
		assertTrue(mockTaxonList.containsAll(Arrays.asList(new String[]{"_Mock3","_Mock4","_Mock5"})));
		assertEquals(3, mockTaxonList.size());
		
		//all of, only in should be read : give me the native,ephemere that are only native or ephemere in AB and BC (including Greenland and St-Pierre status)
		regionQueryPart.setRegionSelector(RegionSelector.ALL_OF_ONLY_IN);
		regionQueryPart.setSearchOnlyInCanada(false);
		it = taxonDAO.loadTaxonLookup(200, null, -1, regionQueryPart, new String[]{"native","ephemere"}, null, false, null);
		mockTaxonList = extractMockTaxonNameFromLookup(it);
		assertTrue(mockTaxonList.containsAll(Arrays.asList(new String[]{"_Mock3","_Mock5"})));
		assertEquals(2, mockTaxonList.size());
	}
	
	/**
	 * Test TaxonLookupModel loading using status and rank.
	 */
	@Test
	public void loadTaxonLookupModelStatusRankCriteria(){
		RegionQueryPart regionQueryPart = new RegionQueryPart();
		regionQueryPart.setRegion(new String[]{"AB","bc"});
		regionQueryPart.setRegionSelector(RegionSelector.ANY_OF);
		
		Iterator<TaxonLookupModel> it = taxonDAO.loadTaxonLookup(200, null, -1,regionQueryPart, NATIVE_EPHEMERE_STATUSES, new String[]{"subclass","variety"}, false, null);
		List<String> mockTaxonList = extractMockTaxonNameFromLookup(it);
		assertTrue(mockTaxonList.containsAll(Arrays.asList(new String[]{"_Mock3"})));
		assertTrue(!mockTaxonList.contains("_Mock4"));
	}
	
	/**
	 * Extract all taxon calname(calculated name) from a TaxonLookupModel iterator
	 * @param it
	 * @return
	 */
	private List<String> extractMockTaxonNameFromLookup(Iterator<TaxonLookupModel> it){
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
	
	/**
	 * Extract all taxon calnameauthor(calculated name with authorship) from a key/value iterator
	 * @param it
	 * @return
	 */
	private List<String> extractMockTaxonNameFromMap(Iterator<Map<String,Object>> it){
		List<String> mockTaxonNameList = new ArrayList<String>();
		String calNameAuthor;
		while(it.hasNext()){
			calNameAuthor = (String)it.next().get(TaxonDAO.DD_CALNAME_AUTHOR);
			if(calNameAuthor.startsWith(MOCK_TAXON_NAME)){
				mockTaxonNameList.add(calNameAuthor);
			}
		}
		return mockTaxonNameList;
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
