package net.canadensys.dataportal.vascan.dao;

import java.util.List;
import java.util.Map;

import net.canadensys.dataportal.vascan.model.DistributionModel;
import net.canadensys.dataportal.vascan.model.DistributionStatusModel;

/**
 * Interface for accessing distribution related data
 * @author canadensys
 *
 */
public interface DistributionDAO {
	
	//denormalized data (DD) keys
	public static final String DD_TAXON_ID = "taxonid";
	public static final String DD_ISO3166_2 = "iso3166_2";
	public static final String DD_REGION = "region";
	public static final String DD_ISO3166_1 = "iso3166_1";
	public static final String DD_OCCURRENCE_STATUS = "occurrencestatus";
	public static final String DD_ESTABLISHMENT_MEANS = "establishmentMeans";
	public static final String DD_REFERENCE = "reference";
	public static final String DD_URL = "url";
	public static final String DD_EXCLUDED_CODE = "excludedcode";
	
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
	public List<Map<String,Object>> loadDenormalizedDistributionData(List<Integer> taxonIdList);

}
