package net.canadensys.dataportal.vascan.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.canadensys.dataportal.vascan.model.VernacularNameModel;

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
public class VernacularNameDAOTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private VernacularNameDAO vernacularNameDAO;
	
	/**
	 * Data loaded from test/resources/vascan/vascan-insertData-dao.sql file
	 */
	@Test
	public void testLoadVernacularNameModel(){
		VernacularNameModel vernacularNameModel = vernacularNameDAO.loadVernacularName(1);
		assertEquals("Fougères",vernacularNameModel.getName());
		
		List<VernacularNameModel> vernacularNameModelList = vernacularNameDAO.loadVernacularNameByName("Fougères");
		assertEquals(1,vernacularNameModelList.get(0).getId());
	}
}
