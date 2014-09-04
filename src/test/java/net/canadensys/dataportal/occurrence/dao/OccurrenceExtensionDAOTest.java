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

/**
 * This test is using a 'stub' hstore since hstore is PostgreSQL specific.
 * The trick to use it with h2 is available in src/test/resources/h2/h2setup.sql
 * Test Coverage : 
 * -Insert extension data using jdbcTemplate
 * -Save OccurrenceExtensionModel
 * -Load OccurrenceExtensionModel from id
 * @author canadensys
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-spring.xml" })
@TransactionConfiguration(transactionManager = "hibernateTransactionManager")
public class OccurrenceExtensionDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private OccurrenceExtensionDAO occurrenceExtDAO;

	@Before
	public void setup() {
		// make sure the table is empty
		jdbcTemplate.update("DELETE FROM occurrence_extension");
		jdbcTemplate
				.update("INSERT INTO occurrence_extension (auto_id,dwcaid,sourcefileid,ext_type,ext_data) VALUES (1,'1','source','image', toKeyValue('image_type=>png','author=>darwin','licence=>cc0'))");
	}

	@Test
	public void testSaveAndLoad() {

		OccurrenceExtensionModel occExtModel = new OccurrenceExtensionModel();
		occExtModel.setAuto_id(2l);
		occExtModel.setDwcaid("2");
		occExtModel.setSourcefileid("source");
		occExtModel.setExt_type("image");
		Map<String, String> data = new HashMap<String, String>();
		data.put("licence", "cc-by");
		occExtModel.setExt_data(data);

		assertTrue(occurrenceExtDAO.save(occExtModel));

		// reload the model
		OccurrenceExtensionModel extModel = occurrenceExtDAO.load(2l);
		assertEquals("cc-by", extModel.getExt_data().get("licence"));
	}

}
