package net.canadensys.dataportal.vascan.dao;

import java.util.List;

import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

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
	
	/**
	 * Load denormalized distribution data for a collection of ids.
	 * @param taxonIdList
	 * @return list of Object with the following content :  "taxonid","iso3166_2","region","iso3166_1","occurrencestatus","establishmentMeans"
			"reference","url","excludedcode"
	 */
	public List<Object[]> loadCompleteDistributionData(List<Integer> taxonIdList);

}
