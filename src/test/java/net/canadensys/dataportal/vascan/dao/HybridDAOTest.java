package net.canadensys.dataportal.vascan.dao;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
public class HybridDAOTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private HybridDAO hybridDAO;
	
	@Test
	public void testDistributionStatus(){
		List<Map<String,Object>> data = hybridDAO.loadDenormalizedHybridParentsData(Arrays.asList(2663));
		assertEquals(2, data.size());
	}

}
