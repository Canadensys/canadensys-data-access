package net.canadensys.dataportal.occurrence.dao.impl;

import net.canadensys.dataportal.occurrence.dao.OccurrenceExtensionDAO;
import net.canadensys.dataportal.occurrence.model.OccurrenceExtensionModel;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation for accessing occurrence extension data through Hibernate technology.
 * @author canadensys
 *
 */
@Repository("occurrenceExtensionDAO")
public class HibernateOccurrenceExtensionDAO implements OccurrenceExtensionDAO{
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateOccurrenceExtensionDAO.class);
	private static final String MANAGED_ID = "id";
		
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean save(OccurrenceExtensionModel occurrenceExtensionModel) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(occurrenceExtensionModel);
		}
		catch (HibernateException e) {
			LOGGER.fatal("Couldn't save OccurrenceExtensionModel", e);
			return false;
		}
		return true;
	}

	@Override
	public OccurrenceExtensionModel load(Integer id) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(OccurrenceExtensionModel.class);
		searchCriteria.add(Restrictions.eq(MANAGED_ID, id));
		return (OccurrenceExtensionModel)searchCriteria.uniqueResult();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
