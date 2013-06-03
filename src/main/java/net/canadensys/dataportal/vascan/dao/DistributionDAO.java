package net.canadensys.dataportal.vascan.dao;

import java.util.List;

import net.canadensys.dataportal.vascan.model.DistributionModel;
import net.canadensys.dataportal.vascan.model.DistributionStatusModel;

/**
 * Interface for accessing distribution related data
 * @author canadensys
 *
 */
public interface DistributionDAO {
	
	/**
	 * Load all the DistributionModel for a specific taxonId
	 * @param taxonId
	 * @return
	 */
	public List<DistributionModel> loadTaxonDistribution(Integer taxonId);
	
	/**
	 * Load all the DistributionStatusModel
	 * @return
	 */
	public List<DistributionStatusModel> loadAllDistributionStatus();

}
