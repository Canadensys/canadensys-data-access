package net.canadensys.dataportal.occurrence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.query.LimitedResult;
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
 * -Search one one field, search with an OR, search with an AND
 * -Search with iterator
 * -Search from a model
 * -Summary of a OccurrenceModel
 * -Search with no result
 * -Value and unique value count
 * @author canadensys
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-spring.xml" })
@TransactionConfiguration(transactionManager="hibernateTransactionManager")
public class OccurrenceDAOTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private OccurrenceDAO occurrenceDAO;
	
	private JdbcTemplate jdbcTemplate;
	
	private List<String> columnList;
	private SearchQueryPart sqpCountryMexico;
	private SearchQueryPart sqpCountrySweden;
	private SearchQueryPart sqpCountryILikeSweden;
	private SearchQueryPart sqpCountryUSA;
	private SearchQueryPart sqpMunicipalityUppsala;
	
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Before
    public void setup(){
    	columnList = new ArrayList<String>();
    	columnList.add("auto_id");
		columnList.add("country");
		//make sure the table is empty
		jdbcTemplate.update("DELETE FROM occurrence");
		//add controlled rows
    	jdbcTemplate.update("INSERT INTO occurrence (auto_id,country,locality,sourcefileid,institutioncode) VALUES (1,'Mexico','Mexico','uom-occurrence','MT')");
		jdbcTemplate.update("INSERT INTO occurrence (auto_id,country,locality,sourcefileid,institutioncode) VALUES (2,'Sweden','Stockholm','uos-occurrence','MT')");
		jdbcTemplate.update("INSERT INTO occurrence (auto_id,country,locality,sourcefileid,institutioncode) VALUES (3,'Sweden','Uppsala','uou-occurrence','UBC')");
		jdbcTemplate.update("INSERT INTO occurrence (auto_id,country,locality,sourcefileid,institutioncode) VALUES (4,'United States','Mexico','uow-occurrence','UBC')");
		
		sqpCountryMexico = new SearchQueryPart();
		sqpCountryMexico.setSearchableField(TestSearchableFieldBuilder.buildSingleValueSearchableField(1,"country","country"));
		sqpCountryMexico.setOp(QueryOperatorEnum.EQ);
		sqpCountryMexico.addValue("Mexico");
		sqpCountryMexico.addParsedValue("Mexico", "country", "Mexico");
		
		sqpCountrySweden = new SearchQueryPart();
		sqpCountrySweden.setSearchableField(TestSearchableFieldBuilder.buildSingleValueSearchableField(1,"country","country"));
		sqpCountrySweden.setOp(QueryOperatorEnum.EQ);
		sqpCountrySweden.addValue("Sweden");
		sqpCountrySweden.addParsedValue("Sweden", "country", "Sweden");
		
		sqpCountryILikeSweden = new SearchQueryPart();
		sqpCountryILikeSweden.setSearchableField(TestSearchableFieldBuilder.buildSingleValueSearchableField(1,"country","country"));
		sqpCountryILikeSweden.setOp(QueryOperatorEnum.SLIKE);
		sqpCountryILikeSweden.addValue("swede");
		
		sqpCountryUSA = new SearchQueryPart();
		sqpCountryUSA.setSearchableField(TestSearchableFieldBuilder.buildSingleValueSearchableField(1,"country","country"));
		sqpCountryUSA.setOp(QueryOperatorEnum.EQ);
		sqpCountryUSA.addValue("United States");
		sqpCountryUSA.addParsedValue("United States", "country", "United States");
		
		sqpMunicipalityUppsala = new SearchQueryPart();
		sqpMunicipalityUppsala.setSearchableField(TestSearchableFieldBuilder.buildSingleValueSearchableField(2,"locality","locality"));
		sqpMunicipalityUppsala.setOp(QueryOperatorEnum.EQ);
		sqpMunicipalityUppsala.addValue("Uppsala");
		sqpMunicipalityUppsala.addParsedValue("Uppsala", "locality", "Uppsala");
    }
	
	@Test
	public void testSimpleSearch(){
		Map<String,List<SearchQueryPart>> searchCriteria = new HashMap<String, List<SearchQueryPart>>();
		List<SearchQueryPart> queryPartList = new ArrayList<SearchQueryPart>();
		queryPartList.add(sqpCountrySweden);
		searchCriteria.put("country", queryPartList);
		
		//test searchWithLimit on one criteria
		LimitedResult<List<Map<String,String>>> result = occurrenceDAO.searchWithLimit(searchCriteria, columnList);
		assertEquals(2, result.getTotal_rows());
	}
	
	/**
	 * Make sure we get only 1 row when asking for Sweden AND Uppsala
	 */
	@Test
	public void testSearchWithANDOperator(){
		Map<String,List<SearchQueryPart>> searchCriteria = new HashMap<String, List<SearchQueryPart>>();
		List<SearchQueryPart> queryPartListCountry = new ArrayList<SearchQueryPart>();
		queryPartListCountry.add(sqpCountrySweden);
		searchCriteria.put("country", queryPartListCountry);
		
		List<SearchQueryPart> queryPartListMunicipality = new ArrayList<SearchQueryPart>();
		queryPartListMunicipality.add(sqpMunicipalityUppsala);
		searchCriteria.put("locality", queryPartListMunicipality);
		
		//test searchWithLimit on one criteria
		LimitedResult<List<Map<String,String>>> result = occurrenceDAO.searchWithLimit(searchCriteria, columnList);
		assertEquals(1, result.getRows().size());
		assertEquals("3", result.getRows().get(0).get("auto_id"));
	}
	
	/**
	 * Make sure we get 2 rows when asking for Mexico OR United States
	 */
	@Test
	public void testSearchWithOROperator(){
		Map<String,List<SearchQueryPart>> searchCriteria = new HashMap<String, List<SearchQueryPart>>();
		List<SearchQueryPart> queryPartListCountry = new ArrayList<SearchQueryPart>();
		queryPartListCountry.add(sqpCountryMexico);
		queryPartListCountry.add(sqpCountryUSA);
		searchCriteria.put("country", queryPartListCountry);
		
		//test searchWithLimit on one criteria
		LimitedResult<List<Map<String,String>>> result = occurrenceDAO.searchWithLimit(searchCriteria, columnList);
		assertEquals(2, result.getRows().size());

		String id1 = result.getRows().get(0).get("auto_id");
		String id2 = result.getRows().get(1).get("auto_id");
		//the order is not important
		assertTrue(id1.equals("1") && id2.equals("4") || 
				id1.equals("4") && id2.equals("1"));
	}
	
	/**
	 * Test JSON representation, null column should be ignored
	 */
	@Test
	public void testSearchJSON(){
		//get the auto_id since this is the PK
		int auto_id = jdbcTemplate.queryForInt("SELECT auto_id from occurrence where auto_id = 1");
		ArrayList<String> columnList = new ArrayList<String>();
		columnList.add("auto_id");
		columnList.add("country");
		columnList.add("locality");
		columnList.add("sourcefileid");
		String json = occurrenceDAO.getOccurrenceSummaryJson(auto_id, "auto_id", columnList);
		
		//check the validity by "contains" since the order is not guaranteed
		assertTrue(json.contains("\"auto_id\":1"));
		assertTrue(json.contains("\"country\":\"Mexico\""));
		assertTrue(json.contains("\"locality\":\"Mexico\""));
		assertTrue(json.contains("\"sourcefileid\":\"uom-occurrence\""));
		
		assertTrue(json.startsWith("{"));
		assertTrue(json.endsWith("}"));
	}
	
	@Test
	public void testLoadOccurrenceSummary(){
		ArrayList<String> columnList = new ArrayList<String>();
		columnList.add("auto_id");
		columnList.add("country");
		columnList.add("locality");
		columnList.add("sourcefileid");
		
		OccurrenceModel occModel = occurrenceDAO.loadOccurrenceSummary(1, columnList);
		
		//this column is in the database but was not specified in column list
		assertNull(occModel.getInstitutioncode());
		
		assertEquals("Mexico",occModel.getCountry());
		assertEquals("Mexico",occModel.getLocality());
		assertEquals("uom-occurrence", occModel.getSourcefileid());
	}
	
	/**
	 * Test iterator feature of the Occurrence DAO
	 */
	@Test
	public void testSearchIterator(){
		
		Map<String,List<SearchQueryPart>> searchCriteria = new HashMap<String, List<SearchQueryPart>>();
		List<SearchQueryPart> queryPartListCountry = new ArrayList<SearchQueryPart>();
		queryPartListCountry.add(sqpCountryMexico);
		queryPartListCountry.add(sqpCountryUSA);
		searchCriteria.put("country", queryPartListCountry);

		Iterator<OccurrenceModel> it = occurrenceDAO.searchIterator(searchCriteria);
		List<Integer> ids = new ArrayList<Integer>();
		while(it.hasNext()){
			ids.add(it.next().getAuto_id());
		}
		
		assertEquals(2, ids.size());
		assertTrue(ids.contains(1));
		assertTrue(ids.contains(4));
		
		//try another way to iterate
		it = occurrenceDAO.searchIterator(searchCriteria);
		ids.clear();
		OccurrenceModel curr = it.next();
		while(curr != null){
			ids.add(curr.getAuto_id());
			curr = it.next();
		}
		
		assertEquals(2, ids.size());
		assertTrue(ids.contains(1));
		assertTrue(ids.contains(4));
	}
	
	@Test
	public void testSearchNoResult(){
		Map<String,List<SearchQueryPart>> searchCriteria = new HashMap<String, List<SearchQueryPart>>();
		List<SearchQueryPart> queryPartList = new ArrayList<SearchQueryPart>();
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildSingleValueSearchableField(1,"country","country"));
		sqp.setOp(QueryOperatorEnum.EQ);
		sqp.addValue("Kashyyyk");
		queryPartList.add(sqp);
		searchCriteria.put("country", queryPartList);
		
		//test searchWithLimit on one criteria
		LimitedResult<List<Map<String,String>>> result = occurrenceDAO.searchWithLimit(searchCriteria, columnList);
		assertEquals(0, result.getTotal_rows());
		//make sure we have an empty list
		assertEquals(0, result.getRows().size());
	}
	
	@Test
	public void testSearchFromModel(){
		OccurrenceModel model = new OccurrenceModel();
		model.setLocality("Stockholm");
		
		List<OccurrenceModel> result = occurrenceDAO.search(model, null);
		assertEquals(1, result.size());
		assertEquals(2, result.get(0).getAuto_id());
	}
	
	@Test
	public void testGetDistinctInstitutionCode(){
		Map<String,List<SearchQueryPart>> searchCriteria = new HashMap<String, List<SearchQueryPart>>();
		List<SearchQueryPart> queryPartList = new ArrayList<SearchQueryPart>();
		queryPartList.add(sqpCountrySweden);
		searchCriteria.put("country", queryPartList);

		List<String> result = occurrenceDAO.getDistinctInstitutionCode(searchCriteria);
		assertEquals(2, result.size());
		assertTrue(result.contains("MT"));
		assertTrue(result.contains("UBC"));
	}
	
	@Test
	public void testColumnValueCount(){
		Map<String,List<SearchQueryPart>> searchCriteria = new HashMap<String, List<SearchQueryPart>>();
		List<SearchQueryPart> queryPartList = new ArrayList<SearchQueryPart>();
		queryPartList.add(sqpCountrySweden);
		searchCriteria.put("country", queryPartList);
		
		List<AbstractMap.SimpleImmutableEntry<String,Integer>> result = occurrenceDAO.getValueCount(searchCriteria,"locality",10);
		assertEquals(2, result.size());
	}
	
	@Test
	public void testColumnUniqueValueCount(){
		Map<String,List<SearchQueryPart>> searchCriteria = new HashMap<String, List<SearchQueryPart>>();
		List<SearchQueryPart> queryPartList = new ArrayList<SearchQueryPart>();
		queryPartList.add(sqpCountrySweden);
		searchCriteria.put("country", queryPartList);
		
		Integer count = occurrenceDAO.getCountDistinct(searchCriteria,"locality");
		assertEquals(2, count.intValue());
	}

}
