package net.canadensys.dataportal.vascan.dao.query;



/**
 * Represents the region related part of a query.
 * @author cgendreau
 *
 */
public class RegionQueryPart {
	
	public enum RegionSelector {
		ALL_OF("allof"),ANY_OF("anyof"),ONLY_IN("only"),ALL_OF_ONLY_IN("allofonly");
		
		private String label;
		private RegionSelector(String label){
			this.label = label;
		}
		
		public String getLabel(){
			return label;
		}
		
	    public static RegionSelector fromLabel(String label) {
	    	if (label != null) {
	    		for (RegionSelector currRegion : RegionSelector.values()) {
	    			if (label.equalsIgnoreCase(currRegion.label)) {
	    				return currRegion;
	    			}
	    		}
	    	}
	        return null;
	    }
	};
	
	private RegionSelector regionSelector;
	private String[] region;
	private boolean searchOnlyInCanada = false;
	
	public String[] getRegion() {
		return region;
	}
	public void setRegion(String[] region) {
		this.region = region;
	}
	
	public boolean isSearchOnlyInCanada() {
		return searchOnlyInCanada;
	}
	public void setSearchOnlyInCanada(boolean searchOnlyInCanada) {
		this.searchOnlyInCanada = searchOnlyInCanada;
	}

	public RegionSelector getRegionSelector() {
		return regionSelector;
	}
	public void setRegionSelector(RegionSelector regionSelector) {
		this.regionSelector = regionSelector;
	}
}
