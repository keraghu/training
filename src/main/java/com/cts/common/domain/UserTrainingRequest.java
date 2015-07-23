package com.cts.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_training_req_tb")
public class UserTrainingRequest implements java.io.Serializable{
	@Id @GeneratedValue
	@Column(name = "req_id")
	 private int reqId;
	
	@Column(name = "req_status")
	 private String reqStatus="Pending";
	
	@Column(name = "approved_dt")
	 private Date approvedDate;
	
	@Column(name = "training_status")
	private String trainingStatus="Not Completed";
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "training_id", nullable = false)
	private Training training;
	
	@Column(name = "created_dt")
	 private Date createdDate=new Date();
	
	@Column(name = "completed_dt")
	private Date completedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approver_id", nullable = false)
	private User approver;
	
	public User getUser() {
		return this.user;
	}
	
	
	public Training getTraining(){
		return this.training;
	}

	public String getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getTrainingStatus() {
		return trainingStatus;
	}

	public void setTrainingStatus(String trainingStatus) {
		this.trainingStatus = trainingStatus;
	}

	public int getReqId() {
		return reqId;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setTraining(Training training) {
		this.training = training;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public Date getCompletedDate() {
		return completedDate;
	}


	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}


	public User getApprover() {
		return approver;
	}


	public void setApprover(User approver) {
		this.approver = approver;
	}
	
	public void setReqId(int reqId) {
		this.reqId = reqId;
	}

}
