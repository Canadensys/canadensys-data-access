package net.canadensys.dataportal.occurrence.dao.impl;

import net.canadensys.dataportal.occurrence.dao.DownloadLogDAO;
import net.canadensys.dataportal.occurrence.model.DownloadLogModel;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation for handling DownloadLogModel through Hibernate technology.
 * @author canadensys
 *
 */
@Repository("downloadDAO")
public class HibernateDownloadLogDAO implements DownloadLogDAO {
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateDownloadLogDAO.class);
	
	private static final String MANAGED_ID = "id";
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean save(DownloadLogModel downloadLogModel) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(downloadLogModel);
		}
		catch (HibernateException e) {
			LOGGER.fatal("Could'n save DownloadLogModel", e);
			return false;
		}
		return true;
	}

	@Override
	public DownloadLogModel load(Integer id) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(DownloadLogModel.class);
		searchCriteria.add(Restrictions.eq(MANAGED_ID, id));
		return (DownloadLogModel)searchCriteria.uniqueResult();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
