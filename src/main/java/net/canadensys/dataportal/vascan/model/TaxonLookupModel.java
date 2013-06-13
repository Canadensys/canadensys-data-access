/*
	Copyright (c) 2010 Canadensys
*/
package net.canadensys.dataportal.vascan.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Denormalized representation of a taxon
 * @author canadensys
 *
 */
@Entity
@Table(name="lookup")
public class TaxonLookupModel extends NestedSet{

	private String	calname;
	private String 	calnameauthor;
	private String	calnamehtml;
	private	String	calnamehtmlauthor;
	private String	calnameauthornoautonym;
	private Integer taxonId;
	private boolean	isLeaf;
	private String	status;
	private String	rank;
	private String	calhabit;
	private String	AB;
	private String	BC;
	private String	GL;
	private String	NL_L;
	private String	MB;
	private String	NB;
	private String	NL_N;
	private String	NT;
	private String	NS;
	private String	NU;
	private String	ON;
	private String	PE;
	private String	QC;
	private String	PM;
	private String	SK;
	private String	YT;
	private String	higherclassification;
	private String	classe;
	private String	order;
	private String	family;
	private String 	genus;
	private String	subgenus;
	private String	specificepithet;
	private String	infraspecificepithet;
	private String	author;
	private Date	cdate;
	private Date	mdate;
	
	
	public String getCalname() {
		return calname;
	}
	public void setCalname(String calname) {
		this.calname = calname;
	}

	public String getCalnameauthor() {
		return calnameauthor;
	}
	public void setCalnameauthor(String calnameauthor) {
		this.calnameauthor = calnameauthor;
	}

	public String getCalnamehtml() {
		return calnamehtml;
	}
	public void setCalnamehtml(String calnamehtml) {
		this.calnamehtml = calnamehtml;
	}

	public String getCalnamehtmlauthor() {
		return calnamehtmlauthor;
	}
	public void setCalnamehtmlauthor(String calnamehtmlauthor) {
		this.calnamehtmlauthor = calnamehtmlauthor;
	}
	
	public String getCalnameauthornoautonym() {
		return calnameauthornoautonym;
	}
	public void setCalnameauthornoautonym(String calnameauthornoautonym) {
		this.calnameauthornoautonym = calnameauthornoautonym;
	}

