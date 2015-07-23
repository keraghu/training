package com.cts.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "training")
public class Training implements java.io.Serializable{

	@Id @GeneratedValue
	@Column(name = "training_id")
	 private int trainingId;
	
	@Column(name = "training_name")
	 private String trainingName;
	
	@Column(name = "training_desc")
	 private String trainingDesc;
	
	@Column(name = "duration")
	 private String duration;
	
	@Column(name = "start_dt")
	 private Date startDate;
	
	@Column(name = "end_dt")
	 private Date endDate;
	
	@Column(name = "mode")
	 private String mode;
	
	@Column(name = "pre_requisite")
	 private String preRequisite;
	
	@OneToOne
	@JoinColumn(name="certification_id")
	private Certification certification;
	
	@Column(name = "cost")
	 private double cost;

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getTrainingName() {
		return trainingName;
	}

	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}

	public String getTrainingDesc() {
		return trainingDesc;
	}

	public void setTrainingDesc(String trainingDesc) {
		this.trainingDesc = trainingDesc;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getPreRequisite() {
		return preRequisite;
	}

	public void setPreRequisite(String preRequisite) {
		this.preRequisite = preRequisite;
	}

	public Certification getCertification() {
		return certification;
	}

	public void setCertification(Certification certification) {
		this.certification = certification;
	}

	public int getTrainingId() {
		return trainingId;
	}
	
	
	
}
