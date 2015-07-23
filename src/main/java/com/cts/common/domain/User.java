package com.cts.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable{

@Id @GeneratedValue
@Column(name = "user_id")
 private int userId;

@Column(name = "user_name")
 private String userName;

@Column(name = "created_by")
 private String createdBy;

@Column(name = "created_dt")
 private Date createdDate;

@Column(name = "employee_id")
private int employeeId;

@Column(name = "email")
private String email;


@ManyToOne(cascade={CascadeType.ALL})
@JoinColumn(name="approver")
private User approver;

//private Set<UserTraining> userTrainingList=new HashSet<UserTraining>(0);

@OneToOne
@JoinColumn(name="role_id")
private Role role;

public Role getRole() {
	return role;
}

public void setRole(Role role) {
	this.role = role;
}

@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
private Set<UserTrainingRequest> userTrainingRequestList=new HashSet<UserTrainingRequest>(0);

@OneToMany(fetch = FetchType.LAZY, mappedBy = "associatedUser")
private Set<UserCertificationDetail> userCertificationList=new HashSet<UserCertificationDetail>(0);


public User() {

}

public int getUserId() {
 return userId;
 }
 public void setUserId(int userId) {
 this.userId = userId;
 }
 public String getUserName() {
 return userName;
 }
 public void setUserName(String userName) {
 this.userName = userName;
 }
 public String getCreatedBy() {
 return createdBy;
 }
 public void setCreatedBy(String createdBy) {
 this.createdBy = createdBy;
 }
 public Date getCreatedDate() {
 return createdDate;
 }
 public void setCreatedDate(Date createdDate) {
 this.createdDate = createdDate;
 
 
 }

public int getEmployeeId() {
	return employeeId;
}

public void setEmployeeId(int employeeId) {
	this.employeeId = employeeId;
}

public User getApprover() {
	return approver;
}

public void setApprover(User approver) {
	this.approver = approver;
}
 
 


public Set<UserTrainingRequest> getUserTrainingRequestList() {
	return this.userTrainingRequestList;
}

public void setUserTrainingRequestList(Set<UserTrainingRequest> userTrainingRequestList){
 this.userTrainingRequestList=userTrainingRequestList;
}

public Set<UserCertificationDetail> getUserCertificationList() {
	return this.userCertificationList;
}

public void setUserCertificationList(Set<UserCertificationDetail> userCertificationList){
 this.userCertificationList=userCertificationList;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}


}

