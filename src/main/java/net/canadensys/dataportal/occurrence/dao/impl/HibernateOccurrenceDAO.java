package net.canadensys.dataportal.occurrence.dao.impl;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import net.canadensys.databaseutils.PostgresUtils;
import net.canadensys.databaseutils.ScrollableResultsIteratorWrapper;
import net.canadensys.dataportal.occurrence.dao.OccurrenceDAO;
import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.query.LimitedResult;
import net.canadensys.query.OrderByEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchQueryPartUtils;
import net.canadensys.query.interpreter.QueryPartInterpreter;
import net.canadensys.query.interpreter.QueryPartInterpreterResolver;
import net.canadensys.query.sort.SearchSortPart;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.ScrollMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation for accessing occurrence data through Hibernate technology.
 * TODO setter for max result
 * @author canadensys
 *
 */
@Repository("occurrenceDAO")
public class HibernateOccurrenceDAO implements OccurrenceDAO {
	private static final String OCCURRENCE_TABLE = OccurrenceModel.class.getAnnotation(Table.class).name();
	private static final String MANAGED_ID = "auto_id";
	
	//natural id
	private static final String SOURCE_FILE_ID = "sourcefileid";
	private static final String DWCA_ID = "dwcaid";
	private static final String RAW_MODEL = "rawModel";
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateOccurrenceDAO.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	//this object is expensive to create so only create one and reuse it. This object
	//is thread safe after configuration.
	private ObjectMapper jacksonMapper = new ObjectMapper();
	
	@Override
	public OccurrenceModel load(int auto_id){
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(OccurrenceModel.class);
		searchCriteria.add(Restrictions.eq(MANAGED_ID, auto_id));
		return (OccurrenceModel)searchCriteria.uniqueResult();
	}
	
	@Override
	public OccurrenceModel load(String sourceFileId, String dwcaId, boolean deepLoad){
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(OccurrenceModel.class);
		searchCriteria.add(Restrictions.eq(SOURCE_FILE_ID, sourceFileId));
		searchCriteria.add(Restrictions.eq(DWCA_ID, dwcaId));
		
		if(deepLoad){
			searchCriteria.setFetchMode(RAW_MODEL, FetchMode.JOIN);
		}
		return (OccurrenceModel)searchCriteria.uniqueResult();
	}
	
	@Override
	public OccurrenceModel loadOccurrenceSummary(int auto_id, List<String> columnList){
		Criteria occCriteria = sessionFactory.getCurrentSession().createCriteria(OccurrenceModel.class);
		occCriteria.add(Restrictions.eq(MANAGED_ID, auto_id));
		if(columnList != null && !columnList.isEmpty()){
			ProjectionList projectionsList = Projections.projectionList();
			for(String fieldName : columnList){
				projectionsList.add(Projections.property(fieldName),fieldName);
			}
			occCriteria.setProjection(projectionsList);
		}
		occCriteria.setResultTransformer(Transformers.aliasToBean(OccurrenceModel.class));
		return (OccurrenceModel)occCriteria.uniqueResult();
	}
	
	@Override
	public List<OccurrenceModel> search(OccurrenceModel searchCriteria, Integer limit){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OccurrenceModel.class)
				.add( Example.create(searchCriteria));

		if(limit != null){
			criteria.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<OccurrenceModel> results = criteria.list();
		return results;
	}
			
	@Override
	public List<OccurrenceModel> search(
			HashMap<String, List<String>> searchCriteriaMap) {
		
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(OccurrenceModel.class)
				.setMaxResults(10);
		
		Iterator<String> fieldIt = searchCriteriaMap.keySet().iterator();
		String fieldName;
		List<String> valueList;
		while(fieldIt.hasNext()){
			fieldName = fieldIt.next();
			valueList = searchCriteriaMap.get(fieldName);
			for(String value : valueList){
				searchCriteria.add(Restrictions.eq(fieldName, value));
			}
		}
		
		@SuppressWarnings("unchecked")    
		List<OccurrenceModel> occurrenceList = searchCriteria.list();
		return occurrenceList;
	}
	
