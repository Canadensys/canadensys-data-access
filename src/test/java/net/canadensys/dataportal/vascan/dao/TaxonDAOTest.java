package net.canadensys.dataportal.vascan.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import net.canadensys.dataportal.vascan.model.TaxonLookupModel;
import net.canadensys.dataportal.vascan.model.TaxonModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/vascan/vascan-test-context.xml" })
@TransactionConfiguration(transactionManager="hibernateTransactionManager")
public class TaxonDAOTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private TaxonDAO taxonDAO;
		
	@Test
	public void testSaveLoadTaxonLookupModel(){
		
		//"higherclassification","class","order","family","genus","subgenus","specificepithet","infraspecificepithet","author","vernacularfr","vernacularen","cdate","mdate"
		//"","Equisetopsida","","","","","","","C. Aghard","","","2013-03-08 00:00:00","2013-03-08 10:30:44"
		
		TaxonLookupModel tlm = new TaxonLookupModel();
		tlm.setTaxonId(73);
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
		
		TaxonLookupModel loadedTlm = taxonDAO.loadTaxonLookup(73);
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
		
		int count = taxonDAO.countTaxonLookup("allof", null, -1, null, null, new String[]{"class"}, true, null);
		assertEquals(1,count);
		
		Iterator<TaxonLookupModel> it = taxonDAO.loadTaxonLookup(200, "allof", null, -1, null, null, new String[]{"class"}, true, null);
		assertTrue(it.hasNext());
		assertEquals(new Integer(73),it.next().getTaxonId());
	}
	
	@Test
	public void testGetAcceptedTaxon(){
		//get all taxon for class and above
		List<Object[]> taxonInfo = taxonDAO.getAcceptedTaxon(1);
		assertEquals(1,taxonInfo.size());
		assertEquals(new Integer(73),taxonInfo.get(0)[0]);
	}


}
