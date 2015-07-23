package com.cts.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "certifications")
public class Certification implements java.io.Serializable{
	@Id @GeneratedValue
	@Column(name = "certification_id")
	 private int certificationId;
	
	@Column(name = "certification_name")
	 private String certificationName;
	
	@Column(name = "certification_desc")
	 private String certificationDesc;
	
	@Column(name = "IsReimbursed")
	 private boolean isReimbursed;
	
	@Column(name = "Ismandatory")
	 private boolean isMandatory;
	
	@Column(name = "cost")
	 private double cost;

	public String getCertificationName() {
		return certificationName;
	}

	public void setCertificationName(String certificationName) {
		this.certificationName = certificationName;
	}

	public String getCertificationDesc() {
		return certificationDesc;
	}

	public void setCertificationDesc(String certificationDesc) {
		this.certificationDesc = certificationDesc;
	}

	public boolean isReimbursed() {
		return isReimbursed;
	}

	public void setReimbursed(boolean isReimbursed) {
		this.isReimbursed = isReimbursed;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getCertificationId() {
		return certificationId;
	}
	
	
	
}
