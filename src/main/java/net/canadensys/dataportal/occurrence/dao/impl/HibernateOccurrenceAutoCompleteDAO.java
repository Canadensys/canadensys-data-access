package net.canadensys.dataportal.occurrence.dao.impl;

import java.io.IOException;
import java.util.List;

import net.canadensys.dataportal.occurrence.dao.OccurrenceAutoCompleteDAO;
import net.canadensys.dataportal.occurrence.model.UniqueValuesModel;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
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
	
	//this object is expensive to create so only create one and reuse it. This object
	//is thread safe after configuration.
	private ObjectMapper jacksonMapper = new ObjectMapper();
	
	/**
	 * TODO : get rid of the Wrapper
	 */
	@Override
	public String getSuggestionsFor(String field, String currValue, boolean useSanitizedValue) {
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
		List<UniqueValuesModel> suggestions = suggestionCriteria.list();
		
		Wrapper w = new Wrapper();
		w.setTotal_rows(Integer.toString(suggestions.size()));
		w.setRows(suggestions);

		try {
			return jacksonMapper.writeValueAsString(w);
		} catch (JsonGenerationException e) {
			LOGGER.fatal("getSuggestionsFor error", e);
		} catch (JsonMappingException e) {
			LOGGER.fatal("getSuggestionsFor error", e);
		} catch (IOException e) {
			LOGGER.fatal("getSuggestionsFor error", e);
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UniqueValuesModel> getAllPossibleValues(String field) {
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
	
	private class Wrapper{
		private String total_rows;
		private List<UniqueValuesModel> rows;
		
		public List<UniqueValuesModel> getRows() {
			return rows;
		}
		public void setRows(List<UniqueValuesModel> rows) {
			this.rows = rows;
		}
		public String getTotal_rows() {
			return total_rows;
		}
		public void setTotal_rows(String total_rows) {
			this.total_rows = total_rows;
		}
	}
}
