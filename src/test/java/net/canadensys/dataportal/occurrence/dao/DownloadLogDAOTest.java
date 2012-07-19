package net.canadensys.dataportal.occurrence.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import net.canadensys.dataportal.occurrence.dao.DownloadLogDAO;
import net.canadensys.dataportal.occurrence.model.DownloadLogModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Test Coverage : 
 * -Save DownloadLogModel
 * -Load DownloadLogModel
 * @author canadensys
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-spring.xml" })
@TransactionConfiguration(transactionManager="hibernateTransactionManager")
public class DownloadLogDAOTest  extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private DownloadLogDAO downloadLogDAO;
	
	@Test
	public void testSaveAndLoad(){
		DownloadLogModel testModel = new DownloadLogModel();
		Date now = new Date();
		testModel.setEvent_date(now);
		testModel.setSearch_criteria("this is a search");
		testModel.setEmail("a@a.com");
		testModel.setNumber_of_records(12);
		downloadLogDAO.save(testModel);
		
		int id = testModel.getId();
		
		DownloadLogModel loadedModel = downloadLogDAO.load(id);
		assertEquals("this is a search",loadedModel.getSearch_criteria());
		assertEquals("a@a.com",loadedModel.getEmail());
		assertEquals(new Integer(12),loadedModel.getNumber_of_records());
		assertEquals(now,loadedModel.getEvent_date());
	}
}