	@Override
	public Iterator<OccurrenceModel> searchIterator(Map<String, List<SearchQueryPart>> searchCriteriaMap){
		Session session = sessionFactory.getCurrentSession();
		Criteria searchCriteria = session.createCriteria(OccurrenceModel.class);
		
		//put searchCriteriaMap into the Criteria object
		fillCriteria(searchCriteria,searchCriteriaMap);
		searchCriteria.setFetchSize(DEFAULT_FLUSH_LIMIT);  // experiment with this to optimize performance vs. memory
		return new ScrollableResultsIteratorWrapper<OccurrenceModel>(searchCriteria.scroll(ScrollMode.FORWARD_ONLY),session);
	}
	
	/**
	 * This will preload the rawModel 
	 * TODO : find a way to load the OccurrenceRawModel only
	 * Doc in Hibernate scroll : http://docs.jboss.org/hibernate/orm/4.1/devguide/en-US/html_single/#d5e986
	 * @param searchCriteriaMap
	 * @param orderBy (optional) descending order property
	 * @return
	 */
	public Iterator<OccurrenceModel> searchIteratorRaw(Map<String, List<SearchQueryPart>> searchCriteriaMap, String orderBy){
		Session session = sessionFactory.getCurrentSession();
		Criteria searchCriteria = session.createCriteria(OccurrenceModel.class,"occ");
		searchCriteria.setFetchMode("rawModel", FetchMode.JOIN);
		
		//would be nice to add a projection but the projection will remove the FetchMode.JOIN
//		ProjectionList projectionsList = Projections.projectionList();
//		projectionsList.add(Projections.property("rawModel"));
//		searchCriteria.setProjection(projectionsList);
		
		//put searchCriteriaMap into the Criteria object
		fillCriteria(searchCriteria,searchCriteriaMap);
		searchCriteria.setFetchSize(DEFAULT_FLUSH_LIMIT);
		searchCriteria.setCacheMode(CacheMode.IGNORE);
		if(orderBy != null){
			//we always use desc, maybe we should let the caller decide
			searchCriteria.addOrder(Order.desc(orderBy));
		}		
		return new ScrollableResultsIteratorWrapper<OccurrenceModel>(searchCriteria.scroll(ScrollMode.FORWARD_ONLY),session);
	}

	@Override
	public LimitedResult<List<Map<String, String>>> searchWithLimit(
			Map<String, List<SearchQueryPart>> searchCriteriaMap, List<String> columnList) {
		return searchWithLimit(searchCriteriaMap, columnList, null);
	}
	
	@Override
	public LimitedResult<List<Map<String, String>>> searchWithLimit(
			Map<String, List<SearchQueryPart>> searchCriteriaMap, List<String> columnList, SearchSortPart sorting) {
		
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(OccurrenceModel.class)
				.setMaxResults(DEFAULT_LIMIT);
		
		fillCriteria(searchCriteria,searchCriteriaMap);
		
		//First, count all rows
		searchCriteria.setProjection(Projections.count(MANAGED_ID));
		Long total_rows = (Long)searchCriteria.uniqueResult();
		
		//Then set our projection, paging, order by ...
		if(sorting != null){
			handleSorting(searchCriteria, sorting, DEFAULT_LIMIT);
		}

		ProjectionList projectionsList = Projections.projectionList();
		for(String fieldName : columnList){
			projectionsList.add(Projections.property(fieldName));
		}
		searchCriteria.setProjection(projectionsList);
		
		@SuppressWarnings("unchecked")    
		List<Object[]> occurrenceList = searchCriteria.list();
		List<Map<String, String>> searchResult = new ArrayList<Map<String,String>>(occurrenceList.size());
		
		//Put the result into a hashmap
		for(Object[] row : occurrenceList){
			HashMap<String,String> hm = new HashMap<String,String>();
			for(int i=0;i<columnList.size();i++){
				hm.put(columnList.get(i),ObjectUtils.toString(row[i]));
			}
			searchResult.add(hm);
		}
		
		LimitedResult<List<Map<String, String>>> qr = new LimitedResult<List<Map<String, String>>>();
		qr.setRows(searchResult);
		qr.setTotal_rows(total_rows);
		return qr;
	}
	
