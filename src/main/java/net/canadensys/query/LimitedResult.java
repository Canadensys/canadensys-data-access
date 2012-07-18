/*
	Copyright (c) 2012 Canadensys
*/
package net.canadensys.query;


/**
 * This class is used to encapsulate the result of a query when a LIMIT clause is applied.
 * The total number of rows is still returned but only a limited number of those rows are returned.
 * It can be used as a Java Bean.
 * @author canadensys
 * @param <T> type of the rows structure
 */

public class LimitedResult<T> {
	private long total_rows;
	private T rows;
	
	public long getTotal_rows() {
		return total_rows;
	}
	public void setTotal_rows(long total_rows) {
		this.total_rows = total_rows;
	}
	public T getRows() {
		return rows;
	}
	public void setRows(T rows) {
		this.rows = rows;
	}
	
	@Override
	public String toString(){
		if(rows == null){
			return "total_rows:"+total_rows+",rows:null";
		}
		return "total_rows:"+total_rows+",rows:"+rows.toString();
	}
}
