package net.canadensys.dataportal.occurrence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.canadensys.dataportal.occurrence.model.ResourceContactModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Test Coverage : 
 * -Save ResourceContactModel
 * -Get generated id
 * -Load ResourceContactModel from id
 * -Load ResourceContactModel list from datasetShortname
 * @author canadensys
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-spring.xml" })
@TransactionConfiguration(transactionManager="hibernateTransactionManager")
public class ResourceContactDAOTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private ResourceContactDAO resourceContactDAO;
	
	@Test
	public void testSaveAndLoad(){
		ResourceContactModel testModel = new ResourceContactModel();
		testModel.setName("Test Name");
		testModel.setEmail("a@a.com");
		testModel.setDataset_shortname("test-resource");
		assertTrue(resourceContactDAO.save(testModel));
		
		Integer id = testModel.getId();		
		ResourceContactModel loadedModel = resourceContactDAO.load(id);
		assertEquals("Test Name",loadedModel.getName());
		assertEquals("a@a.com",loadedModel.getEmail());
		
		List<ResourceContactModel> loadedList = resourceContactDAO.load("test-resource");
		assertEquals("Test Name",loadedList.get(0).getName());
	}
}
