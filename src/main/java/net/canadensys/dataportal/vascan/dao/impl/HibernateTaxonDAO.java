package net.canadensys.dataportal.vascan.dao.impl;

import java.util.List;

import net.canadensys.dataportal.vascan.dao.TaxonDAO;
import net.canadensys.dataportal.vascan.model.TaxonLookupModel;
import net.canadensys.dataportal.vascan.model.TaxonModel;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation for handling TaxonModel and TaxonLookupModel through Hibernate technology.
 * @author canadensys
 *
 */
@Repository("taxonDAO")
public class HibernateTaxonDAO implements TaxonDAO{

	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateTaxonDAO.class);
	private static final String MANAGED_ID = "taxonId";
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public boolean saveTaxonLookup(TaxonLookupModel tlm) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(tlm);
		}
		catch (HibernateException e) {
			LOGGER.fatal("Couldn't save TaxonLookupModel", e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public TaxonLookupModel loadTaxonLookup(Integer taxonId) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonLookupModel.class);
		searchCriteria.add(Restrictions.eq(MANAGED_ID, taxonId));
		return (TaxonLookupModel)searchCriteria.uniqueResult();
	}

	@Override
	public TaxonModel loadTaxon(Integer taxonId) {
		try{
			Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonModel.class);
			searchCriteria.add(Restrictions.eq("id", taxonId));
			return (TaxonModel)searchCriteria.uniqueResult();
		}
		catch(HibernateException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaxonModel> loadTaxonByName(String taxonCalculatedName) {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(TaxonModel.class).createCriteria("lookup").add(Restrictions.like("calname", taxonCalculatedName));
		return (List<TaxonModel>)searchCriteria.list();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}


