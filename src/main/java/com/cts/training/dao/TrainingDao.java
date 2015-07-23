package com.cts.training.dao;

import java.util.List;

import com.cts.common.domain.Training;
import com.cts.common.domain.User;
import com.cts.common.domain.UserTrainingApprovalQueue;
import com.cts.common.domain.UserTrainingRequest;

public interface TrainingDao {
	public List getUserCompletedTrainingList(String userName,int pageNo);
	public List getUserUpcomingTrainingList(String userName,int pageNo);
	public List getApprovedUpcomingTrainingList(String userName);
	public User getTrainingRequestApprover(String userName);
	public UserTrainingRequest updateTraining(String userName,String trainingName,String trainingStatus);
	public UserTrainingRequest updateTrainingRequestStatus(int reqId,String reqStatus);
	public long getPendingCount(String approverName);
	public List getPendingRequestList(String approverName);
	
public UserTrainingRequest submitTrainingRequest(String userName, String trainingName);
	
	public List<Training> getTrainingCourses();
	public User getUser(String userName);
	public void persistMessageInQueue(List<UserTrainingApprovalQueue> queueList);
}
