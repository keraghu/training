package com.cts.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_training_approval_queue_tb")
public class UserTrainingApprovalQueue {
	@Id @GeneratedValue
	@Column(name = "user_queue_id")
	 private int userQueueId;
	
	@Column(name = "created_dt")
	private Date createdDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "submitter_id", nullable = false)
	private User submittedUser;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approver_id", nullable = false)
	private User approver;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "training_id", nullable = false)
	private Training training;
	
	@OneToOne
	@JoinColumn(name="training_req_id")
	private UserTrainingRequest userTrainingRequest;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getSubmittedUser() {
		return submittedUser;
	}

	public void setSubmittedUser(User submittedUser) {
		this.submittedUser = submittedUser;
	}

	public User getApprover() {
		return approver;
	}

	public void setApprover(User approver) {
		this.approver = approver;
	}

	public Training getTraining() {
		return training;
	}

	public void setTraining(Training training) {
		this.training = training;
	}

	public int getUserQueueId() {
		return userQueueId;
	}

	public UserTrainingRequest getUserTrainingRequest() {
		return userTrainingRequest;
	}

	public void setUserTrainingRequest(UserTrainingRequest userTrainingRequest) {
		this.userTrainingRequest = userTrainingRequest;
	}

	
	

}
