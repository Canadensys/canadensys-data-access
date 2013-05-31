package net.canadensys.dataportal.vascan.dao;

import static org.junit.Assert.*;

import java.util.Set;

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
	private TaxonomyDAO taxonomyDAO;
	
	/**
	 * Data loaded from test/resources/vascan/vascan-insertData-dao.sql file
	 */
	@Test
	public void testTaxonomy(){
		Set<Integer> childrenIdSet = taxonomyDAO.getChildrenIdSet(73, false);
		assertTrue(childrenIdSet.contains(26));
	}

}
