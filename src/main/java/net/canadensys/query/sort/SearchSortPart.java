package net.canadensys.query.sort;

import net.canadensys.query.OrderByEnum;

/**
 * SearchSortPart represents the sorting part of a search query and is not tied to a specific technology.
 * The main usage is to translate a sort options of a search query received via URL parameters.
 * @author canadensys
 *
 */
public class SearchSortPart {
	
	private Integer pageNumber;
	private String orderByColumn;
	private OrderByEnum orderBy;
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public String getOrderByColumn() {
		return orderByColumn;
	}
	public void setOrderByColumn(String orderByColumn) {
		this.orderByColumn = orderByColumn;
	}
	
	public OrderByEnum getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(OrderByEnum orderBy) {
		this.orderBy = orderBy;
	}
}
