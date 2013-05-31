package net.canadensys.dataportal.vascan.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import net.canadensys.dataportal.vascan.model.DistributionStatusModel;

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
public class DistributionDAOTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private DistributionDAO distributionDAO;
	
	/**
	 * Data loaded from test/resources/vascan/vascan-insertData-dao.sql file
	 */
	@Test
	public void testDistributionStatus(){
		List<DistributionStatusModel> distributionStatusModelList = distributionDAO.loadAllDistributionStatus();
		boolean foundAbsent = false;
		for(DistributionStatusModel curr : distributionStatusModelList){
			if(curr.getDistributionstatus().equalsIgnoreCase("absent")){
				foundAbsent = true;
				break;
			}
		}
		assertTrue(foundAbsent);
	}
}
