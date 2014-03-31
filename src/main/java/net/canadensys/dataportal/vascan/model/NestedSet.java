package net.canadensys.dataportal.vascan.model;

import javax.persistence.MappedSuperclass;

/**
 * Nested set related data.
 * @author canadensys
 *
 */
@MappedSuperclass
public abstract class NestedSet {
	private Integer _left;
	private Integer _right;
	private Integer parentID;
	
	public Integer get_left() {
		return _left;
	}
	public void set_left(Integer _left) {
		this._left = _left;
	}
	
	public Integer get_right() {
		return _right;
	}
	public void set_right(Integer _right) {
		this._right = _right;
	}
	
	public Integer getParentID() {
		return parentID;
	}
	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}
}
