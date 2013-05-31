package net.canadensys.dataportal.vascan.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.canadensys.dataportal.vascan.dao.TaxonomyDAO;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("taxonomyDAO")
public class HibernateTaxonomyDAO implements TaxonomyDAO {

	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateTaxonomyDAO.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
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
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


}
