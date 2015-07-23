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
@Table(name = "user_certification_dtl_tb")
public class UserCertificationDetail implements java.io.Serializable{
	
	@Id @GeneratedValue
	@Column(name = "user_certification_id")
	 private int userCertificationId;
	
	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public void setAssociatedUser(User user) {
		this.associatedUser = user;
	}

	public void setCertification(Certification certification) {
		this.certification = certification;
	}

	@Column(name = "completed_dt")
	 private Date completedDate;
	
	@Column(name = "score")
	 private String score;
	
	private User associatedUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getAssociatedUser() {
		return this.associatedUser;
	}
	
	private Certification certification;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "certification_id", nullable = false)
	public Certification getCertification(){
		return this.certification;
	}

	public int getUserCertificationId() {
		return userCertificationId;
	}

	
	
}
