package net.canadensys.dataportal.occurrence.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import net.canadensys.dataportal.occurrence.model.ImportLogModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Test Coverage : 
 * -Save ImportLogDAO
 * -Get generated id
 * -Load ImportLogDAO from id
 * @author canadensys
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-spring.xml" })
@TransactionConfiguration(transactionManager="hibernateTransactionManager")
public class ImportLogDAOTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private ImportLogDAO importLogDAO;
	
	@Test
	public void testSaveAndLoad(){
		ImportLogModel testModel = new ImportLogModel();
		Date now = new Date();
		testModel.setSourcefileid("test_sourcefileid");
		testModel.setUpdated_by("me");
		testModel.setEvent_end_date_time(now);
		importLogDAO.save(testModel);
		
		int id = testModel.getId();
		
		ImportLogModel loadedModel = importLogDAO.load(id);
		assertEquals("test_sourcefileid",loadedModel.getSourcefileid());
		assertEquals("me",loadedModel.getUpdated_by());
		assertEquals(now,loadedModel.getEvent_end_date_time());
	}
}
