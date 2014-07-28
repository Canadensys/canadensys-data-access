package net.canadensys.databaseutils.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

/**
 * Hibernate UserType to handle key/value coloumn type (e.g. Postgres hstore).
 * The driver must return a Java Map object when rs.getObject(column) is called on a key/value column.
 * PostgreSQL JDBC driver 9.2 and higher are returning hstore as a Map.
 * 
 * @author Jakub Gluszecki (original author)
 * @author cgendreau
 *
 */
public class KeyValueType implements UserType {

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@SuppressWarnings("unchecked")
	public Object deepCopy(Object o) throws HibernateException {
		// Not a true deep copy but since the map contains immutable Strings, it's fine.
		return new HashMap<String, String>((Map<String, String>) o);
	}

	public Serializable disassemble(Object o) throws HibernateException {
		return (Serializable) o;
	}

	@SuppressWarnings("deprecation")
	public boolean equals(Object o1, Object o2) throws HibernateException {
		// deprecated in Java 7
		return ObjectUtils.equals(o1, o2);
	}

	public int hashCode(Object o) throws HibernateException {
		return o.hashCode();
	}

	public boolean isMutable() {
		return true;
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	public Class<?> returnedClass() {
		return Map.class;
	}

	public int[] sqlTypes() {
		return new int[] { Types.OTHER };
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		String col = names[0];
		return rs.getObject(col);
	}

	@Override
	public void nullSafeSet(PreparedStatement ps, Object obj, int index, SessionImplementor session) throws HibernateException, SQLException {
		ps.setObject(index, obj, Types.OTHER);
	}

}
