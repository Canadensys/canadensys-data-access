package net.canadensys.dataportal.occurrence.dwc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.canadensys.dataportal.occurrence.model.OccurrenceModel;
import net.canadensys.dataportal.occurrence.model.OccurrenceRawModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Test Coverage : 
 * -Create DarwinCore archive from a OccurrenceRawModel
 * -Load DownloadLogModel
 * @author canadensys
 *
 */
public class OccurrenceDwcWriterTest {
	
	@Test
	public void testDarwinCoreArchiveGeneration(){
		File destinationFolder = null;
		try {
			destinationFolder = new File(getClass().getResource("/test-dwca").toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail();
		}

		OccurrenceModel occModel = new OccurrenceModel();
		occModel.setAuto_id(1);
		OccurrenceRawModel occRawModel = new OccurrenceRawModel();
		occModel.setRawModel(occRawModel);
		
		occRawModel.setAuto_id(1);
		occRawModel.setOccurrenceid("ID#1");
		occRawModel.setSourcefileid("source-file-occurrence");
		occRawModel.setCountry("Canada");
		occRawModel.set_class("Equisetopsida");
		
		List<String> headers = new ArrayList<String>();
		headers.add("country");
		headers.add("class");
		
		List<OccurrenceModel> occModelList = new ArrayList<OccurrenceModel>();
		occModelList.add(occModel);
		
		assertTrue(OccurrenceDwcWriter.write(headers, destinationFolder, occModelList.iterator()));
		
		File expectedFile = new File(FilenameUtils.concat(destinationFolder.getAbsolutePath(), "expected-occurrence.txt"));
		File generatedFile = new File(FilenameUtils.concat(destinationFolder.getAbsolutePath(), "occurrence.txt"));
		
		File expectedMeta = new File(FilenameUtils.concat(destinationFolder.getAbsolutePath(), "expected-meta.xml"));
		File generatedMeta = new File(FilenameUtils.concat(destinationFolder.getAbsolutePath(), "meta.xml"));
		
		try {
			List<String> expectedLines = FileUtils.readLines(expectedFile, "UTF-8");
			List<String> actualLines = FileUtils.readLines(expectedFile, "UTF-8");
			//use StringUtils.chomp to ignore the EOL char 
			for(int i=0;i<expectedLines.size();i++){
				assertEquals(StringUtils.chomp(expectedLines.get(i)),StringUtils.chomp(actualLines.get(i)));
			}

			expectedLines = FileUtils.readLines(expectedMeta, "UTF-8");
			actualLines = FileUtils.readLines(generatedMeta, "UTF-8");
			//use StringUtils.chomp to ignore the EOL char 
			for(int i=0;i<expectedLines.size();i++){
				assertEquals(StringUtils.chomp(expectedLines.get(i)),StringUtils.chomp(actualLines.get(i)));
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		//clean up
		generatedFile.delete();
		generatedMeta.delete();
	}
}
