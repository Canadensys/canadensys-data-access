package net.canadensys.dataportal.occurrence.dao.impl;

import java.util.List;

import net.canadensys.dataportal.occurrence.dao.ResourceContactDAO;
import net.canadensys.dataportal.occurrence.model.ResourceContactModel;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("resourceContactDAO")
public class HibernateResourceContactDAO implements ResourceContactDAO {
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateResourceContactDAO.class);
	private static final String MANAGED_ID = "id";
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean save(ResourceContactModel resourceContactModel) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(resourceContactModel);
		}
		catch (HibernateException e) {
			LOGGER.fatal("Couldn't save ResourceContactModel", e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public ResourceContactModel load(Integer id) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(ResourceContactModel.class);
		searchCriteria.add(Restrictions.eq(MANAGED_ID, id));
		return (ResourceContactModel)searchCriteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResourceContactModel> load(String sourceFileId){
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(ResourceContactModel.class);
		searchCriteria.add(Restrictions.eq("sourcefileid", sourceFileId));
		return searchCriteria.list();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
