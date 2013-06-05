package net.canadensys.dataportal.vascan.dao;

import java.util.List;

import net.canadensys.dataportal.vascan.model.RegionModel;

/**
 * Interface for accessing region data.
 * @author canandensys
 *
 */
public interface RegionDAO {
	
	public List<RegionModel> loadAllRegion();
}
