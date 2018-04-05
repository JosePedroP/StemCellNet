package databaseAccess;

// Generated 1/Jul/2015 12:11:11 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

/**
 * Interaction generated by hbm2java
 */
public class Interaction implements java.io.Serializable {

	private Integer id;
	private Proteins proteinsByPridB;
	private Proteins proteinsByPridA;
	private String regulatory;
	private Integer oldIntdisid;
	private Set methods = new HashSet(0);
	private Set pubmedrefs = new HashSet(0);
	private Set interactionSpecieses = new HashSet(0);
	private Set interactionConfscores = new HashSet(0);

	public Interaction() {
	}

	public Interaction(Proteins proteinsByPridB, Proteins proteinsByPridA) {
		this.proteinsByPridB = proteinsByPridB;
		this.proteinsByPridA = proteinsByPridA;
	}

	public Interaction(Proteins proteinsByPridB, Proteins proteinsByPridA,
			String regulatory, Integer oldIntdisid, Set methods,
			Set pubmedrefs, Set interactionSpecieses, Set interactionConfscores) {
		this.proteinsByPridB = proteinsByPridB;
		this.proteinsByPridA = proteinsByPridA;
		this.regulatory = regulatory;
		this.oldIntdisid = oldIntdisid;
		this.methods = methods;
		this.pubmedrefs = pubmedrefs;
		this.interactionSpecieses = interactionSpecieses;
		this.interactionConfscores = interactionConfscores;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Proteins getProteinsByPridB() {
		return this.proteinsByPridB;
	}

	public void setProteinsByPridB(Proteins proteinsByPridB) {
		this.proteinsByPridB = proteinsByPridB;
	}

	public Proteins getProteinsByPridA() {
		return this.proteinsByPridA;
	}

	public void setProteinsByPridA(Proteins proteinsByPridA) {
		this.proteinsByPridA = proteinsByPridA;
	}

	public String getRegulatory() {
		return this.regulatory;
	}

	public void setRegulatory(String regulatory) {
		this.regulatory = regulatory;
	}

	public Integer getOldIntdisid() {
		return this.oldIntdisid;
	}

	public void setOldIntdisid(Integer oldIntdisid) {
		this.oldIntdisid = oldIntdisid;
	}

	public Set getMethods() {
		return this.methods;
	}

	public void setMethods(Set methods) {
		this.methods = methods;
	}

	public Set getPubmedrefs() {
		return this.pubmedrefs;
	}

	public void setPubmedrefs(Set pubmedrefs) {
		this.pubmedrefs = pubmedrefs;
	}

	public Set getInteractionSpecieses() {
		return this.interactionSpecieses;
	}

	public void setInteractionSpecieses(Set interactionSpecieses) {
		this.interactionSpecieses = interactionSpecieses;
	}

	public Set getInteractionConfscores() {
		return this.interactionConfscores;
	}

	public void setInteractionConfscores(Set interactionConfscores) {
		this.interactionConfscores = interactionConfscores;
	}

}
