package net.canadensys.dataportal.occurrence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.canadensys.dataportal.occurrence.dao.OccurrenceDAO;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceRawModel;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.TestSearchableFieldBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Test Coverage : 
 * -OneToOne relationship
 * -Lazy loading
 * @author canadensys
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-spring.xml" })
@TransactionConfiguration(transactionManager="hibernateTransactionManager")
public class OccurrenceModelAndRawTest  extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private OccurrenceDAO occurrenceDAO;
		
	private JdbcTemplate jdbcTemplate;
	
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Before
    public void setup(){
		//make sure the table is empty
		jdbcTemplate.update("DELETE FROM occurrence");
		//add controlled rows
		jdbcTemplate.update("INSERT INTO occurrence_raw (auto_id,country,locality,sourcefileid) VALUES (1,'Mexico','Mexicco','uom-occurrence')");
    	jdbcTemplate.update("INSERT INTO occurrence (auto_id,country,locality,sourcefileid) VALUES (1,'Mexico','Mexico','uom-occurrence')");
    }
    
    @Test
    public void testLazyLoading(){
		OccurrenceModel occModel = occurrenceDAO.load(1);
		
		Field rawModelField;
		Field rawModelLocalityField;
		try {
			//get the data by reflection to bypass the Hibernate proxy
			rawModelField = OccurrenceModel.class.getDeclaredField("rawModel");
			rawModelField.setAccessible(true);
			OccurrenceRawModel occRawModel= (OccurrenceRawModel)rawModelField.get(occModel);
			
			rawModelLocalityField = OccurrenceRawModel.class.getDeclaredField("locality");
			rawModelLocalityField.setAccessible(true);
			Object noProxyLocality = rawModelLocalityField.get(occRawModel);
			//is lazy loading is working the field should be null until we call a get on the object
			assertNull(noProxyLocality);
			
			//normal access through getter should load the object
			assertEquals("Mexicco",occModel.getRawModel().getLocality());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testRawModelSearchIterator(){	
    	SearchQueryPart sqpCountryMexico = new SearchQueryPart();
		sqpCountryMexico.setSearchableField(TestSearchableFieldBuilder.buildSingleValueSearchableField(1,"country","country"));
		sqpCountryMexico.setOp(QueryOperatorEnum.EQ);
		sqpCountryMexico.addValue("Mexico");
		sqpCountryMexico.addParsedValue("Mexico", "country", "Mexico");
    	
		Map<String,List<SearchQueryPart>> searchCriteria = new HashMap<String, List<SearchQueryPart>>();
		List<SearchQueryPart> queryPartListCountry = new ArrayList<SearchQueryPart>();
		queryPartListCountry.add(sqpCountryMexico);
		searchCriteria.put("country", queryPartListCountry);

		Iterator<OccurrenceModel> it = occurrenceDAO.searchIteratorRaw(searchCriteria,null);
		OccurrenceRawModel rawModel = it.next().getRawModel();
		assertEquals("Mexicco",rawModel.getLocality());
    }
}
