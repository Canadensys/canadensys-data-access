package net.canadensys.dataportal.vascan.dao.impl;

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.canadensys.databaseutils.SQLHelper;
import net.canadensys.databaseutils.ScrollableResultsIteratorWrapper;
import net.canadensys.dataportal.vascan.dao.TaxonDAO;
import net.canadensys.dataportal.vascan.dao.query.RegionQueryPart;
import net.canadensys.dataportal.vascan.model.TaxonLookupModel;
import net.canadensys.dataportal.vascan.model.TaxonModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.CalendarType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation for handling TaxonModel and TaxonLookupModel through Hibernate technology.
 * @author canadensys
 *
 */
@Repository("taxonDAO")
public class HibernateTaxonDAO implements TaxonDAO {
	
	private enum RegionCriterionOperatorEnum{OR,AND};

	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateTaxonDAO.class);
	private static final String TAXON_MANAGED_ID = "id";
	private static final String TAXON_LOOKUP_MANAGED_ID = "taxonId";
	
	//TODO should not be in that class
	//includes Greenland and St-Pierre Miquelon
	public static final String ALL_PROVINCES[] = {"AB","BC","GL","NL_N","NL_L","MB","NB","NT","NS","NU","ON","PE","QC","PM","SK","YT"};
	public static final String CANADA_PROVINCES[] = {"AB","BC","NL_N","NL_L","MB","NB","NT","NS","NU","ON","PE","QC","SK","YT"};
	public static final int STATUS_ACCEPTED = 1;
	public static final int STATUS_SYNONYM = 2;
	
	//jOOQ
	private static final SQLDialect SQL_DIALECT = SQLDialect.MYSQL;
	private static final Table<Record> LOOKUP_TABLE = table("lookup");
	
	//predefined SQL
	//database independant order by clause using nested sets with NULL last
	//(in mysql it's cleaner to use ISNULL(lookup._left))
	private static final String TAXONOMIC_ORDERBY = "ORDER BY COALESCE(lookup._left," + Integer.MAX_VALUE + "), lookup._left ASC";
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public boolean saveTaxonLookup(TaxonLookupModel tlm) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(tlm);
		}
		catch (HibernateException e) {
			LOGGER.fatal("Couldn't save TaxonLookupModel", e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean deleteTaxon(Integer taxonId){
		TaxonModel taxonModelToDelete = loadTaxon(taxonId, false);
		if(taxonModelToDelete != null){
			Session hibernateSession = sessionFactory.getCurrentSession();
			//check if some data are pointing on us
			
			//check if this taxon is a hybrid parent
			Query query = hibernateSession.createSQLQuery("SELECT count(childid)noc FROM taxonhybridparent WHERE parentid = :taxonid")
				.addScalar("noc",BigIntegerType.INSTANCE)
				.setParameter("taxonid", taxonId);
			BigInteger count = (BigInteger)query.uniqueResult();
			if(count.intValue() > 0){
				LOGGER.error("Could not delete taxonID :" + taxonId + ". This taxon is used as hybrid parent.");
				return false;
			}
			
			//check if this taxon is a parent in taxonomy
			query = hibernateSession.createSQLQuery("SELECT count(childid)noc FROM taxonomy WHERE parentid = :taxonid")
				.addScalar("noc",BigIntegerType.INSTANCE)
				.setParameter("taxonid", taxonId);
			count = (BigInteger)query.uniqueResult();
			if(count.intValue() > 0){
				LOGGER.error("Could not delete taxonID :" + taxonId + ". This taxon is used as parent in taxonomy.");
				return false;
			}
			
			hibernateSession.delete(taxonModelToDelete);
			hibernateSession.flush();
		 }
		 else{
			 LOGGER.error("Could not delete taxonID :" + taxonId + ". This taxon doesn't exist.");
			 return false;
		 }
		return true;
	}

	@Override
	public TaxonLookupModel loadTaxonLookup(Integer taxonId) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonLookupModel.class);
		searchCriteria.add(Restrictions.eq(TAXON_LOOKUP_MANAGED_ID, taxonId));
		return (TaxonLookupModel)searchCriteria.uniqueResult();
	}

	@Override
	public TaxonModel loadTaxon(Integer taxonId, boolean deepLoad) {
		try{
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonModel.class);
			searchCriteria.add(Restrictions.eq("id", taxonId));
			if(deepLoad){
				searchCriteria.setFetchMode("vernacularnames", FetchMode.JOIN);
			}
			return (TaxonModel)searchCriteria.uniqueResult();
		}
		catch(HibernateException e){
			LOGGER.error("Couldn't load taxon " + taxonId, e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaxonModel> loadTaxonList(List<Integer> taxonIdList){
		try{
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonModel.class);
			searchCriteria.add(Restrictions.in("id", taxonIdList));
			return (List<TaxonModel>)searchCriteria.list();
		}
		catch(HibernateException e){
			LOGGER.error("Couldn't load taxon list", e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaxonModel> loadTaxonByName(String taxonCalculatedName) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonModel.class).createCriteria("lookup").add(Restrictions.like("calname", taxonCalculatedName));
		return (List<TaxonModel>)searchCriteria.list();
	}
	
//	@Override
//	public Iterator<TaxonModel> searchIterator(int limitResultsTo, String habitus, Integer taxonid, RegionQueryPart rqp, String[] status,
//			String[] rank, boolean includeHybrids, String sort){
//		
//		// don't go any further if no region & status
//		// Make sure this request is looking for something
//		if((status == null || rqp == null || rqp.getRegion() == null) && (taxonid == null || taxonid.intValue() == 0) && habitus !=null && habitus.equals("all")){
//			return null;
//		}
//		
//		List<Criterion> lookupCriterionList = new ArrayList<Criterion>();
//		
//		// filter by status in region
//		if(status != null && rqp != null && rqp.getRegion() != null && rqp.getRegionSelector() != null){
//			lookupCriterionList.add(getStatusRegionCriterion(rqp, status));
//		}
//		
//		// filter by habitus
//		if(habitus != null && habitus!="" && !habitus.equals("all")){
//			lookupCriterionList.add(getHabitCriterion(habitus));
//		}
//		
//		// filter by rank
//		if(rank != null){
//			lookupCriterionList.add(getRankCriterion(rank));
//		}
//		
//		// filter by hybrids
//		if(!includeHybrids){
//			lookupCriterionList.add(getExcludeHybridCriterion());
//		}
//		
//		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonModel.class)
//				.createAlias("lookup", "lkp", JoinType.INNER_JOIN, Restrictions.and(lookupCriterionList.toArray(new Criterion[0])) );
//		
//		if(taxonid !=null && taxonid > 0){
//			searchCriteria.add(getTaxonCriterion(taxonid,TAXON_MANAGED_ID));
//		}
//		
//		// order
//		addOrderBy(searchCriteria, sort,"lkp.calname");
//		
//		// limit
//		if(limitResultsTo > 0 ){
//			addLimitClause(searchCriteria, limitResultsTo);
//		}
//		
//		return new ScrollableResultsIteratorWrapper<TaxonModel>(searchCriteria.scroll(ScrollMode.FORWARD_ONLY), sessionFactory.getCurrentSession());
//	}
	
	@Override
	public Iterator<Map<String,Object>> searchIteratorDenormalized(int limitResultsTo, String habitus, Integer taxonid, RegionQueryPart rqp, String[] status,
			String[] rank, boolean includeHybrids, String sort){
		
		// don't go any further if no region & status
		// Make sure this request is looking for something
		if((status == null || rqp == null || rqp.getRegion() == null) && (taxonid == null || taxonid.intValue() == 0) && habitus !=null && habitus.equals("all")){
			return null;
		}
		
		Condition searchCondition = null;
		String orderBySQL = null;
		
		// filter by status in region
		if(status != null && rqp != null && rqp.getRegion() != null && rqp.getRegionSelector() != null){
			searchCondition = safeAnd(searchCondition, getStatusRegionCondition(rqp, status));
		}
		
		// filter by habitus
		if(habitus != null && habitus!="" && !habitus.equals("all")){
			searchCondition = safeAnd(searchCondition, getHabitCondition(habitus));
		}
		
		// filter by rank
		if(rank != null){
			searchCondition = safeAnd(searchCondition, getRankCondition("lookup.rank", rank));
		}
		
		// filter by hybrids
		if(!includeHybrids){
			searchCondition = safeAnd(searchCondition, getExcludeHybridCondition("lookup.calname"));
		}
		
		// filter by taxonid
		if(taxonid !=null && taxonid > 0){
			searchCondition = safeAnd(searchCondition, condition(getTaxonConditionSQL(taxonid,"taxon."+TAXON_MANAGED_ID)));
		}
		
		orderBySQL = getOrderBy(sort,"lookup.calname");

		Query searchQuery = buildSearchDenormalizedQuery(sessionFactory.getCurrentSession(), searchCondition, orderBySQL);
		searchQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		// limit
		if(limitResultsTo > 0 ){
			searchQuery.setMaxResults(limitResultsTo);
		}
		
		return new ScrollableResultsIteratorWrapper<Map<String,Object>>(searchQuery.scroll(ScrollMode.FORWARD_ONLY), sessionFactory.getCurrentSession());
	}
	
	/**
	 * Build a search Query object using SQL to get decent performance.
	 * @param hibernateSession
	 * @param whereClause
	 * @return
	 */
	private Query buildSearchDenormalizedQuery(Session hibernateSession, Condition whereClause, String orderBySQL ){
		return hibernateSession.createSQLQuery("SELECT taxon.id, taxon.mdate, lookup.status, GROUP_CONCAT(taxonomy.parentid) concatParentId,"+
			"reference.url, reference.reference, lookup.calnameauthor,taxon.author,lookup.rank,"+
			"GROUP_CONCAT(pLookup.calnameauthor) AS concatParentCalNameAuthor, lookup.higherclassification,"+
			"lookup.class, lookup._order,lookup.family,lookup.genus,"+
			"lookup.subgenus,lookup.specificepithet,lookup.infraspecificepithet,lookup.calhabit,taxon.statusid "+
			"FROM taxon "+
			"INNER JOIN lookup ON taxon.id = lookup.taxonid "+
			"INNER JOIN reference ON taxon.referenceid = reference.id "+
			"LEFT JOIN taxonomy ON taxonomy.childid = taxon.id "+
			"LEFT JOIN lookup pLookup ON pLookup.taxonid = taxonomy.parentid "+
			"WHERE " + DSL.using(SQL_DIALECT).renderInlined(whereClause) + 
			" GROUP BY taxon.id " +
			StringUtils.defaultString(orderBySQL))
			    .addScalar("id", IntegerType.INSTANCE)
				.addScalar("mdate", CalendarType.INSTANCE)
				.addScalar("status", StringType.INSTANCE)
				.addScalar(DD_CONCAT_PARENT_ID, StringType.INSTANCE)
				.addScalar("url", StringType.INSTANCE)
				.addScalar("reference", StringType.INSTANCE)
				.addScalar("calnameauthor", StringType.INSTANCE)
				.addScalar("author", StringType.INSTANCE)
				.addScalar("rank", StringType.INSTANCE)
				.addScalar(DD_CONCAT_PARENT_CALNAME_AUTHOR, StringType.INSTANCE)
				.addScalar("higherclassification", StringType.INSTANCE)
				.addScalar("class", StringType.INSTANCE)
				.addScalar("_order", StringType.INSTANCE)
				.addScalar("family", StringType.INSTANCE)
				.addScalar("genus", StringType.INSTANCE)
				.addScalar("subgenus", StringType.INSTANCE)
				.addScalar("specificepithet", StringType.INSTANCE)
				.addScalar("infraspecificepithet", StringType.INSTANCE)
				.addScalar(DD_CALHABIT, StringType.INSTANCE)
				.addScalar(DD_STATUS_ID, IntegerType.INSTANCE);
	}
	
	/**
	 * @see TaxonDAO
	 */
	@Override
	public Iterator<TaxonLookupModel> loadTaxonLookup(int limitResultsTo, String habitus, int taxonid, RegionQueryPart rqp, String[] status, String[] rank, boolean includeHybrids, String sort){
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonLookupModel.class);

		// don't go any further if no region & status
		// Make sure this request is looking for something
		if((status == null || rqp == null || rqp.getRegion() == null) && taxonid == 0 && habitus.equals("all")){
			return null;
		}
		
		if(taxonid > 0){
			searchCriteria.add(getTaxonCriterion(taxonid,TAXON_LOOKUP_MANAGED_ID));
		}
		
		// filter by status in region
		if(status != null && rqp != null && rqp.getRegion() != null && rqp.getRegionSelector() != null){
			searchCriteria.add(getStatusRegionCriterion(rqp, status));
		}
		
		// filter by habitus
		if(habitus != null && habitus!="" && !habitus.equals("all")){
			searchCriteria.add(getHabitCriterion(habitus));
		}
		
		// filter by rank
		if(rank != null){
			searchCriteria.add(getRankCriterion(rank));
		}
		
		// filter by hybrids
		if(!includeHybrids){
			searchCriteria.add(getExcludeHybridCriterion());
		}
		
		// order
		addOrderBy(searchCriteria, sort, "calname");
		
		// limit
		if(limitResultsTo > 0 ){
			addLimitClause(searchCriteria, limitResultsTo);
		}
		
		return new ScrollableResultsIteratorWrapper<TaxonLookupModel>(searchCriteria.scroll(ScrollMode.FORWARD_ONLY),sessionFactory.getCurrentSession());
	}
	
	/**
	 * @see TaxonDAO
	 */
	@Override
	public Integer countTaxonLookup(String habitus, int taxonid, RegionQueryPart rqp, String[] status, String[] rank, boolean includeHybrids){
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonLookupModel.class);

		// don't go any further if no region & status
		// Make sure this request is looking for something
		if((status == null || rqp == null || rqp.getRegion() == null) && taxonid == 0 && habitus.equals("all")){
			return null;
		}
		//we only want to count
		searchCriteria.setProjection(Projections.count(TAXON_LOOKUP_MANAGED_ID));
		
		// filter for a specific taxon
		if(taxonid > 0){
			searchCriteria.add(getTaxonCriterion(taxonid,TAXON_LOOKUP_MANAGED_ID));
		}
		
		// filter by status in region
		if(status != null && rqp.getRegion() != null && rqp.getRegionSelector() != null){
			searchCriteria.add(getStatusRegionCriterion(rqp, status));
		}
		
		// filter by habitus
		if(habitus != null && habitus!="" && !habitus.equals("all")){
			searchCriteria.add(getHabitCriterion(habitus));
		}
		
		// filter by rank
		if(rank != null){
			searchCriteria.add(getRankCriterion(rank));
		}
		
		// filter by hybrids
		if(!includeHybrids){
			searchCriteria.add(getExcludeHybridCriterion());
		}

		Long total_rows = (Long)searchCriteria.uniqueResult();
		return total_rows.intValue();
	}
	
	@Override
	public List<Object[]> getAcceptedTaxon(int maximumRank){
		Session hibernateSession = sessionFactory.getCurrentSession();
		Query query = hibernateSession.createSQLQuery("SELECT taxon.id, lookup.calname, lookup.rank FROM taxon,lookup WHERE taxon.id = lookup.taxonid AND taxon.rankid <= :rankid AND taxon.statusid = :statusid  ORDER BY lookup.calname ASC")
				.addScalar("id",IntegerType.INSTANCE)
				.addScalar("calname",StringType.INSTANCE)
				.addScalar("rank",StringType.INSTANCE)
				.setParameter("rankid", maximumRank)
				.setParameter("statusid", STATUS_ACCEPTED);
		return (List<Object[]>)query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> loadDenormalizedTaxonData(List<Integer> taxonIdList){
		Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT taxon.id, taxon.mdate, lookup.status, GROUP_CONCAT(taxonomy.parentid) concatParentId, reference.url," +
			"reference.reference, lookup.calnameauthor,taxon.author,lookup.rank, GROUP_CONCAT(parentLookup.calnameauthor) AS concatParentCalNameAuthor, lookup.higherclassification, " +
			"lookup.class, lookup._order,lookup.family,lookup.genus,lookup.subgenus,lookup.specificepithet,lookup.infraspecificepithet,lookup.calhabit,taxon.statusid" +
			" FROM taxon" +
			" INNER JOIN lookup ON taxon.id = lookup.taxonid"+
			" INNER JOIN reference ON taxon.referenceid = reference.id"+
			" LEFT JOIN taxonomy ON taxonomy.childid = taxon.id"+
			" LEFT JOIN taxon parentTaxon ON taxonomy.parentid = parentTaxon.id"+
			" LEFT JOIN lookup parentLookup ON parentLookup.taxonid = parentTaxon.id"+
		    " WHERE taxon.id IN (:id)"+
		    " GROUP BY taxon.id ")
		    .addScalar(DD_ID, IntegerType.INSTANCE)
			.addScalar(DD_MDATE, CalendarType.INSTANCE)
			.addScalar(DD_STATUS, StringType.INSTANCE)
			.addScalar(DD_CONCAT_PARENT_ID, StringType.INSTANCE)
			.addScalar(DD_URL, StringType.INSTANCE)
			.addScalar(DD_REFERENCE, StringType.INSTANCE)
			.addScalar(DD_CALNAME_AUTHOR, StringType.INSTANCE)
			.addScalar(DD_AUTHOR, StringType.INSTANCE)
			.addScalar(DD_RANK, StringType.INSTANCE)
			.addScalar(DD_CONCAT_PARENT_CALNAME_AUTHOR, StringType.INSTANCE)
			.addScalar(DD_HIGHER_CLASSIFICATION, StringType.INSTANCE)
			.addScalar(DD_CLASS, StringType.INSTANCE)
			.addScalar(DD_ORDER, StringType.INSTANCE)
			.addScalar(DD_FAMILY, StringType.INSTANCE)
			.addScalar(DD_GENUS, StringType.INSTANCE)
			.addScalar(DD_SUBGENUS,StringType.INSTANCE)
			.addScalar(DD_SPECIFIC_EPITHET, StringType.INSTANCE)
			.addScalar(DD_INFRASPECIFIC_EPITHET, StringType.INSTANCE)
			.addScalar(DD_CALHABIT, StringType.INSTANCE)
			.addScalar(DD_STATUS_ID, IntegerType.INSTANCE);
		query.setParameterList("id", taxonIdList);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	/**
	 * Add a limit to the provided Criteria
	 * @param searchCriteria
	 * @param limitResultsTo
	 */
	private void addLimitClause(Criteria searchCriteria, int limitResultsTo){
		searchCriteria.setMaxResults(limitResultsTo);
	}
	
	/**
	 * Add ORDER BY clause to the provided Criteria representing the provided 'sort'.
	 * Note: taxonomic sort will put synonyms at the beginning (as opposed to  getOrderBy(String,String) method ).
	 * 
	 * @param searchCriteria
	 * @param sort
	 * @param propertyName name of the property to used (for alphabetic sort only)
	 * @return
	 */
	private void addOrderBy(Criteria searchCriteria, String sort, String propertyName){
		if(StringUtils.isNotBlank(sort)){
			if(sort.equals(SORT_ALPHABETIC)){
				searchCriteria.addOrder(Order.asc(propertyName));
			}
			else if(sort.equals(SORT_TAXONOMIC)){
				//nothing to do, it's the assumed default order. This is risky and should be fixed.
				searchCriteria.addOrder(Order.asc("_left"));
			}
			else{
				LOGGER.warn("unknown sort value:" + sort);
			}
		}
	}
	
	/**
	 * Get ORDER BY clause in SQL representing the provided 'sort'.
	 * Note: taxonomic sort will put synonyms at the end.
	 * 
	 * @param sort
	 * @param propertyName name of the property to used (for alphabetic sort only)
	 * @return sql ORDER BY clause or null
	 */
	private String getOrderBy(String sort, String propertyName){
		if(StringUtils.isNotBlank(sort)){
			if(sort.equals(SORT_ALPHABETIC)){
				return DSL.orderBy(field(propertyName).asc()).toString();
			}
			else if(sort.equals(SORT_TAXONOMIC)){
				return TAXONOMIC_ORDERBY;
			}
			else{
				LOGGER.warn("unknown sort value:" + sort);
			}
		}
		return null;
	}
	
	/**
	 * Returns a criterion to exclude hybrids
	 * @return
	 */
	private Criterion getExcludeHybridCriterion(){
		//warning, × is not the x character, × = &times;
		return Restrictions.not(Restrictions.like("calname", "×", MatchMode.ANYWHERE));
	}
	
	private Condition getExcludeHybridCondition(String scientificNameColumn){
		//warning, × is not the x character, × = &times;
		return field(scientificNameColumn).notLike("%×%");
	}
	
	
	/**
	 * 
	 * Fills the idList with the entire accepted children, recursively.
	 * 
	 * @param taxonid
	 * @param in
	 */
	private void getTaxonIdTree(int taxonid, List<Integer> idList){
		Session hibernateSession = sessionFactory.getCurrentSession();
		SQLQuery query;
		try {
			query = hibernateSession.createSQLQuery("SELECT childid FROM taxonomy,taxon WHERE parentid = :taxonid AND taxonomy.childid = taxon.id AND taxon.statusid = :statusid");
			query.setParameter("taxonid", taxonid);
			query.setParameter("statusid",STATUS_ACCEPTED);
			query.addScalar("childid",IntegerType.INSTANCE);
			
			List<Integer> result = query.list();
			if(result != null){
				for(Integer currId : result){
					idList.add(currId);
					getTaxonIdTree(currId,idList);
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a Criterion for a taxon and all its accepted children.
	 * @param taxonId
	 * @return
	 */
	private Criterion getTaxonCriterion(int taxonId, String idPropertyName){
		List<Integer> idList = new ArrayList<Integer>();
		getTaxonIdTree(taxonId, idList);
		//add the taxon itself
		idList.add(taxonId);
		return Restrictions.in(idPropertyName, idList);
	}
		
	/**
	 * Returns SQL string build from a jOOQ sub query that returns a taxon and all of its accepted children.
	 * 
	 * @param taxonId
	 * @param idPropertyName
	 * @return
	 */
	private String getTaxonConditionSQL(int taxonId, String idPropertyName){
		Table<?> leftRightOfTaxon = table(
				DSL.select(field("_left"),field("_right")).from(LOOKUP_TABLE)
				.where(field("taxonid").eq(taxonId))).as("tlf");
		
		Select<?> subSelect = DSL.using(SQL_DIALECT)
			.select(field("taxonid")).from(LOOKUP_TABLE, leftRightOfTaxon)
			.where(field("lookup._left").gt(field("tlf._left"))
			.and(field("lookup._right").lt(field("tlf._right"))));

		String sql = "SELECT " + taxonId + " UNION " +  DSL.using(SQL_DIALECT).renderInlined(subSelect);
		return SQLHelper.in(idPropertyName, sql);
	}
	
	/**
	 * Returns a Criterion for one ore more rank(s)
	 * @param rank a validated rank array
	 * @return
	 */	
	private Criterion getRankCriterion(String[] rank){
		Disjunction disjunction = Restrictions.disjunction();
		for(String r : rank){
			disjunction.add(Restrictions.eq("rank", r.toLowerCase()));
		}
		return disjunction;
	}

	/**
	 * @param alias given to rank column in the query
	 * @param rank
	 * @return
	 */
	private Condition getRankCondition(String rankColumnAlias, String[] rank){
		Condition condition = null;
		for(String r : rank){
			condition = safeOr(condition, field(rankColumnAlias).equal(r.toLowerCase()));
		}
		return condition;
	}
	
	/**
	 * Returns a Criterion for a habit against calhabit of the lookup table.
	 * Since this is a calculated field, the calhabit shall 'contains' the habit.
	 * @return
	 */
	private Criterion getHabitCriterion(String habit){
		return Restrictions.like("calhabit", habit.toLowerCase(), MatchMode.ANYWHERE);
	}
	
	private Condition getHabitCondition(String habit){
		return field("calhabit").like("%" + habit.toLowerCase() +"%");
	}
	
	/**
	 * Returns a Criterion for a combination of status and region(provinces)
	 * Combination definition : 
	 * (allof), (native,ephemere), (qc,on): 
	 * AND (p_ON = 'native' OR p_ON = 'ephemere') AND (p_QC = 'native' OR p_QC = 'ephemere') ...
	 * 
	 * (anyof), (native,ephemere), (qc,on): 
	 * AND (p_ON = 'native' OR p_ON = 'ephemere' OR p_QC = 'native' OR p_QC = 'ephemere') ...
	 * 
	 * (only), (native,ephemere), (qc,on):
	 * AND (p_ON = 'native' OR p_ON = 'ephemere') OR (p_QC = 'native' OR p_QC = 'ephemere') 
	 * AND p_BC <> 'native' AND p_BC <> 'ephemere' AND p_PM <> 'native' AND p_PM <> 'ephemere' [and so on for all provinces & territories] ... 
	 * 
	 * (all of, only in), (native,ephemere), (qc,on):
	 * AND (p_ON = 'native' OR p_ON = 'ephemere') AND (p_QC = 'native' OR p_QC = 'ephemere') 
	 * AND p_BC <> 'native' AND p_BC <> 'ephemere' AND p_PM <> 'native' AND p_MB <> 'ephemere' [and so on for all provinces & territories, except p_PM & p_GL] ...
	 * 
	 * @param rqp
	 * @param status
	 * @return
	 */
	private Criterion getStatusRegionCriterion(RegionQueryPart rqp, String[] status){
		//TaxonLookupModel uses upper case for regions
		String[] region = net.canadensys.utils.StringUtils.allUpperCase(rqp.getRegion());
		
		Criterion onlyInCriteria = null;
		switch(rqp.getRegionSelector()){
			case ALL_OF : return getAllRegionStatusCriterion(status, region, RegionCriterionOperatorEnum.AND);
			case ANY_OF : return getAnyRegionStatusCriterion(status, region);
			case ONLY_IN:
				onlyInCriteria = getAllRegionStatusCriterion(status, region,RegionCriterionOperatorEnum.OR);
				break;
			case ALL_OF_ONLY_IN:
				onlyInCriteria = getAllRegionStatusCriterion(status, region,RegionCriterionOperatorEnum.AND);
				break;
		}
		//sanity check
		if(onlyInCriteria == null){
			LOGGER.fatal("Unhandled rqp.getRegionSelector() case");
		}
		
		if(rqp.isSearchOnlyInCanada()){
			return Restrictions.and(onlyInCriteria, getExclusionCriterion(status,region,CANADA_PROVINCES));
		}
		else{
			return Restrictions.and(onlyInCriteria, getExclusionCriterion(status,region,ALL_PROVINCES));
		}
	}
	
	private Condition getStatusRegionCondition(RegionQueryPart rqp, String[] status){
		//TaxonLookupModel uses upper case for regions
		String[] region = net.canadensys.utils.StringUtils.allUpperCase(rqp.getRegion());
		
		Condition onlyInCondition = null;
		switch(rqp.getRegionSelector()){
			case ALL_OF : return getAllRegionStatusCondition(status, region, RegionCriterionOperatorEnum.AND);
			case ANY_OF : return getAnyRegionStatusCondition(status, region);
			case ONLY_IN:
				onlyInCondition = getAllRegionStatusCondition(status, region,RegionCriterionOperatorEnum.OR);
				break;
			case ALL_OF_ONLY_IN:
				onlyInCondition = getAllRegionStatusCondition(status, region,RegionCriterionOperatorEnum.AND);
				break;
		}
		//sanity check
		if(onlyInCondition == null){
			LOGGER.fatal("Unhandled rqp.getRegionSelector() case");
			return null;
		}
		
		if(rqp.isSearchOnlyInCanada()){
			return onlyInCondition.and(getExclusionCondition(status,region,CANADA_PROVINCES));
		}
		else{
			return onlyInCondition.and(getExclusionCondition(status,region,ALL_PROVINCES));
		}
	}
	
	/**
	 * Returns a Criterion to include all region with one of the status.
	 * The operation determine if the have an OR or an AND between the regions
	 * @param status
	 * @param province uppercase
	 * @param op
	 * @return
	 */
	private Criterion getAllRegionStatusCriterion(String[] status, String[] region, RegionCriterionOperatorEnum op){
		Criterion regionStatusCriterion = null;
		Disjunction disjunction = null;
		for(String prov : region){
			// match against all the statuses checked in the checklist builder
			disjunction = Restrictions.disjunction();
			for(String currStatus: status){
				disjunction.add(Restrictions.eq(prov, currStatus));
			}
			if(regionStatusCriterion == null){
				regionStatusCriterion = disjunction;
			}
			else{
				if(op == RegionCriterionOperatorEnum.AND){
					regionStatusCriterion = Restrictions.and(regionStatusCriterion, disjunction);
				}
				else if(op == RegionCriterionOperatorEnum.OR){
					regionStatusCriterion = Restrictions.or(regionStatusCriterion, disjunction);
				}
			}
		}
		return regionStatusCriterion;
	}
	
	private Condition getAllRegionStatusCondition(String[] status, String[] region, RegionCriterionOperatorEnum op){
		Condition allStatus = null;
		Condition statusByProvince = null;
		
		for(String prov : region){
			// match against all the statuses checked in the checklist builder
			statusByProvince = null;
			for(String currStatus: status){
				statusByProvince = safeOr(statusByProvince,DSL.fieldByName("lookup",prov).eq(currStatus));
			}

			if(allStatus == null){
				allStatus = statusByProvince;
			}
			else{
				if(op == RegionCriterionOperatorEnum.AND){
					allStatus = allStatus.and(statusByProvince);
				}
				else if(op == RegionCriterionOperatorEnum.OR){
					allStatus = allStatus.or(statusByProvince);
				}
			}
		}
		return allStatus;
	}
	
	/**
	 * Returns a Criterion to include any of the regions with any of the status.
	 * @param status
	 * @param province uppercase
	 * @return
	 */
	private Criterion getAnyRegionStatusCriterion(String[] status, String[] regions){
		Disjunction disjunction = Restrictions.disjunction();
		for(String currRegion : regions){
			for(String currStatus: status){
				disjunction.add(Restrictions.eq(currRegion, currStatus));
			}
		}
		return disjunction;
	}
	
	private Condition getAnyRegionStatusCondition(String[] status, String[] regions){		
		Condition anyStatusProvince = null;
		for(String currRegion : regions){
			for(String currStatus: status){
				anyStatusProvince = safeOr(anyStatusProvince, DSL.fieldByName("lookup",currRegion).eq(currStatus));
			}
		}
		return anyStatusProvince;
	}

	
	/**
	 * Returns a Criterion to exclude all regions with one of the provided statuses that are not in the provided region array.
	 * @param status
	 * @param province uppercase
	 * @param allProvinces uppercase
	 * @return
	 */
	private Criterion getExclusionCriterion(String[] status, String[] region, String[] allRegions){
		Conjunction exclusionCriterion = Restrictions.conjunction();
		for(String currRegion : allRegions){
			boolean exclude = true;
			for(String prov : region){
				if(prov.equals(currRegion)){
					exclude = false;
					break;
				}
			}
			
			if(exclude){
				exclusionCriterion.add(Restrictions.not(Restrictions.in(currRegion, status)));
			}
		}
		return exclusionCriterion;
	}
		
	private Condition getExclusionCondition(String[] status, String[] region, String[] allRegions){
		Condition exclusionCondition = null;
		for(String currRegion : allRegions){
			boolean exclude = true;
			for(String prov : region){
				if(prov.equals(currRegion)){
					exclude = false;
					break;
				}
			}
			if(exclude){
				exclusionCondition = safeAnd(exclusionCondition, DSL.fieldByName("lookup",currRegion).notIn(Arrays.asList(status)));
			}
		}
		return exclusionCondition;
	}
	
	/**
	 * JOOQ OR Condition that handles null on current.
	 * @param current
	 * @param condition
	 * @return
	 */
	private Condition safeOr(Condition current, Condition condition){
		if(current == null){
			return condition;
		}else{
			return current.or(condition);
		}
	}
	
	/**
	 * JOOQ AND Condition that handles null aon current.
	 * @param current
	 * @param condition
	 * @return
	 */
	private Condition safeAnd(Condition current, Condition condition){
		if(current == null){
			return condition;
		}else{
			return current.and(condition);
		}
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}


