package net.canadensys.dataportal.vascan.dao;

import java.util.List;

import net.canadensys.dataportal.vascan.model.DistributionModel;
import net.canadensys.dataportal.vascan.model.DistributionStatusModel;

public interface DistributionDAO {
	
	public List<DistributionModel> loadTaxonDistribution(Integer taxonId);
	
	public List<DistributionStatusModel> loadAllDistributionStatus();

}
