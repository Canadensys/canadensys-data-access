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
	private Integer pageSize;
	private String orderByColumn;
	private OrderByEnum orderBy;
	
	/**
	 * Page number starts at 1.
	 * First page is 1 but 0 is an accepted value.
	 * @return
	 */
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
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
