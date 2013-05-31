package net.canadensys.dataportal.vascan.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.canadensys.databaseutils.ScrollableResultsIteratorWrapper;
import net.canadensys.dataportal.vascan.dao.TaxonDAO;
import net.canadensys.dataportal.vascan.model.TaxonLookupModel;
import net.canadensys.dataportal.vascan.model.TaxonModel;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation for handling TaxonModel and TaxonLookupModel through Hibernate technology.
 * @author canadensys
 *
 */
@Repository("taxonDAO")
public class HibernateTaxonDAO implements TaxonDAO{

	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateTaxonDAO.class);
	private static final String MANAGED_ID = "taxonId";
	
	//includes Greenland and St-Pierre Miquelon
	//TODO should not be in that class
	public static final String ALL_PROVINCES[] = {"AB","BC","GL","NL_N","NL_L","MB","NB","NT","NS","NU","ON","PE","QC","PM","SK","YT"};
	public static final String CANADA_PROVINCES[] = {"AB","BC","NL_N","NL_L","MB","NB","NT","NS","NU","ON","PE","QC","SK","YT"};
	public static final int STATUS_ACCEPTED = 1;
	
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
	public TaxonLookupModel loadTaxonLookup(Integer taxonId) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonLookupModel.class);
		searchCriteria.add(Restrictions.eq(MANAGED_ID, taxonId));
		return (TaxonLookupModel)searchCriteria.uniqueResult();
	}

	@Override
	public TaxonModel loadTaxon(Integer taxonId) {
		try{
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonModel.class);
			searchCriteria.add(Restrictions.eq("id", taxonId));
			return (TaxonModel)searchCriteria.uniqueResult();
		}
		catch(HibernateException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaxonModel> loadTaxonByName(String taxonCalculatedName) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonModel.class).createCriteria("lookup").add(Restrictions.like("calname", taxonCalculatedName));
		return (List<TaxonModel>)searchCriteria.list();
	}
	

	/**
	 * TODO this function needs a complete rewrite to use Criteria
	 */
	public Iterator<TaxonLookupModel> loadTaxonLookup(int limitResultsTo, String combination, String habitus, int taxonid, String[] province, String[] status, String[] rank, boolean includeHybrids, String sort){

		Session hibernateSession = sessionFactory.getCurrentSession();

		String tables = "lookup";
		String sortJoin = "1";
		
		// don't go any further if no provinces & status
		// Make sure this request is looking for something
		if((status == null || province == null) && taxonid == 0 && habitus.equals("all")){
			return null;
		}
		
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT lookup.*");

		sql.append("\nFROM ").append(tables);
		sql.append("\nWHERE ").append(sortJoin);
		
		// filter by hierarchy of children for IN clause
		if(taxonid > 0){
			StringBuffer in = new StringBuffer(taxonid);
			taxonClause(taxonid,in);
			sql.append("\nAND lookup.taxonid IN ( ").append(taxonid).append(in.toString()).append(")");
		}
		
		// filter by status in provinces
		if(status != null && province != null && combination != null)
			sql.append("\nAND ").append(provinceClause(combination, status, province));
		
		
		// filter by habitus
		if(habitus != null && habitus!="" && !habitus.equals("all"))
			sql.append("\nAND ").append(habitusClause(habitus));
		
		// filter by rank
		if(rank != null){
			sql.append("\nAND ").append(rankClause(rank));
		}
		
		// filter by hybrids
		if(!includeHybrids)
			sql.append("\nAND ").append(hybridClause());
		
		// order
		sql.append("\n").append(sortClause(sort));
		
		// limit
		if(limitResultsTo > 0 )
			sql.append("\n").append(limitClause(limitResultsTo));

		SQLQuery sqlQuery = null;
		try {
			sqlQuery = hibernateSession.createSQLQuery(sql.toString()).addEntity(TaxonLookupModel.class);
		} catch (HibernateException e) {
			e.printStackTrace();
		}		
		
		return new ScrollableResultsIteratorWrapper<TaxonLookupModel>(sqlQuery.scroll(ScrollMode.FORWARD_ONLY),hibernateSession);
	}
	
	/**
	 * TODO this function needs a complete rewrite to use Criteria
	 */
	public Integer countTaxonLookup(String combination, String habitus, int taxonid, String[] province, String[] status, String[] rank, boolean includeHybrids, String sort){
		Session hibernateSession = sessionFactory.getCurrentSession();

		String tables = "lookup";
		String sortJoin = "1";
		
		// don't go any further if no provinces & status
		// Make sure this request is looking for something
		if((status == null || province == null) && taxonid == 0 && habitus.equals("all")){
			return null;
		}
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT count(lookup.taxonid)");

		sql.append("\nFROM ").append(tables);
		sql.append("\nWHERE ").append(sortJoin);
		
		// filter by hierarchy of children for IN clause
		if(taxonid > 0){
			StringBuffer in = new StringBuffer(taxonid);
			taxonClause(taxonid,in);
			sql.append("\nAND lookup.taxonid IN ( ").append(taxonid).append(in.toString()).append(")");
		}
		
		// filter by status in provinces
		if(status != null && province != null && combination != null)
			sql.append("\nAND ").append(provinceClause(combination, status, province));
		
		
		// filter by habitus
		if(habitus != null && habitus!="" && !habitus.equals("all"))
			sql.append("\nAND ").append(habitusClause(habitus));
		
		// filter by rank
		if(rank != null){
			sql.append("\nAND ").append(rankClause(rank));
		}
		
		// filter by hybrids
		if(!includeHybrids)
			sql.append("\nAND ").append(hybridClause());
		
		// order
		sql.append("\n").append(sortClause(sort));
		
		SQLQuery sqlQuery = null;
		try {
			sqlQuery = hibernateSession.createSQLQuery(sql.toString());
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		
		return ((BigInteger)sqlQuery.uniqueResult()).intValue();
		
	}
	
	public List<Object[]> getAcceptedTaxon(int maximumRank){
		org.hibernate.Session hibernateSession =sessionFactory.getCurrentSession();
		Query query = hibernateSession.createSQLQuery("SELECT taxon.id, lookup.calname, lookup.rank FROM taxon,lookup WHERE taxon.id = lookup.taxonid AND taxon.rankid <= :rankid AND taxon.statusid = :statusid  ORDER BY lookup.calname ASC")
				.addScalar("id",IntegerType.INSTANCE)
				.addScalar("calname",StringType.INSTANCE)
				.addScalar("rank",StringType.INSTANCE)
				.setParameter("rankid", maximumRank)
				.setParameter("statusid", STATUS_ACCEPTED);
		return (List<Object[]>)query.list();
	}
	
	private static String limitClause(int limitResultsTo){
		String clause = "";
		clause = "LIMIT ".concat(String.valueOf(limitResultsTo));
		return clause;
	}
	
	/**
	 * TODO Issue 753
	 * @param sort
	 * @return
	 */
	private static String sortClause(String sort){
		String clause = "";
		if(sort != null){
			if(sort.equals("alphabetically")){
				clause = "ORDER BY calname ASC";
			}
			else if(sort.equals("taxonomically")){
				clause = "";
			}
			else{
				
			}
		}
		return clause;
	}
	
	/**
	 * This method is used to exclude hybrids
	 * @return
	 */
	private static String hybridClause(){
		String clause = "lookup.calname NOT LIKE '%×%'";// not x character, × = &times;
		return clause;
	}

	
	/**
	 * 
	 * fill "in" StringBuffer from sql statement : where taxonid IN (133,455,553,1,43)
	 * 
	 * @param taxonid
	 * @param in
	 */
	private void taxonClause(int taxonid, StringBuffer in){
		Session hibernateSession = sessionFactory.getCurrentSession();
		SQLQuery query;
		try {
			query = hibernateSession.createSQLQuery("SELECT childid FROM taxonomy,taxon WHERE parentid = " + taxonid + " AND taxonomy.childid = taxon.id AND taxon.statusid = " + STATUS_ACCEPTED);
			query.setParameter(0, taxonid);
			
			ScrollableResults sr = query.scroll(ScrollMode.FORWARD_ONLY);
			
			if(sr != null){
				sr.beforeFirst();
				while(sr.next()){
					int id = sr.getInteger(0); //0=childid
					in.append(",").append(String.valueOf(id));
					taxonClause(id,in);
				}
				sr.close();
				sr = null;
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * TODO change to Criteria
	 * @param rank a validated rank array
	 * @return
	 */
	private String rankClause(String[] rank){
		StringBuffer buffer = new StringBuffer("");
		ArrayList<String> clause = new ArrayList<String>();

		for(String r : rank){
			buffer = new StringBuffer(" (");
			clause.add("lookup.rank = '" + r.toLowerCase() + "'");
		}
		Iterator it = clause.iterator();
        if (it.hasNext()) {
            buffer.append(it.next());
            while (it.hasNext()) {
            	buffer.append(" OR ");
                buffer.append(it.next());
            }
        }	
        buffer.append(") ");
		return buffer.toString();
	}
	
	/**
	 * TODO change to Criteria
	 * @param habitus a validated habitus
	 * @return
	 */
	private String habitusClause(String habitus){
		String clause = "lookup.calhabit like '%" + habitus + "%'";
		return clause;
	}
	
	/**
	 * Build sql statement to filter on provinces and statuses; String[] status,
	 * String[] province & String combination must not be null.
	 * 
	 * depending on the combination value (allof, anyof, only, only_ca), the sql
	 * operator will change from AND to OR to match groupings of provinces and
	 * statuses. The only / only_ca combination will also append exclusion sql
	 * statements to exlude statuses from provinces
	 * 
	 * ex. (allof), (native,ephemere), (qc,on): 
	 * AND (p_ON = 'native' OR p_ON = 'ephemere') AND (p_QC = 'native' OR p_QC = 'ephemere') ...
	 * 
	 * ex. (anyof), (native,ephemere), (qc,on): 
	 * AND (p_ON = 'native' OR p_ON = 'ephemere' OR p_QC = 'native' OR p_QC = 'ephemere') ...
	 * 
	 * ex. (only), (native,ephemere), (qc,on):
	 * AND (p_ON = 'native' OR p_ON = 'ephemere') OR (p_QC = 'native' OR p_QC = 'ephemere') 
	 * AND p_BC <> 'native' AND p_BC <> 'ephemere' AND p_PM <> 'native' AND p_PM <> 'ephemere' [and so on for all provinces & territories] ... 
	 * 
	 * ex. (only_ca), (native,ephemere), (qc,on):
	 * AND (p_ON = 'native' OR p_ON = 'ephemere') OR (p_QC = 'native' OR p_QC = 'ephemere') 
	 * AND p_BC <> 'native' AND p_BC <> 'ephemere' AND p_PM <> 'native' AND p_MB <> 'ephemere' [and so on for all provinces & territories, except p_PM & p_GL] ...
	 * 
	 * 
	 * @param combination
	 * @param status
	 * @param province
	 * @return a string representation of the sql statement AND ...
	 */
	private static String provinceClause(String combination, String[] status, String[] province){
		StringBuffer buffer = new StringBuffer("");
		Iterator it;
		ArrayList<String> clauses = new ArrayList<String>();
		ArrayList<String> clause = new ArrayList<String>();
		String op = "";
		
		// all available provinces & territories 
		String provinces[] = ALL_PROVINCES;
		
		// set sql operator based on type of combination 
		if(combination.equals("allof"))
			op = " AND ";
		else if(combination.equals("anyof"))
			op = " OR ";
		else if(combination.equals("only"))
			op = " AND ";
		else{ //only_ca
			op = " AND ";
			// we are looking to query only canadian provinces, and ignore greenland and saint-pierre miquelon 
			// (may or may not have a status that must be excluded in other canadian provinces)
			provinces = CANADA_PROVINCES;
		}
		
		// INCLUSION SQL STATEMENT (province = status)
		// for every provinces checked in the checklist builder
		for(String prov : province){
			clause = new ArrayList<String>();
			buffer = new StringBuffer("(");
			
			// match against all the statuses checked in the checklist builder
			// fill an array list of clauses that will be joined into a string by an sql operator
			for(String stat: status){
				clause.add("lookup.`" + prov + "` = '" + stat +"'");
			}
			
			// for all the province = status clauses, join into a string with appropriate operator
			it = clause.iterator();
	        if (it.hasNext()) {
	            buffer.append(it.next());
	            while (it.hasNext()) {
	            	if(combination.equals("allof") && status.length == 1)
	            		buffer.append(" AND ");
	            	else
	            		buffer.append(" OR ");
	                buffer.append(it.next());
	            }
	        }
			buffer.append(")");	
			clauses.add(buffer.toString());
		}

		// EXCLUSION SQL STATEMENT (province <> status)
		if(combination.equals("only") || combination.equals("only_ca")){
			// we need to exclude every other province...
			
			ArrayList<String> notEqualClause = new ArrayList<String>();
			// for every checked status
			for(String stat: status){
				// for every province
				for(String excludeProvince : provinces){
					boolean exclude = true;
					// if province is checked in checklist builder, do not exclude
					for(String prov : province){
						if(prov.equals(excludeProvince)){
							exclude = false;
							break;
						}
					}
					// if province is not checked in builder, exclude that province for that status
					if(exclude){
						notEqualClause.add("lookup.`" + excludeProvince + "` <> '" + stat +"'");
					}
				}
			}
			
			//for all the province <> status clauses, join into a string with AND operator (p_ON <> 'native' AND p_QC <> 'native' AND p_SK <> 'native' ...)
			StringBuffer notEqualBuffer = new StringBuffer("");
			it = notEqualClause.iterator();
	        if (it.hasNext()) {
	            notEqualBuffer.append(it.next());
	            while (it.hasNext()) {
	            	notEqualBuffer.append(" AND ");
	            	notEqualBuffer.append(it.next());
	            }
	        }

	        // reuse the previous clauses arraylist to rewrite the inclusion sql statement with proper parenthesis groupings and OR operators
	        StringBuffer equalBuffer = new StringBuffer("(");
	        it = clauses.iterator();
	        if (it.hasNext()) {
	            equalBuffer.append(it.next());
	            while (it.hasNext()) {
	            	equalBuffer.append(" OR ");
	            	equalBuffer.append(it.next());
	            }
	        }
	        equalBuffer.append(")");
	        
	        // rewrite clauses to add only to sql statements : the equality one where we match provinces on statuses 
	        // and the exclusion one where we exclude provinces on statuses. these two statements will be joined by an
	        // AND operator
	        clauses = new ArrayList();
	        clauses.add(equalBuffer.toString());
			clauses.add(notEqualBuffer.toString());
		}
		
		// join all sql statement clauses with sql operator
		buffer = new StringBuffer("(");
		it = clauses.iterator();
	    if (it.hasNext()) {
	        buffer.append(it.next());
	        while (it.hasNext()) {
	            buffer.append(op);
	            buffer.append(it.next());
	        }
	    }
		buffer.append(")");
		return buffer.toString();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}