	@Override
	public int getOccurrenceCount(Map<String, List<SearchQueryPart>> searchCriteriaMap) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(OccurrenceModel.class);
		searchCriteria.setProjection(Projections.count(MANAGED_ID));
		//put searchCriteriaMap into the Criteria object
		fillCriteria(searchCriteria,searchCriteriaMap);
		
		Long total_rows = (Long)searchCriteria.uniqueResult();
		return total_rows.intValue();
	}
	
	@Override
	public List<String> getDistinctInstitutionCode(Map<String,List<SearchQueryPart>> searchCriteriaMap){
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(OccurrenceModel.class);
		searchCriteria.setProjection(Projections.distinct( Projections.projectionList()
				.add( Projections.property("institutioncode"), "institutioncode")));
		//put searchCriteriaMap into the Criteria object
		fillCriteria(searchCriteria,searchCriteriaMap);
		
		@SuppressWarnings("unchecked")
		List<String> distinctIntitutionCode = searchCriteria.list();
		return distinctIntitutionCode;
	}
	
	/**
	 * Optimized for PostgreSQL
	 */
	@Override
	public Integer getCountDistinct(Map<String,List<SearchQueryPart>> searchCriteriaMap, String column){
		String whereClause = SearchQueryPartUtils.searchCriteriaToSQLWhereClause(searchCriteriaMap);
		String sqlQuery = PostgresUtils.getOptimizedCountDistinctQuery(column, whereClause, OCCURRENCE_TABLE,"cu");
		Integer count = (Integer)sessionFactory.getCurrentSession().createSQLQuery(sqlQuery).addScalar("cu", IntegerType.INSTANCE).uniqueResult();
		return count;
	}

	@Override
	public String getOccurrenceSummaryJson(int auto_id, String idColumnName, List<String> columnList) {
		
		Criteria occCriteria = sessionFactory.getCurrentSession().createCriteria(OccurrenceModel.class);
		occCriteria.add(Restrictions.eq(idColumnName, auto_id));
		ProjectionList projectionsList = Projections.projectionList();
		for(String fieldName : columnList){
			projectionsList.add(Projections.property(fieldName));
		}
		occCriteria.setProjection(projectionsList);
		
		@SuppressWarnings("unchecked")
		List<Object[]> occurrence = occCriteria.list();
		String json = "";
		///Json
		if(!occurrence.isEmpty()){
			Object[] row = occurrence.get(0);
			Map<String, Object> occurrenceInMap = new HashMap<String, Object>();
			
			for(int i=0;i<columnList.size();i++){
				occurrenceInMap.put(columnList.get(i), row[i]);
			}
			try {
				json = jacksonMapper.writeValueAsString(occurrenceInMap);
			} catch (JsonGenerationException e) {
				LOGGER.error("JSON generation error", e);
			} catch (JsonMappingException e) {
				LOGGER.error("JSON generation error", e);
			} catch (IOException e) {
				LOGGER.error("JSON generation error", e);
			}
		}
		else{
			LOGGER.error("Occurrence auto_id " + auto_id + " not found");
		}
		return json;
	}
	
	@Override
	public List<AbstractMap.SimpleImmutableEntry<String,Integer>> getValueCount(Map<String, List<SearchQueryPart>> searchCriteriaMap, String column, Integer max){
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(OccurrenceModel.class);
		List<SimpleImmutableEntry<String,Integer>> countList = new ArrayList<SimpleImmutableEntry<String,Integer>>();
		searchCriteria.setProjection(Projections.projectionList()
				.add(Projections.alias(Projections.rowCount(),"rowCount"))
				.add(Projections.groupProperty(column)));
		searchCriteria.add(Restrictions.isNotNull(column));
		searchCriteria.addOrder(Order.desc("rowCount"));
		if(max != null){
			searchCriteria.setMaxResults(max);
		}
		//put searchCriteriaMap into the Criteria object
		fillCriteria(searchCriteria,searchCriteriaMap);
		
		@SuppressWarnings("unchecked")
		List<Object[]> count = searchCriteria.list();
		SimpleImmutableEntry<String,Integer> entry;
		for(Object[] row : count){
			entry = new SimpleImmutableEntry<String, Integer>(row[1].toString(), ((Long)row[0]).intValue());
			countList.add(entry);
		}
		return countList;
	}
	
	/**
	 * Fills the Criteria object from the criteria in searchCriteriaMap.
	 * All different fields from the searchCriteriaMap key will be separated with an AND and
	 * all different values in the list will be separated with an OR.
	 * @param citeria
	 * @param searchCriteriaMap
	 * @return same instance of Criteria, could be used in method chaining
	 */
	protected Criteria fillCriteria(Criteria citeria, Map<String, List<SearchQueryPart>> searchCriteriaMap){
		Iterator<String> fieldIt = searchCriteriaMap.keySet().iterator();
		Criterion fieldCriterion = null;
		List<SearchQueryPart> queryPartList;
		String fieldName;
		
		while(fieldIt.hasNext()){
			fieldName = fieldIt.next();
			queryPartList = searchCriteriaMap.get(fieldName);
			for(SearchQueryPart qp : queryPartList){
				if(fieldCriterion == null){
					fieldCriterion = convertIntoCriterion(qp);
				}
				else{ //separate different values with an OR statement
					fieldCriterion = Restrictions.or(fieldCriterion, convertIntoCriterion(qp));
				}
			}
			//skip invalid criterion
			if(fieldCriterion != null){
				citeria.add(fieldCriterion);
			}
			fieldCriterion = null;
		}
		return citeria;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Converts a SearchQueryPart into a Hibernate Criterion
	 * @param queryPart  a valid SearchQueryPart object
	 * @return Criterion representing this SearchQueryPart or null if no Criterion can be created
	 */
	private Criterion convertIntoCriterion(SearchQueryPart queryPart){
		QueryPartInterpreter intepreter = QueryPartInterpreterResolver.getQueryPartInterpreter(queryPart);
		if(intepreter == null){
			LOGGER.fatal("No interpreter found for " + queryPart);
			return null;
		}
		return intepreter.toCriterion(queryPart);
	}
	
	/**
	 * Handle paging and ORDER BY clause together.
	 * The main reason is that paging requires a stable ordering.
	 * @param searchCriteria
	 * @param sorting
	 * @param pageSize
	 */
	private void handleSorting(Criteria searchCriteria, SearchSortPart sorting, Integer pageSize){
		
		Integer pageNumber = sorting.getPageNumber();
		String orderByColumn = sorting.getOrderByColumn();
		OrderByEnum direction = sorting.getOrderBy();
		
		//if no paging and no ordering, there is nothing to do.
		if(pageNumber == null && (StringUtils.isBlank(orderByColumn) || direction == null)){
			return;
		}
		
		//if we ask for paging, ensure the order by clause for consitent results
		if(pageNumber != null && (StringUtils.isBlank(orderByColumn) || direction == null)){
			//paging requires order by
			if(StringUtils.isBlank(orderByColumn)){
				orderByColumn = MANAGED_ID;
			}
			if(direction == null){
				direction = OrderByEnum.ASC;
			}
		}
		
		//Handle paging
		if(pageNumber != null && pageNumber.intValue() > 0){
			if(pageSize == null){
				pageSize = DEFAULT_LIMIT;
			}
			searchCriteria.setFirstResult(pageNumber*pageSize);
		}
		
		switch(direction){
			case ASC: searchCriteria.addOrder(Order.asc(orderByColumn));
			break;
			case DESC: searchCriteria.addOrder(Order.desc(orderByColumn));
			break;
			default: throw new IllegalArgumentException("Direction must be ASC or DESC");
		}
	}
}
