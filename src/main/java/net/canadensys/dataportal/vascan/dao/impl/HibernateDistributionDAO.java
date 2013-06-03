package net.canadensys.dataportal.vascan.dao.impl;

import java.util.List;

import net.canadensys.dataportal.vascan.dao.DistributionDAO;
import net.canadensys.dataportal.vascan.model.DistributionModel;
import net.canadensys.dataportal.vascan.model.DistributionStatusModel;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation for handling distribution related models through Hibernate technology.
 * @author canadensys
 *
 */
@Repository("distributionDAO")
public class HibernateDistributionDAO implements DistributionDAO{
		
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<DistributionModel> loadTaxonDistribution(Integer taxonId) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(DistributionModel.class);
		searchCriteria.createAlias("taxon", "t");
		searchCriteria.add(Restrictions.eq("t.id", taxonId));
		return (List<DistributionModel>)searchCriteria.list();
	}
	
	@Override
	public List<DistributionStatusModel> loadAllDistributionStatus(){
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(DistributionStatusModel.class);
		return (List<DistributionStatusModel>)searchCriteria.list();
	}
	
	/**
	 * Compute the distribution for a taxon. To generate the complete distribution (inclugin the parents) call this methos using the same computedMap 
	 * with the parent taxonId.
	 * @param taxonId
	 * @param computedMap map <regionId,distributionstatusid>. The distributionstatusid will be changed if the current on is higher than the one for this taxon.
	 * @return
	 */
//	public Map<Integer,Integer> getComputedDistribution(Integer taxonId, Map<Integer,Integer> computedMap){
//		Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT regionid, distributionstatusid FROM distribution WHERE distribution.taxonid =:id").addScalar("regionid",IntegerType.INSTANCE).addScalar("distributionstatusid",IntegerType.INSTANCE);
//		query.setParameter("id",taxonId);
//		List<Object[]> distributions = query.list();
//		
//		for(Object[] distribution : distributions){
//			int regionid = (Integer)distribution[0];
//			int statusid = (Integer)distribution[1];
//			if(computedMap.containsKey(regionid)){
//				if(computedMap.get(regionid) > statusid)
//					computedMap.put(regionid,statusid);
//			}
//			else{
//				computedMap.put(regionid,statusid);
//			}
//		}
//		return computedMap;
//	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
