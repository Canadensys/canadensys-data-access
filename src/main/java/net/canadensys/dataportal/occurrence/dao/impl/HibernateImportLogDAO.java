package net.canadensys.dataportal.occurrence.dao.impl;

import net.canadensys.dataportal.occurrence.dao.ImportLogDAO;
import net.canadensys.dataportal.occurrence.model.ImportLogModel;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("importLogDAO")
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
	
	/**
	 * Last ImportLogModel is defined as the one with the highest id.
	 * It assumes records were inserted normally and that the sequence was used.
	 * @return ImportLogModel or null if nothing is found
	 */
	@Override
	public ImportLogModel loadLast(){
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ImportLogModel.class);
		c.addOrder(Order.desc(MANAGED_ID));
		c.setMaxResults(1);
		return (ImportLogModel)c.uniqueResult();
	}
	
	@Override
	public ImportLogModel loadLastFrom(String sourceFileId){
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ImportLogModel.class);
		c.add(Restrictions.eq("sourcefileid", sourceFileId));
		c.addOrder(Order.desc(MANAGED_ID));
		c.setMaxResults(1);
		return (ImportLogModel)c.uniqueResult();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
