package net.canadensys.dataportal.occurrence.dwc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveField;
import org.gbif.dwc.text.ArchiveFile;
import org.gbif.dwc.text.MetaDescriptorWriter;
import org.gbif.file.TabWriter;
import org.gbif.metadata.eml.Eml;
import org.gbif.metadata.eml.EmlWriter;

import com.google.common.collect.Maps;

import freemarker.template.TemplateException;

/**
 * DarwinCore archive writer that handles headers declaration.
 * The declaration will allow to specify the order of the columns and will also avoid a second
 * writing of the archive to fix missing data when a new column is 'discovered' in a later row.
 * Written from GBIF DwcaWriter.
 * @author canadensys
 *
 */
public class FixedHeadersDwcaWriter {
	private File dir;
	private long recordNum;
	private String coreId;
	private Map<ConceptTerm, String> coreRow;
	private final ConceptTerm coreRowType;
	private Map<ConceptTerm, TabWriter> writers = new HashMap<ConceptTerm, TabWriter>();
	private Map<ConceptTerm, String> dataFileNames = new HashMap<ConceptTerm, String>();
	private Map<ConceptTerm, List<ConceptTerm>> terms = new HashMap<ConceptTerm, List<ConceptTerm>>();
	private Eml eml;

	/**
	 * @param coreRowType
	 *            the core row type.
	 * @param dir
	 *            the directory to create the archive in.
	 */
	public FixedHeadersDwcaWriter(ConceptTerm coreRowType, List<ConceptTerm> coreTermList, File dir) throws IOException {
		this.dir = dir;
		this.coreRowType = coreRowType;
		addRowType(coreRowType,coreTermList);
	}

	public static String dataFileName(ConceptTerm rowType) {
		return rowType.simpleNormalisedName().toLowerCase() + ".txt";
	}

	private void addRowType(ConceptTerm rowType,List<ConceptTerm> conceptTermList) throws IOException {
		if(conceptTermList != null){
			terms.put(rowType, conceptTermList);
		}
		else{
			terms.put(rowType, new ArrayList<ConceptTerm>());
		}

		String dfn = dataFileName(rowType);
		dataFileNames.put(rowType, dfn);
		File df = new File(dir, dfn);
		org.apache.commons.io.FileUtils.forceMkdir(df.getParentFile());
		OutputStream out = new FileOutputStream(df);
		TabWriter wr = new TabWriter(out);
		writers.put(rowType, wr);
		
		//write headers
		writeHeaders(rowType);
	}

	public void newRecord(String id) throws IOException {
		// flush last record
		flushLastCoreRecord();
		// start new
		recordNum++;
		coreId = id;
		if(coreRow == null){
			coreRow = new HashMap<ConceptTerm, String>();
		}
		else{
			coreRow.clear();
		}
	}

	private void flushLastCoreRecord() throws IOException {
		if (coreRow != null && coreId != null) {
			writeRow(coreRow, coreRowType);
		}
	}

	public long getRecordsWritten() {
		return recordNum;
	}

	private void writeRow(Map<ConceptTerm, String> rowMap, ConceptTerm rowType)
			throws IOException {
		TabWriter writer = writers.get(rowType);
		List<ConceptTerm> coreTerms = terms.get(rowType);
		String[] row = new String[coreTerms.size() + 1];
		row[0] = coreId;
		for (ConceptTerm term : rowMap.keySet()) {
			int column = 1 + coreTerms.indexOf(term);
			row[column] = rowMap.get(term);
		}
		writer.write(row);
	}

	public void addCoreColumn(ConceptTerm term, String value) {
		List<ConceptTerm> coreTerms = terms.get(coreRowType);
		if (!coreTerms.contains(term)) {
			coreTerms.add(term);
		}
		coreRow.put(term, value);
	}

	/**
	 * @return new map of all current data file names by their rowTypes.
	 */
	public Map<ConceptTerm, String> getDataFiles() {
		return Maps.newHashMap(dataFileNames);
	}

	public void addExtensionRecord(ConceptTerm rowType,
			Map<ConceptTerm, String> row) throws IOException {
		// make sure we know the extension rowtype
		if (!terms.containsKey(rowType)) {
			addRowType(rowType,null);
		}
		// make sure we know all terms
		List<ConceptTerm> knownTerms = terms.get(rowType);
		for (ConceptTerm term : row.keySet()) {
			if (!knownTerms.contains(term)) {
				knownTerms.add(term);
			}
		}
		// write extension record
		writeRow(row, rowType);
	}

	public void setEml(Eml eml) {
		this.eml = eml;
	}

	/**
	 * writes meta.xml and eml.xml to the archive and closes tab writers.
	 */
	public void finalize() throws IOException {
		addEml();
		addMeta();
		// flush last record
		flushLastCoreRecord();
		// TODO: add missing columns in second iteration of data files

		// close writers
		for (TabWriter w : writers.values()) {
			w.close();
		}
	}

	private void addEml() throws IOException {
		if (eml != null) {
			File emlFile = new File(dir, "eml.xml");
			try {
				EmlWriter.writeEmlFile(emlFile, eml);
			} catch (TemplateException e) {
				throw new IOException("EML template exception: "
						+ e.getMessage(), e);
			}
		}
	}

	private void addMeta() throws IOException {
		File metaFile = new File(dir, "meta.xml");

		Archive arch = new Archive();
		if (eml != null) {
			arch.setMetadataLocation("eml.xml");
		}
		arch.setCore(buildArchiveFile(arch, coreRowType));
		for (ConceptTerm rowType : this.terms.keySet()) {
			if (!coreRowType.equals(rowType)) {
				arch.addExtension(buildArchiveFile(arch, rowType));
			}
		}
		try {
			MetaDescriptorWriter.writeMetaFile(metaFile, arch);
		} catch (TemplateException e) {
			throw new IOException("Meta.xml template exception: "
					+ e.getMessage(), e);
		}
	}
	
	private void writeHeaders(ConceptTerm rowType) throws IOException{
		TabWriter writer = writers.get(rowType);
		List<ConceptTerm> coreTerms = terms.get(rowType);
		String[] row = new String[coreTerms.size()+1];
		int i=1;
		row[0] = "id";
		for (ConceptTerm term : coreTerms) {
			row[i] = term.simpleName();
			i++;
		}
		writer.write(row);
	}

	private ArchiveFile buildArchiveFile(Archive archive, ConceptTerm rowType) {
		ArchiveFile af = ArchiveFile.buildTabFile();
		af.setArchive(archive);
		af.addLocation(dataFileNames.get(rowType));

		af.setEncoding("utf-8");
		//do not ignore header line
		af.setIgnoreHeaderLines(1);
		af.setRowType(rowType.qualifiedName());

		ArchiveField id = new ArchiveField();
		id.setIndex(0);
		af.setId(id);

		int idx = 0;
		for (ConceptTerm c : this.terms.get(rowType)) {
			idx++;
			ArchiveField field = new ArchiveField();
			field.setIndex(idx);
			field.setTerm(c);
			af.addField(field);
		}

		return af;
	}

}
