package net.canadensys.dataportal.occurrence.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * A full Occurrence according to DarwinCore.
 * This class contains all raw values.
 * @author canadensys
 */
@Entity
@Table(name = "occurrence_raw")
public class OccurrenceRawModel {
	
	@Id
	private int auto_id;
	
	//used to easily read the id in the dwca
	@Transient
	private String id;
	
	//mapped to id in the dwca
	private String dwcaid;
	//not in dwca
	private String sourcefileid;
	
	private String acceptednameusage;
	private String acceptednameusageid;
	private String accessrights;
	private String associatedmedia;
	private String associatedoccurrences;
	private String associatedreferences;
	private String associatedsequences;
	private String associatedtaxa;
	private String basisofrecord;
	private String bed;
	private String behavior;
	private String bibliographiccitation;
	private String catalognumber;
	private String _class;
	private String collectioncode;
	private String collectionid;
	private String continent;
	private String coordinateprecision;
	private String coordinateuncertaintyinmeters;
	private String country;
	private String countrycode;
	private String county;
	private String datageneralizations;
	private String datasetid;
	private String datasetname;
	private String dateidentified;
	private String day;
	private String decimallatitude;
	private String decimallongitude;
	private String disposition;
	private String dynamicproperties;
	private String earliestageorloweststage;
	private String earliesteonorlowesteonothem;
	private String earliestepochorlowestseries;
	private String earliesteraorlowesterathem;
	private String earliestperiodorlowestsystem;
	private String enddayofyear;
	private String establishmentmeans;
	private String eventdate;
	private String eventid;
	private String eventremarks;
	private String eventtime;
	private String family;
	private String fieldnotes;
	private String fieldnumber;
	private String footprintspatialfit;
	private String footprintsrs;
	private String footprintwkt;
	private String formation;
	private String genus;
	private String geodeticdatum;
	private String geologicalcontextid;
	private String georeferencedby;
	private String georeferenceddate;
	private String georeferenceprotocol;
	private String georeferenceremarks;
	private String georeferencesources;
	private String georeferenceverificationstatus;
	private String _group;
	private String habitat;
	private String higherclassification;
	private String highergeography;
	private String highergeographyid;
	private String highestbiostratigraphiczone;
	private String identificationid;
	private String identificationqualifier;
	private String identificationreferences;
	private String identificationremarks;
	private String identificationverificationstatus;
	private String identifiedby;
	private String individualcount;
	private String individualid;
	private String informationwithheld;
	private String infraspecificepithet;
	private String institutioncode;
	private String institutionid;
	private String island;
	private String islandgroup;
	private String kingdom;
	private String language;
	private String latestageorhigheststage;
	private String latesteonorhighesteonothem;
	private String latestepochorhighestseries;
	private String latesteraorhighesterathem;
	private String latestperiodorhighestsystem;
	private String lifestage;
	private String lithostratigraphicterms;
	private String locality;
	private String locationaccordingto;
	private String locationid;
	private String locationremarks;
	private String lowestbiostratigraphiczone;
	private String maximumdepthinmeters;
	private String maximumdistanceabovesurfaceinmeters;
	private String maximumelevationinmeters;
	private String member;
	private String minimumdepthinmeters;
	private String minimumdistanceabovesurfaceinmeters;
	private String minimumelevationinmeters;
	private String modified;
	private String month;
	private String municipality;
	private String nameaccordingto;
	private String nameaccordingtoid;
	private String namepublishedin;
	private String namepublishedinid;
	private String namepublishedinyear;
	private String nomenclaturalcode;
	private String nomenclaturalstatus;
	private String occurrenceid;
	private String occurrenceremarks;
	private String occurrencestatus;
	private String _order;
	private String originalnameusage;
	private String originalnameusageid;
	private String othercatalognumbers;
	private String ownerinstitutioncode;
	private String parentnameusage;
	private String parentnameusageid;
	private String phylum;
	private String pointradiusspatialfit;
	private String preparations;
	private String previousidentifications;
	private String recordedby;
	private String recordnumber;
	private String _references;
	private String reproductivecondition;
	private String rights;
	private String rightsholder;
	private String samplingeffort;
	private String samplingprotocol;
	private String scientificname;
	private String scientificnameauthorship;
	private String scientificnameid;
	private String sex;
	private String specificepithet;
	private String startdayofyear;
	private String stateprovince;
	private String subgenus;
	private String taxonconceptid;
	private String taxonid;
	private String taxonomicstatus;
	private String taxonrank;
	private String taxonremarks;
	private String type;
	private String typestatus;
	private String verbatimcoordinates;
	private String verbatimcoordinatesystem;
	private String verbatimdepth;
	private String verbatimelevation;
	private String verbatimeventdate;
	private String verbatimlatitude;
	private String verbatimlocality;
	private String verbatimlongitude;
	private String verbatimsrs;
	private String verbatimtaxonrank;
	private String vernacularname;
	private String waterbody;
	private String year;
	
