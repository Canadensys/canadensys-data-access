package net.canadensys.dataportal.vascan.dao.impl;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;

import java.util.List;
import java.util.Map;

import net.canadensys.dataportal.vascan.dao.HybridDAO;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation for handling hybrids related models through Hibernate technology.
 * 
 * @author cgendreau
 *
 */
@Repository("hybridDAO")
public class HibernateHybridDAO implements HybridDAO {
	
	private static final SQLDialect DQL_DIALECT = SQLDialect.MYSQL;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> loadDenormalizedHybridParentsData(List<Integer> taxonIdList){
		
		String sqlStatement = 
			DSL.using(DQL_DIALECT)
			.renderInlined(
			select(field("thp.childid"),field("thp.parentid"),field("calnameauthor"))
			.from(table("taxonhybridparent").as("thp"), table("lookup"))
			.where(field("childid").in(taxonIdList)
			.and(field("lookup.taxonid").eq(field("thp.parentid")))));
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlStatement)
				.addScalar(DD_TAXON_ID, IntegerType.INSTANCE)
				.addScalar(DD_PARENT_ID, IntegerType.INSTANCE)
				.addScalar(DD_PARENT_CALNAME_AUTHOR, StringType.INSTANCE);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		return query.list();
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