	/**
	 * @return the taxonId that this lookup represent
	 */
	@Id
	public Integer getTaxonId() {
		return taxonId;
	}
	public void setTaxonId(Integer taxonId) {
		this.taxonId = taxonId;
	}
	/**
	 * @return the isLeaf
	 */
	@Column(name="isleaf")
	public boolean isLeaf() {
		return isLeaf;
	}
	/**
	 * @param isLeaf the isLeaf to set
	 */
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}
	/**
	 * @return the calhabit
	 */
	public String getCalhabit() {
		return calhabit;
	}
	/**
	 * @param calhabit the calhabit to set
	 */
	public void setCalhabit(String calhabit) {
		this.calhabit = calhabit;
	}
	/**
	 * @return the aB
	 */
	public String getAB() {
		return AB;
	}
	/**
	 * @param aB the aB to set
	 */
	public void setAB(String aB) {
		AB = aB;
	}
	/**
	 * @return the bC
	 */
	public String getBC() {
		return BC;
	}
	/**
	 * @param bC the bC to set
	 */
	public void setBC(String bC) {
		BC = bC;
	}
	/**
	 * @return the gL
	 */
	public String getGL() {
		return GL;
	}
	/**
	 * @param gL the gL to set
	 */
	public void setGL(String gL) {
		GL = gL;
	}
	/**
	 * @return the nL_L
	 */
	public String getNL_L() {
		return NL_L;
	}
	/**
	 * @param nLL the nL_L to set
	 */
	public void setNL_L(String nLL) {
		NL_L = nLL;
	}
	/**
	 * @return the mB
	 */
	public String getMB() {
		return MB;
	}
	/**
	 * @param mB the mB to set
	 */
	public void setMB(String mB) {
		MB = mB;
	}
	/**
	 * @return the nB
	 */
	public String getNB() {
		return NB;
	}
	/**
	 * @param nB the nB to set
	 */
	public void setNB(String nB) {
		NB = nB;
	}
	/**
	 * @return the nL_N
	 */
	public String getNL_N() {
		return NL_N;
	}
	/**
	 * @param nLN the nL_N to set
	 */
	public void setNL_N(String nLN) {
		NL_N = nLN;
	}
	/**
	 * @return the nT
	 */
	public String getNT() {
		return NT;
	}
	/**
	 * @param nT the nT to set
	 */
	public void setNT(String nT) {
		NT = nT;
	}
	/**
	 * @return the nS
	 */
	public String getNS() {
		return NS;
	}
	/**
	 * @param nS the nS to set
	 */
	public void setNS(String nS) {
		NS = nS;
	}
	/**
	 * @return the nU
	 */
	public String getNU() {
		return NU;
	}
	/**
	 * @param nU the nU to set
	 */
	public void setNU(String nU) {
		NU = nU;
	}
	/**
	 * @return the oN
	 */
	@Column(name="`ON`")
	public String getON() {
		return ON;
	}
	/**
	 * @param oN the oN to set
	 */
	public void setON(String oN) {
		ON = oN;
	}
	/**
	 * @return the pE
	 */
	public String getPE() {
		return PE;
	}
	/**
	 * @param pE the pE to set
	 */
	public void setPE(String pE) {
		PE = pE;
	}
	/**
	 * @return the qC
	 */
	public String getQC() {
		return QC;
	}
	/**
	 * @param qC the qC to set
	 */
	public void setQC(String qC) {
		QC = qC;
	}
	/**
	 * @return the pM
	 */
	public String getPM() {
		return PM;
	}
	/**
	 * @param pM the pM to set
	 */
	public void setPM(String pM) {
		PM = pM;
	}
	/**
	 * @return the sK
	 */
	public String getSK() {
		return SK;
	}
	/**
	 * @param sK the sK to set
	 */
	public void setSK(String sK) {
		SK = sK;
	}
	/**
	 * @return the yT
	 */
	public String getYT() {
		return YT;
	}
	/**
	 * @param yT the yT to set
	 */
	public void setYT(String yT) {
		YT = yT;
	}
	/**
	 * @return the higherclassification
	 */
	public String getHigherclassification() {
		return higherclassification;
	}
	/**
	 * @param higherclassification the higherclassification to set
	 */
	public void setHigherclassification(String higherclassification) {
		this.higherclassification = higherclassification;
	}

	@Column(name="class")
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}

	@Column(name="_order")
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	/**
	 * @return the family
	 */
	public String getFamily() {
		return family;
	}
	/**
	 * @param family the family to set
	 */
	public void setFamily(String family) {
		this.family = family;
	}
	/**
	 * @return the genus
	 */
	public String getGenus() {
		return genus;
	}
	/**
	 * @param genus the genus to set
	 */
	public void setGenus(String genus) {
		this.genus = genus;
	}
	/**
	 * @return the subgenus
	 */
	public String getSubgenus() {
		return subgenus;
	}
	/**
	 * @param subgenus the subgenus to set
	 */
	public void setSubgenus(String subgenus) {
		this.subgenus = subgenus;
	}
	/**
	 * @return the specificepithet
	 */
	public String getSpecificepithet() {
		return specificepithet;
	}
	/**
	 * @param specificepithet the specificepithet to set
	 */
	public void setSpecificepithet(String specificepithet) {
		this.specificepithet = specificepithet;
	}
	/**
	 * @return the infraspecificepithet
	 */
	public String getInfraspecificepithet() {
		return infraspecificepithet;
	}
	/**
	 * @param infraspecificepithet the infraspecificepithet to set
	 */
	public void setInfraspecificepithet(String infraspecificepithet) {
		this.infraspecificepithet = infraspecificepithet;
	}
	
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the cdate
	 */
	public Date getCdate() {
		return cdate;
	}
	/**
	 * @param cdate the cdate to set
	 */
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	/**
	 * @return the mdate
	 */
	public Date getMdate() {
		return mdate;
	}
	/**
	 * @param mdate the mdate to set
	 */
	public void setMdate(Date mdate) {
		this.mdate = mdate;
	}
	
	@Override
	/**
	 * toString is mainly used in the admin backend, where a basic string representation
	 * is dumped to the logfile, in a before/after fashion (changed from / changed to) 
	 */
	public String toString() {
		String delimiter = " ";
		//String newline = "\n";
		StringBuffer lookup = new StringBuffer("");
		lookup.append(this.taxonId).append(delimiter);
		lookup.append(this.calname).append(delimiter);
		lookup.append(this.status).append(delimiter);
		lookup.append(this.rank).append(delimiter);
		lookup.append(this.calhabit).append(delimiter);	
		lookup.append(this.higherclassification).append(delimiter);
		lookup.append(this.classe).append(delimiter);
		lookup.append(this.order).append(delimiter);
		lookup.append(this.family).append(delimiter);
		lookup.append(this.genus).append(delimiter);
		lookup.append(this.subgenus).append(delimiter);
		lookup.append(this.specificepithet).append(delimiter);
		lookup.append(this.infraspecificepithet);
		return lookup.toString();
	}
}
