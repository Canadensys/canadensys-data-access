package net.canadensys.dataportal.occurrence.dao.impl;

import java.util.List;

import net.canadensys.dataportal.occurrence.dao.ResourceDAO;
import net.canadensys.dataportal.occurrence.model.OccurrenceFieldConstants;
import net.canadensys.dataportal.occurrence.model.ResourceModel;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("resourceDAO")
public class HibernateResourceDAO implements ResourceDAO {
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateResourceDAO.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<ResourceModel> loadResources(){
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(ResourceModel.class);
		return searchCriteria.list();
	}
	
	@Override
	public boolean save(ResourceModel resourceModel) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(resourceModel);
		}
		catch(HibernateException hEx){
			LOGGER.fatal("Couldn't save ResourceModel", hEx);
			return false;
		}
		return true;
	}

	@Override
	public ResourceModel load(String sourcefileid) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(ResourceModel.class);
		searchCriteria.add(Restrictions.eq(OccurrenceFieldConstants.SOURCE_FILE_ID, sourcefileid));
		return (ResourceModel)searchCriteria.uniqueResult();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
