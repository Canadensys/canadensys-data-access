package net.canadensys.dataportal.vascan.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import net.canadensys.dataportal.vascan.dao.TaxonomyDAO;
import net.canadensys.dataportal.vascan.model.TaxonLookupModel;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation for handling taxonomy using taxonomy table(tree) or lookup table(nested sets) through Hibernate technology.
 * @author canadensys
 *
 */
@Repository("taxonomyDAO")
public class HibernateTaxonomyDAO implements TaxonomyDAO {

	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateTaxonomyDAO.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAcceptedChildrenIdList(Integer taxonId){
		Session hibernateSession = sessionFactory.getCurrentSession();
		Query query = hibernateSession.createSQLQuery("SELECT taxonomy.childid FROM taxonomy,taxon WHERE taxonomy.childid = taxon.id AND taxonomy.parentid = :id AND taxon.statusid = :statusid");
		query.setParameter("id", taxonId);
		query.setParameter("statusid", HibernateTaxonDAO.STATUS_ACCEPTED);
		return query.list();
	}
	
	@Override
	public Set<Integer> getChildrenIdSet(Integer taxonId, boolean recursive) {
		LinkedHashSet<Integer> childrenIdSet = new LinkedHashSet<Integer>();
		internalGetChildrenIdSet(taxonId,recursive,childrenIdSet);
		return childrenIdSet;
	}
	
	/**
	 * Internal getChildrenIdSet function to allow recursion without exposing it.
	 * TODO : why order by rank ASC?
	 * @param taxonId
	 * @param recursive
	 * @param childrenIdSet
	 */
	@SuppressWarnings("unchecked")
	protected void internalGetChildrenIdSet(Integer taxonId, boolean recursive,LinkedHashSet<Integer> childrenIdSet){
		Session hibernateSession = sessionFactory.getCurrentSession();
		Query query = hibernateSession.createSQLQuery("SELECT taxonomy.childid FROM taxonomy, taxon, rank WHERE taxonomy.childid = taxon.id AND taxonomy.parentid = :id AND taxon.rankid = rank.id ORDER BY rank.sort ASC");
		query.setParameter("id", taxonId);
		List<Integer> acceptedIds = query.list();
		for(Integer acceptedId : acceptedIds){
			childrenIdSet.add(acceptedId);
			if(recursive){
				internalGetChildrenIdSet(acceptedId,recursive,childrenIdSet);
			}
		}
	}
	
	@Override
	public void buildNestedSets(Integer taxonId){
		internalBuildNestedSets(taxonId,new AtomicInteger(0),0);
	}
	
	/**
	 * Internal implementation of nested sets building.
	 * Using direct SQL to avoid loading the entire object to update the left and right values only.
	 * See : http://www.evanpetersen.com/item/nested-sets.html
	 */
	private void internalBuildNestedSets(Integer taxonId, AtomicInteger currCounter, Integer depth){
		List<Integer> childrenIdSet = getAcceptedChildrenIdList(taxonId);

		//set left value
		int left = currCounter.intValue();
		
		//increment since this value was attributed
		currCounter.incrementAndGet();
		for(Integer currChildId : childrenIdSet){
			depth++;
			internalBuildNestedSets(currChildId,currCounter,depth);
			depth--;
		}
		
		//set right value
		int right = currCounter.intValue();
		//increment since this value was attributed
		currCounter.incrementAndGet();
		
		//Update the taxon table
		SQLQuery updateCmd = sessionFactory.getCurrentSession().createSQLQuery("UPDATE lookup SET _left = :left, _right = :right WHERE taxonid = :id");
		updateCmd.setParameter("left", left);
		updateCmd.setParameter("right", right);
		updateCmd.setParameter("id", taxonId);
		updateCmd.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAcceptedChildrenIdListFromNestedSets(Integer taxonId){
		Session hibernateSession = sessionFactory.getCurrentSession();
		SQLQuery query = hibernateSession.createSQLQuery("SELECT taxonid FROM lookup as child, (SELECT _left,_right FROM lookup where taxonid = :id) as parent " +
				"WHERE child._left > parent._left AND child._right < parent._right");
		query.addScalar("taxonid",IntegerType.INSTANCE);
		query.setParameter("id", taxonId);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaxonLookupModel> getAcceptedChildrenListFromNestedSets(Integer taxonId, String[] acceptedRanks){
		Session hibernateSession = sessionFactory.getCurrentSession();
		SQLQuery query = hibernateSession.createSQLQuery("SELECT * FROM lookup as child, (SELECT _left,_right FROM lookup where taxonid = :id) as parent " +
				"WHERE child._left > parent._left AND child._right < parent._right AND child.rank IN (:ranks) ORDER BY child._left");
		query.addEntity(TaxonLookupModel.class);
		query.setParameter("id", taxonId);
		query.setParameterList("ranks", acceptedRanks);
		return query.list();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