	public int getAuto_id() {
		return auto_id;
	}
	public void setAuto_id(int auto_id) {
		this.auto_id = auto_id;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAcceptednameusage() {
		return acceptednameusage;
	}
	public void setAcceptednameusage(String acceptednameusage) {
		this.acceptednameusage = acceptednameusage;
	}
	public String getAcceptednameusageid() {
		return acceptednameusageid;
	}
	public void setAcceptednameusageid(String acceptednameusageid) {
		this.acceptednameusageid = acceptednameusageid;
	}
	public String getAccessrights() {
		return accessrights;
	}
	public void setAccessrights(String accessrights) {
		this.accessrights = accessrights;
	}
	public String getAssociatedmedia() {
		return associatedmedia;
	}
	public void setAssociatedmedia(String associatedmedia) {
		this.associatedmedia = associatedmedia;
	}
	public String getAssociatedoccurrences() {
		return associatedoccurrences;
	}
	public void setAssociatedoccurrences(String associatedoccurrences) {
		this.associatedoccurrences = associatedoccurrences;
	}
	public String getAssociatedreferences() {
		return associatedreferences;
	}
	public void setAssociatedreferences(String associatedreferences) {
		this.associatedreferences = associatedreferences;
	}
	public String getAssociatedsequences() {
		return associatedsequences;
	}
	public void setAssociatedsequences(String associatedsequences) {
		this.associatedsequences = associatedsequences;
	}
	public String getAssociatedtaxa() {
		return associatedtaxa;
	}
	public void setAssociatedtaxa(String associatedtaxa) {
		this.associatedtaxa = associatedtaxa;
	}
	public String getBasisofrecord() {
		return basisofrecord;
	}
	public void setBasisofrecord(String basisofrecord) {
		this.basisofrecord = basisofrecord;
	}
	public String getBed() {
		return bed;
	}
	public void setBed(String bed) {
		this.bed = bed;
	}
	public String getBehavior() {
		return behavior;
	}
	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}
	public String getBibliographiccitation() {
		return bibliographiccitation;
	}
	public void setBibliographiccitation(String bibliographiccitation) {
		this.bibliographiccitation = bibliographiccitation;
	}
	public String getCatalognumber() {
		return catalognumber;
	}
	public void setCatalognumber(String catalognumber) {
		this.catalognumber = catalognumber;
	}
	public String get_class() {
		return _class;
	}
	public void set_class(String _class) {
		this._class = _class;
	}
	public String getCollectioncode() {
		return collectioncode;
	}
	public void setCollectioncode(String collectioncode) {
		this.collectioncode = collectioncode;
	}
	public String getCollectionid() {
		return collectionid;
	}
	public void setCollectionid(String collectionid) {
		this.collectionid = collectionid;
	}
	public String getContinent() {
		return continent;
	}
	public void setContinent(String continent) {
		this.continent = continent;
	}
	public String getCoordinateprecision() {
		return coordinateprecision;
	}
	public void setCoordinateprecision(String coordinateprecision) {
		this.coordinateprecision = coordinateprecision;
	}
	public String getCoordinateuncertaintyinmeters() {
		return coordinateuncertaintyinmeters;
	}
	public void setCoordinateuncertaintyinmeters(
			String coordinateuncertaintyinmeters) {
		this.coordinateuncertaintyinmeters = coordinateuncertaintyinmeters;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountrycode() {
		return countrycode;
	}
	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getDatageneralizations() {
		return datageneralizations;
	}
	public void setDatageneralizations(String datageneralizations) {
		this.datageneralizations = datageneralizations;
	}
	public String getDatasetid() {
		return datasetid;
	}
	public void setDatasetid(String datasetid) {
		this.datasetid = datasetid;
	}
	public String getDatasetname() {
		return datasetname;
	}
	public void setDatasetname(String datasetname) {
		this.datasetname = datasetname;
	}
	public String getDateidentified() {
		return dateidentified;
	}
	public void setDateidentified(String dateidentified) {
		this.dateidentified = dateidentified;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDecimallatitude() {
		return decimallatitude;
	}
	public void setDecimallatitude(String decimallatitude) {
		this.decimallatitude = decimallatitude;
	}
	public String getDecimallongitude() {
		return decimallongitude;
	}
	public void setDecimallongitude(String decimallongitude) {
		this.decimallongitude = decimallongitude;
	}
	public String getDisposition() {
		return disposition;
	}
	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
	public String getDynamicproperties() {
		return dynamicproperties;
	}
	public void setDynamicproperties(String dynamicproperties) {
		this.dynamicproperties = dynamicproperties;
	}
	public String getEarliestageorloweststage() {
		return earliestageorloweststage;
	}
	public void setEarliestageorloweststage(String earliestageorloweststage) {
		this.earliestageorloweststage = earliestageorloweststage;
	}
	public String getEarliesteonorlowesteonothem() {
		return earliesteonorlowesteonothem;
	}
	public void setEarliesteonorlowesteonothem(String earliesteonorlowesteonothem) {
		this.earliesteonorlowesteonothem = earliesteonorlowesteonothem;
	}
	public String getEarliestepochorlowestseries() {
		return earliestepochorlowestseries;
	}
	public void setEarliestepochorlowestseries(String earliestepochorlowestseries) {
		this.earliestepochorlowestseries = earliestepochorlowestseries;
	}
	public String getEarliesteraorlowesterathem() {
		return earliesteraorlowesterathem;
	}
	public void setEarliesteraorlowesterathem(String earliesteraorlowesterathem) {
		this.earliesteraorlowesterathem = earliesteraorlowesterathem;
	}
	public String getEarliestperiodorlowestsystem() {
		return earliestperiodorlowestsystem;
	}
	public void setEarliestperiodorlowestsystem(String earliestperiodorlowestsystem) {
		this.earliestperiodorlowestsystem = earliestperiodorlowestsystem;
	}
	public String getEnddayofyear() {
		return enddayofyear;
	}
	public void setEnddayofyear(String enddayofyear) {
		this.enddayofyear = enddayofyear;
	}
	public String getEstablishmentmeans() {
		return establishmentmeans;
	}
	public void setEstablishmentmeans(String establishmentmeans) {
		this.establishmentmeans = establishmentmeans;
	}
	public String getEventdate() {
		return eventdate;
	}
	public void setEventdate(String eventdate) {
		this.eventdate = eventdate;
	}
	public String getEventid() {
		return eventid;
	}
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}
	public String getEventremarks() {
		return eventremarks;
	}
	public void setEventremarks(String eventremarks) {
		this.eventremarks = eventremarks;
	}
	public String getEventtime() {
		return eventtime;
	}
	public void setEventtime(String eventtime) {
		this.eventtime = eventtime;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getFieldnotes() {
		return fieldnotes;
	}
	public void setFieldnotes(String fieldnotes) {
		this.fieldnotes = fieldnotes;
	}
	public String getFieldnumber() {
		return fieldnumber;
	}
	public void setFieldnumber(String fieldnumber) {
		this.fieldnumber = fieldnumber;
	}
	public String getFootprintspatialfit() {
		return footprintspatialfit;
	}
	public void setFootprintspatialfit(String footprintspatialfit) {
		this.footprintspatialfit = footprintspatialfit;
	}
	public String getFootprintsrs() {
		return footprintsrs;
	}
	public void setFootprintsrs(String footprintsrs) {
		this.footprintsrs = footprintsrs;
	}
	public String getFootprintwkt() {
		return footprintwkt;
	}
	public void setFootprintwkt(String footprintwkt) {
		this.footprintwkt = footprintwkt;
	}
	public String getFormation() {
		return formation;
	}
	public void setFormation(String formation) {
		this.formation = formation;
	}
	public String getGenus() {
		return genus;
	}
	public void setGenus(String genus) {
		this.genus = genus;
	}
	public String getGeodeticdatum() {
		return geodeticdatum;
	}
	public void setGeodeticdatum(String geodeticdatum) {
		this.geodeticdatum = geodeticdatum;
	}
	public String getGeologicalcontextid() {
		return geologicalcontextid;
	}
	public void setGeologicalcontextid(String geologicalcontextid) {
		this.geologicalcontextid = geologicalcontextid;
	}
	public String getGeoreferencedby() {
		return georeferencedby;
	}
	public void setGeoreferencedby(String georeferencedby) {
		this.georeferencedby = georeferencedby;
	}
	public String getGeoreferenceddate() {
		return georeferenceddate;
	}
	public void setGeoreferenceddate(String georeferenceddate) {
		this.georeferenceddate = georeferenceddate;
	}
	public String getGeoreferenceprotocol() {
		return georeferenceprotocol;
	}
	public void setGeoreferenceprotocol(String georeferenceprotocol) {
		this.georeferenceprotocol = georeferenceprotocol;
	}
	public String getGeoreferenceremarks() {
		return georeferenceremarks;
	}
	public void setGeoreferenceremarks(String georeferenceremarks) {
		this.georeferenceremarks = georeferenceremarks;
	}
	public String getGeoreferencesources() {
		return georeferencesources;
	}
	public void setGeoreferencesources(String georeferencesources) {
		this.georeferencesources = georeferencesources;
	}
	public String getGeoreferenceverificationstatus() {
		return georeferenceverificationstatus;
	}
	public void setGeoreferenceverificationstatus(
			String georeferenceverificationstatus) {
		this.georeferenceverificationstatus = georeferenceverificationstatus;
	}
	public String get_group() {
		return _group;
	}
	public void set_group(String _group) {
		this._group = _group;
	}
	public String getHabitat() {
		return habitat;
	}
	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}
	public String getHigherclassification() {
		return higherclassification;
	}
	public void setHigherclassification(String higherclassification) {
		this.higherclassification = higherclassification;
	}
	public String getHighergeography() {
		return highergeography;
	}
	public void setHighergeography(String highergeography) {
		this.highergeography = highergeography;
	}
	public String getHighergeographyid() {
		return highergeographyid;
	}
	public void setHighergeographyid(String highergeographyid) {
		this.highergeographyid = highergeographyid;
	}
	public String getHighestbiostratigraphiczone() {
		return highestbiostratigraphiczone;
	}
	public void setHighestbiostratigraphiczone(String highestbiostratigraphiczone) {
		this.highestbiostratigraphiczone = highestbiostratigraphiczone;
	}
	public String getIdentificationid() {
		return identificationid;
	}
	public void setIdentificationid(String identificationid) {
		this.identificationid = identificationid;
	}
	public String getIdentificationqualifier() {
		return identificationqualifier;
	}
	public void setIdentificationqualifier(String identificationqualifier) {
		this.identificationqualifier = identificationqualifier;
	}
	public String getIdentificationreferences() {
		return identificationreferences;
	}
	public void setIdentificationreferences(String identificationreferences) {
		this.identificationreferences = identificationreferences;
	}
	public String getIdentificationremarks() {
		return identificationremarks;
	}
	public void setIdentificationremarks(String identificationremarks) {
		this.identificationremarks = identificationremarks;
	}
	public String getIdentificationverificationstatus() {
		return identificationverificationstatus;
	}
	public void setIdentificationverificationstatus(
			String identificationverificationstatus) {
		this.identificationverificationstatus = identificationverificationstatus;
	}
	public String getIdentifiedby() {
		return identifiedby;
	}
	public void setIdentifiedby(String identifiedby) {
		this.identifiedby = identifiedby;
	}
	public String getIndividualcount() {
		return individualcount;
	}
	public void setIndividualcount(String individualcount) {
		this.individualcount = individualcount;
	}
	public String getIndividualid() {
		return individualid;
	}
	public void setIndividualid(String individualid) {
		this.individualid = individualid;
	}
	public String getInformationwithheld() {
		return informationwithheld;
	}
	public void setInformationwithheld(String informationwithheld) {
		this.informationwithheld = informationwithheld;
	}
	public String getInfraspecificepithet() {
		return infraspecificepithet;
	}
	public void setInfraspecificepithet(String infraspecificepithet) {
		this.infraspecificepithet = infraspecificepithet;
	}
	public String getInstitutioncode() {
		return institutioncode;
	}
	public void setInstitutioncode(String institutioncode) {
		this.institutioncode = institutioncode;
	}
	public String getInstitutionid() {
		return institutionid;
	}
	public void setInstitutionid(String institutionid) {
		this.institutionid = institutionid;
	}
	public String getIsland() {
		return island;
	}
	public void setIsland(String island) {
		this.island = island;
	}
	public String getIslandgroup() {
		return islandgroup;
	}
	public void setIslandgroup(String islandgroup) {
		this.islandgroup = islandgroup;
	}
	public String getKingdom() {
		return kingdom;
	}
	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getLatestageorhigheststage() {
		return latestageorhigheststage;
	}
	public void setLatestageorhigheststage(String latestageorhigheststage) {
		this.latestageorhigheststage = latestageorhigheststage;
	}
	public String getLatesteonorhighesteonothem() {
		return latesteonorhighesteonothem;
	}
	public void setLatesteonorhighesteonothem(String latesteonorhighesteonothem) {
		this.latesteonorhighesteonothem = latesteonorhighesteonothem;
	}
	public String getLatestepochorhighestseries() {
		return latestepochorhighestseries;
	}
	public void setLatestepochorhighestseries(String latestepochorhighestseries) {
		this.latestepochorhighestseries = latestepochorhighestseries;
	}
	public String getLatesteraorhighesterathem() {
		return latesteraorhighesterathem;
	}
	public void setLatesteraorhighesterathem(String latesteraorhighesterathem) {
		this.latesteraorhighesterathem = latesteraorhighesterathem;
	}
	public String getLatestperiodorhighestsystem() {
		return latestperiodorhighestsystem;
	}
	public void setLatestperiodorhighestsystem(String latestperiodorhighestsystem) {
		this.latestperiodorhighestsystem = latestperiodorhighestsystem;
	}
	public String getLifestage() {
		return lifestage;
	}
	public void setLifestage(String lifestage) {
		this.lifestage = lifestage;
	}
	public String getLithostratigraphicterms() {
		return lithostratigraphicterms;
	}
	public void setLithostratigraphicterms(String lithostratigraphicterms) {
		this.lithostratigraphicterms = lithostratigraphicterms;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getLocationaccordingto() {
		return locationaccordingto;
	}
	public void setLocationaccordingto(String locationaccordingto) {
		this.locationaccordingto = locationaccordingto;
	}
	public String getLocationid() {
		return locationid;
	}
	public void setLocationid(String locationid) {
		this.locationid = locationid;
	}
	public String getLocationremarks() {
		return locationremarks;
	}
	public void setLocationremarks(String locationremarks) {
		this.locationremarks = locationremarks;
	}
	public String getLowestbiostratigraphiczone() {
		return lowestbiostratigraphiczone;
	}
	public void setLowestbiostratigraphiczone(String lowestbiostratigraphiczone) {
		this.lowestbiostratigraphiczone = lowestbiostratigraphiczone;
	}
	public String getMaximumdepthinmeters() {
		return maximumdepthinmeters;
	}
	public void setMaximumdepthinmeters(String maximumdepthinmeters) {
		this.maximumdepthinmeters = maximumdepthinmeters;
	}
	public String getMaximumdistanceabovesurfaceinmeters() {
		return maximumdistanceabovesurfaceinmeters;
	}
	public void setMaximumdistanceabovesurfaceinmeters(
			String maximumdistanceabovesurfaceinmeters) {
		this.maximumdistanceabovesurfaceinmeters = maximumdistanceabovesurfaceinmeters;
	}
	public String getMaximumelevationinmeters() {
		return maximumelevationinmeters;
	}
	public void setMaximumelevationinmeters(String maximumelevationinmeters) {
		this.maximumelevationinmeters = maximumelevationinmeters;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getMinimumdepthinmeters() {
		return minimumdepthinmeters;
	}
	public void setMinimumdepthinmeters(String minimumdepthinmeters) {
		this.minimumdepthinmeters = minimumdepthinmeters;
	}
	public String getMinimumdistanceabovesurfaceinmeters() {
		return minimumdistanceabovesurfaceinmeters;
	}
	public void setMinimumdistanceabovesurfaceinmeters(
			String minimumdistanceabovesurfaceinmeters) {
		this.minimumdistanceabovesurfaceinmeters = minimumdistanceabovesurfaceinmeters;
	}
	public String getMinimumelevationinmeters() {
		return minimumelevationinmeters;
	}
	public void setMinimumelevationinmeters(String minimumelevationinmeters) {
		this.minimumelevationinmeters = minimumelevationinmeters;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getMunicipality() {
		return municipality;
	}
	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}
	public String getNameaccordingto() {
		return nameaccordingto;
	}
	public void setNameaccordingto(String nameaccordingto) {
		this.nameaccordingto = nameaccordingto;
	}
	public String getNameaccordingtoid() {
		return nameaccordingtoid;
	}
	public void setNameaccordingtoid(String nameaccordingtoid) {
		this.nameaccordingtoid = nameaccordingtoid;
	}
	public String getNamepublishedin() {
		return namepublishedin;
	}
	public void setNamepublishedin(String namepublishedin) {
		this.namepublishedin = namepublishedin;
	}
	public String getNamepublishedinid() {
		return namepublishedinid;
	}
	public void setNamepublishedinid(String namepublishedinid) {
		this.namepublishedinid = namepublishedinid;
	}
	public String getNamepublishedinyear() {
		return namepublishedinyear;
	}
	public void setNamepublishedinyear(String namepublishedinyear) {
		this.namepublishedinyear = namepublishedinyear;
	}
	public String getNomenclaturalcode() {
		return nomenclaturalcode;
	}
	public void setNomenclaturalcode(String nomenclaturalcode) {
		this.nomenclaturalcode = nomenclaturalcode;
	}
	public String getNomenclaturalstatus() {
		return nomenclaturalstatus;
	}
	public void setNomenclaturalstatus(String nomenclaturalstatus) {
		this.nomenclaturalstatus = nomenclaturalstatus;
	}
	public String getOccurrenceid() {
		return occurrenceid;
	}
	public void setOccurrenceid(String occurrenceid) {
		this.occurrenceid = occurrenceid;
	}
	public String getOccurrenceremarks() {
		return occurrenceremarks;
	}
	public void setOccurrenceremarks(String occurrenceremarks) {
		this.occurrenceremarks = occurrenceremarks;
	}
	public String getOccurrencestatus() {
		return occurrencestatus;
	}
	public void setOccurrencestatus(String occurrencestatus) {
		this.occurrencestatus = occurrencestatus;
	}
	public String get_order() {
		return _order;
	}
	public void set_order(String _order) {
		this._order = _order;
	}
	public String getOriginalnameusage() {
		return originalnameusage;
	}
	public void setOriginalnameusage(String originalnameusage) {
		this.originalnameusage = originalnameusage;
	}
	public String getOriginalnameusageid() {
		return originalnameusageid;
	}
	public void setOriginalnameusageid(String originalnameusageid) {
		this.originalnameusageid = originalnameusageid;
	}
	public String getOthercatalognumbers() {
		return othercatalognumbers;
	}
	public void setOthercatalognumbers(String othercatalognumbers) {
		this.othercatalognumbers = othercatalognumbers;
	}
	public String getOwnerinstitutioncode() {
		return ownerinstitutioncode;
	}
	public void setOwnerinstitutioncode(String ownerinstitutioncode) {
		this.ownerinstitutioncode = ownerinstitutioncode;
	}
	public String getParentnameusage() {
		return parentnameusage;
	}
	public void setParentnameusage(String parentnameusage) {
		this.parentnameusage = parentnameusage;
	}
	public String getParentnameusageid() {
		return parentnameusageid;
	}
	public void setParentnameusageid(String parentnameusageid) {
		this.parentnameusageid = parentnameusageid;
	}
	public String getPhylum() {
		return phylum;
	}
	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}
	public String getPointradiusspatialfit() {
		return pointradiusspatialfit;
	}
	public void setPointradiusspatialfit(String pointradiusspatialfit) {
		this.pointradiusspatialfit = pointradiusspatialfit;
	}
	public String getPreparations() {
		return preparations;
	}
	public void setPreparations(String preparations) {
		this.preparations = preparations;
	}
	public String getPreviousidentifications() {
		return previousidentifications;
	}
	public void setPreviousidentifications(String previousidentifications) {
		this.previousidentifications = previousidentifications;
	}
	public String getRecordedby() {
		return recordedby;
	}
	public void setRecordedby(String recordedby) {
		this.recordedby = recordedby;
	}
	public String getRecordnumber() {
		return recordnumber;
	}
	public void setRecordnumber(String recordnumber) {
		this.recordnumber = recordnumber;
	}
	public String get_references() {
		return _references;
	}
	public void set_references(String _references) {
		this._references = _references;
	}
	public String getReproductivecondition() {
		return reproductivecondition;
	}
	public void setReproductivecondition(String reproductivecondition) {
		this.reproductivecondition = reproductivecondition;
	}
	public String getRights() {
		return rights;
	}
	public void setRights(String rights) {
		this.rights = rights;
	}
	public String getRightsholder() {
		return rightsholder;
	}
	public void setRightsholder(String rightsholder) {
		this.rightsholder = rightsholder;
	}
	public String getSamplingeffort() {
		return samplingeffort;
	}
	public void setSamplingeffort(String samplingeffort) {
		this.samplingeffort = samplingeffort;
	}
	public String getSamplingprotocol() {
		return samplingprotocol;
	}
	public void setSamplingprotocol(String samplingprotocol) {
		this.samplingprotocol = samplingprotocol;
	}
	public String getScientificname() {
		return scientificname;
	}
	public void setScientificname(String scientificname) {
		this.scientificname = scientificname;
	}
	public String getScientificnameauthorship() {
		return scientificnameauthorship;
	}
	public void setScientificnameauthorship(String scientificnameauthorship) {
		this.scientificnameauthorship = scientificnameauthorship;
	}
	public String getScientificnameid() {
		return scientificnameid;
	}
	public void setScientificnameid(String scientificnameid) {
		this.scientificnameid = scientificnameid;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSpecificepithet() {
		return specificepithet;
	}
	public void setSpecificepithet(String specificepithet) {
		this.specificepithet = specificepithet;
	}
	public String getStartdayofyear() {
		return startdayofyear;
	}
	public void setStartdayofyear(String startdayofyear) {
		this.startdayofyear = startdayofyear;
	}
	public String getStateprovince() {
		return stateprovince;
	}
	public void setStateprovince(String stateprovince) {
		this.stateprovince = stateprovince;
	}
	public String getSubgenus() {
		return subgenus;
	}
	public void setSubgenus(String subgenus) {
		this.subgenus = subgenus;
	}
	public String getTaxonconceptid() {
		return taxonconceptid;
	}
	public void setTaxonconceptid(String taxonconceptid) {
		this.taxonconceptid = taxonconceptid;
	}
	public String getTaxonid() {
		return taxonid;
	}
	public void setTaxonid(String taxonid) {
		this.taxonid = taxonid;
	}
	public String getTaxonomicstatus() {
		return taxonomicstatus;
	}
	public void setTaxonomicstatus(String taxonomicstatus) {
		this.taxonomicstatus = taxonomicstatus;
	}
	public String getTaxonrank() {
		return taxonrank;
	}
	public void setTaxonrank(String taxonrank) {
		this.taxonrank = taxonrank;
	}
	public String getTaxonremarks() {
		return taxonremarks;
	}
	public void setTaxonremarks(String taxonremarks) {
		this.taxonremarks = taxonremarks;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypestatus() {
		return typestatus;
	}
	public void setTypestatus(String typestatus) {
		this.typestatus = typestatus;
	}
	public String getVerbatimcoordinates() {
		return verbatimcoordinates;
	}
	public void setVerbatimcoordinates(String verbatimcoordinates) {
		this.verbatimcoordinates = verbatimcoordinates;
	}
	public String getVerbatimcoordinatesystem() {
		return verbatimcoordinatesystem;
	}
	public void setVerbatimcoordinatesystem(String verbatimcoordinatesystem) {
		this.verbatimcoordinatesystem = verbatimcoordinatesystem;
	}
	public String getVerbatimdepth() {
		return verbatimdepth;
	}
	public void setVerbatimdepth(String verbatimdepth) {
		this.verbatimdepth = verbatimdepth;
	}
	public String getVerbatimelevation() {
		return verbatimelevation;
	}
	public void setVerbatimelevation(String verbatimelevation) {
		this.verbatimelevation = verbatimelevation;
	}
	public String getVerbatimeventdate() {
		return verbatimeventdate;
	}
	public void setVerbatimeventdate(String verbatimeventdate) {
		this.verbatimeventdate = verbatimeventdate;
	}
	public String getVerbatimlatitude() {
		return verbatimlatitude;
	}
	public void setVerbatimlatitude(String verbatimlatitude) {
		this.verbatimlatitude = verbatimlatitude;
	}
	public String getVerbatimlocality() {
		return verbatimlocality;
	}
	public void setVerbatimlocality(String verbatimlocality) {
		this.verbatimlocality = verbatimlocality;
	}
	public String getVerbatimlongitude() {
		return verbatimlongitude;
	}
	public void setVerbatimlongitude(String verbatimlongitude) {
		this.verbatimlongitude = verbatimlongitude;
	}
	public String getVerbatimsrs() {
		return verbatimsrs;
	}
	public void setVerbatimsrs(String verbatimsrs) {
		this.verbatimsrs = verbatimsrs;
	}
	public String getVerbatimtaxonrank() {
		return verbatimtaxonrank;
	}
	public void setVerbatimtaxonrank(String verbatimtaxonrank) {
		this.verbatimtaxonrank = verbatimtaxonrank;
	}
	public String getVernacularname() {
		return vernacularname;
	}
	public void setVernacularname(String vernacularname) {
		this.vernacularname = vernacularname;
	}
	public String getWaterbody() {
		return waterbody;
	}
	public void setWaterbody(String waterbody) {
		this.waterbody = waterbody;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getSourcefileid() {
		return sourcefileid;
	}
	public void setSourcefileid(String sourcefileid) {
		this.sourcefileid = sourcefileid;
	}
	public String getDwcaid() {
		return dwcaid;
	}
	public void setDwcaid(String dwcaid) {
		this.dwcaid = dwcaid;
	}
}
