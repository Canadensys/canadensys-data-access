package net.canadensys.dataportal.vascan.dao;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.canadensys.dataportal.vascan.model.TaxonLookupModel;

import org.hibernate.SessionFactory;
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
public class TaxonomyDAOTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private TaxonomyDAO taxonomyDAO;
	
	/**
	 * Data loaded from test/resources/vascan/vascan-insertData-dao.sql file
	 */
	@Test
	public void testTaxonomy(){
		Set<Integer> childrenIdSet = taxonomyDAO.getChildrenIdSet(73, false);
		assertTrue(childrenIdSet.contains(26));
	}

	/**
	 * Test Nested Sets building and querying
	 */
	@Test
	public void testNestedSetsBuilding(){
		taxonomyDAO.buildNestedSets(73);
		//we need to flush to ensure data is there
		sessionFactory.getCurrentSession().flush();
		List<Integer> childList = taxonomyDAO.getAcceptedChildrenIdListFromNestedSets(73);
		assertTrue(childList.contains(new Integer(26)));
		
		List<TaxonLookupModel> childObjList = taxonomyDAO.getAcceptedChildrenListFromNestedSets(73,new String[]{"class","subclass"});
		boolean found = false;
		for(TaxonLookupModel curr : childObjList){
			if(curr.getTaxonId().equals(new Integer(26))){
				assertTrue(curr.getParentID().equals(73));
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
	
	@Test
	public void testGetSynonymChildrenIdList(){
		List<Integer> synonyms = taxonomyDAO.getSynonymChildrenIdList(Arrays.asList(new Integer[]{5129}));
		assertTrue(synonyms.contains(new Integer(15428)));
	}
	
	//TODO
	//test getAcceptedChildrenListFromNestedSets
	
}
