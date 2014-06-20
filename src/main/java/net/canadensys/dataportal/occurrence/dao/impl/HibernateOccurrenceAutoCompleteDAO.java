package net.canadensys.dataportal.occurrence.dao.impl;

import java.util.List;

import net.canadensys.dataportal.occurrence.dao.OccurrenceAutoCompleteDAO;
import net.canadensys.dataportal.occurrence.model.UniqueValuesModel;
import net.canadensys.model.SuggestedValue;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation for accessing UniqueValuesModel through Hibernate technology.
 * @author canadensys
 *
 */
@Repository("occDAO")
public class HibernateOccurrenceAutoCompleteDAO implements OccurrenceAutoCompleteDAO{
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(HibernateOccurrenceAutoCompleteDAO.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<SuggestedValue> getSuggestionsFor(String field, String currValue, boolean useSanitizedValue) {
		Criteria suggestionCriteria = sessionFactory.getCurrentSession().createCriteria(UniqueValuesModel.class)
			    .add(Restrictions.eq("key", field))
			    .addOrder(Order.desc("occurrence_count"))
			    .setMaxResults(10);
		
		if(currValue != null){
			if(useSanitizedValue){
				suggestionCriteria.add(Restrictions.like("unaccented_value", currValue+"%"));
			}
			else{
				suggestionCriteria.add(Restrictions.like("value", currValue+"%"));
			}
		}
			    
		@SuppressWarnings("unchecked")    
		List<SuggestedValue> suggestions = suggestionCriteria.list();
		
		return suggestions;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SuggestedValue> getAllPossibleValues(String field) {
		Criteria suggestionCriteria = sessionFactory.getCurrentSession().createCriteria(UniqueValuesModel.class)
			    .add(Restrictions.eq("key", field))
			    .addOrder(Order.desc("occurrence_count")); 
		return suggestionCriteria.list();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
