package net.canadensys.dataportal.occurrence.dao.impl;

import net.canadensys.dataportal.occurrence.dao.ImportLogDAO;
import net.canadensys.dataportal.occurrence.model.ImportLogModel;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateImportLogDAO implements ImportLogDAO{
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateImportLogDAO.class);
	
	private static final String MANAGED_ID = "id";
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean save(ImportLogModel importLogModel) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(importLogModel);
		}
		catch (HibernateException e) {
			LOGGER.fatal("Couldn't save ImportLogModel", e);
			return false;
		}
		return true;
	}

	@Override
	public ImportLogModel load(Integer id) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(ImportLogModel.class);
		searchCriteria.add(Restrictions.eq(MANAGED_ID, id));
		return (ImportLogModel)searchCriteria.uniqueResult();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
