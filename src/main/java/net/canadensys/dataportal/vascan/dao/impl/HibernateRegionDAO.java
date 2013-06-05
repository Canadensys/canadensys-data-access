package net.canadensys.dataportal.vascan.dao.impl;

import java.util.List;

import net.canadensys.dataportal.vascan.dao.RegionDAO;
import net.canadensys.dataportal.vascan.model.RegionModel;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation for accessing region data through Hibernate technology.
 * @author canadensys
 *
 */
@Repository("regionDAO")
public class HibernateRegionDAO implements RegionDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RegionModel> loadAllRegion() {
		Criteria searchCriteria = sessionFactory.getCurrentSession().createCriteria(RegionModel.class);
		return (List<RegionModel>)searchCriteria.list();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
