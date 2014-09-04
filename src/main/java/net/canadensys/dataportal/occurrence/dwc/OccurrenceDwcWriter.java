package net.canadensys.dataportal.occurrence.dwc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import net.canadensys.dataportal.occurrence.model.OccurrenceModel;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.TermFactory;
import org.gbif.dwc.terms.UnknownTerm;

/**
 * This class is responsible to write one or many OccurrenceRawModel into a Darwin Core archive.
 * @author canadensys
 *
 */
public class OccurrenceDwcWriter {
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(OccurrenceDwcWriter.class);

	/**
	 * Write a list of OccurrenRawModel into a DarwinCore file.
	 * We use a Iterator because the list could be too big to be loaded in memory.
	 * All columns will be written in alphabetical order
	 * @param destinationFolder
	 * @param it
	 * @return success or not
	 */
	@SuppressWarnings("unchecked")
	public static boolean write(List<String> headers, File destinationFolder, Iterator<OccurrenceModel> it){
		Map<String,Object> beanDescription;
		Object propObj;
		
		try {
			TreeSet<String> sortedHeader;
			//Find the Terms for the header
			List<Term> headerTermList = new ArrayList<Term>();
			Map<String,Term> conceptTermList = new HashMap<String, Term>();
			Term conceptTerm;
			for(String currHeader : headers){
				conceptTerm = TermFactory.instance().findTerm(currHeader);
				if(conceptTerm instanceof UnknownTerm){
					LOGGER.fatal("UnknownTerm in DarwinCore header. This header comes from the config file :" + currHeader);
				}
				else{
					headerTermList.add(conceptTerm);
					conceptTermList.put(currHeader, conceptTerm);
				}
			}
			
			FixedHeadersDwcaWriter dwcaWriter = new FixedHeadersDwcaWriter(DwcTerm.Occurrence, headerTermList, destinationFolder);
			OccurrenceModel occModel = it.next();
			while(occModel != null){
				//use our auto_id since we are an aggregator
				dwcaWriter.newRecord(Integer.toString(occModel.getAuto_id()));
				try {
					//For the moment, we only write the RawModel
					beanDescription = PropertyUtils.describe(occModel.getRawModel());
					sortedHeader = new TreeSet<String>(beanDescription.keySet());
					
					for(String property:sortedHeader){
						propObj =  beanDescription.get(property);
						if(!StringUtils.isBlank(ObjectUtils.toString(propObj)) && propObj.getClass() != Class.class){
							//reserved words start with an underscore
							property = property.replaceFirst("_", "");
							//make sure it's a dwc term
							if(conceptTermList.containsKey(property)){
								conceptTerm = conceptTermList.get(property);
								dwcaWriter.addCoreColumn(conceptTerm, propObj.toString());
							}
						}
					}
					sortedHeader.clear();
					beanDescription.clear();
				} catch (IllegalAccessException e) {
					LOGGER.fatal("Trying to describe OccurrenceModel as JavaBean", e);
					return false;
				} catch (InvocationTargetException e) {
					LOGGER.fatal("Trying to describe OccurrenceModel as JavaBean", e);
					return false;
				} catch (NoSuchMethodException e) {
					LOGGER.fatal("Trying to describe OccurrenceModel as JavaBean", e);
					return false;
				}
				if(it.hasNext()){
					occModel = it.next();
				}
				else{
					occModel = null;
				}
			}
			dwcaWriter.finalize();
		} catch (IOException e) {
			LOGGER.fatal("Trying to write DarwinCore file", e);
			return false;
		}
		return true;
	}
}
