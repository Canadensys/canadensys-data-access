package net.canadensys.model;

/**
 * Simple object to hold a suggestion and its related count.
 * Used for autocomplete values.
 * @author cgendreau
 *
 */
public class SuggestedValue {
	
	protected String value;
	protected long count;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
}
