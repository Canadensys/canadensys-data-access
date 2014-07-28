package net.canadensys.dataportal.occurrence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import net.canadensys.dataportal.occurrence.model.OccurrenceExtensionModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-spring.xml" })
@TransactionConfiguration(transactionManager="hibernateTransactionManager")
public class OccurrenceExtensionDAOTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private OccurrenceExtensionDAO occurrenceExtDAO;
	
    @Before
    public void setup(){	
		//make sure the table is empty
		jdbcTemplate.update("DELETE FROM occurrence_extension");
    	jdbcTemplate.update("INSERT INTO occurrence_extension (id,ext_type,ext_data) VALUES (1,'image', toKeyValue('image_type=>png','author=>darwin','licence=>cc0'))");
    }
	
	@Test
	public void testSaveAndLoad(){
		
		OccurrenceExtensionModel occExtModel = new OccurrenceExtensionModel();
		occExtModel.setId(2);
		occExtModel.setExt_type("image");
		Map<String,String> data = new HashMap<String, String>();
		data.put("licence", "cc0");
		occExtModel.setExt_data(data);
		
		assertTrue(occurrenceExtDAO.save(occExtModel));
		
		//reload the model
		OccurrenceExtensionModel extModel = occurrenceExtDAO.load(1);
		assertEquals("cc0",extModel.getExt_data().get("licence"));
	}

}
