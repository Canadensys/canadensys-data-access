package net.canadensys.dataportal.vascan.dao.impl;

import java.util.List;
import java.util.Map;

import net.canadensys.dataportal.vascan.dao.VernacularNameDAO;
import net.canadensys.dataportal.vascan.model.VernacularNameModel;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("vernacularNameDAO")
public class HibernateVernacularNameDAO implements VernacularNameDAO{
	
	//get log4j handler
	//private static final Logger LOGGER = Logger.getLogger(HibernateVernacularNameDAO.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public VernacularNameModel loadVernacularName(Integer vernacularNameId) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(VernacularNameModel.class);
		searchCriteria.add(Restrictions.eq("id", vernacularNameId));
		return (VernacularNameModel)searchCriteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<VernacularNameModel> loadVernacularNameByName(String vernacularName) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(VernacularNameModel.class);
		return (List<VernacularNameModel>) searchCriteria.add(Restrictions.eq("name", vernacularName)).list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> loadDenormalizedVernacularNameData(List<Integer> taxonIdList){
		Query query = sessionFactory.getCurrentSession().createSQLQuery("SELECT vernacularname.taxonid,vernacularname.name, reference.reference, reference.url, vernacularname.language, vernacularname.statusid" +
			" FROM vernacularname,reference " +
			" WHERE vernacularname.taxonid IN (:id) " +
			" AND reference.id = vernacularname.referenceid")
			.addScalar(DD_TAXON_ID,IntegerType.INSTANCE)
			.addScalar(DD_NAME,StringType.INSTANCE)
			.addScalar(DD_REFERENCE,StringType.INSTANCE)
			.addScalar(DD_URL,StringType.INSTANCE)
			.addScalar(DD_LANGUAGE,StringType.INSTANCE)
			.addScalar(DD_STATUS_ID,IntegerType.INSTANCE);
		query.setParameterList("id", taxonIdList);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
