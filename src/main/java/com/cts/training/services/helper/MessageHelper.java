package com.cts.training.services.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.cts.common.domain.Training;
import com.cts.common.domain.User;
import com.cts.common.domain.UserTrainingApprovalQueue;
import com.cts.common.domain.UserTrainingRequest;

public class MessageHelper {

	public List<com.cts.common.domain.UserTrainingApprovalQueue> prepareApproverQueueMessage(List<String> messageList){
		System.out.println("In parseMessage Entry >>>>>>>>> ");
		String[] subMsgArr = new String[5];
		List<UserTrainingApprovalQueue> traQueLst = new ArrayList<UserTrainingApprovalQueue>(); 
		com.cts.common.domain.UserTrainingApprovalQueue trainAppQueue = null;
		com.cts.common.domain.UserTrainingRequest userTrainRequest =null;
		com.cts.common.domain.User user = null;
		Training training=null;
		String reqId = null;
		String apprName = null;
		String submittedUser = null;
		String trainingName = null;
		String createDt = null;
		String expectedPattern = "MM/dd/yyyy";
	    SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
		for(String msg : messageList){
    		System.out.println("Message Content is >>>>>>>>>:"+msg);
    		trainAppQueue = new UserTrainingApprovalQueue();
    		user = new com.cts.common.domain.User();
    		training = new Training();
    		userTrainRequest = new UserTrainingRequest();
    		subMsgArr = parseMessage(msg);    
    		reqId = subMsgArr[0];
    		apprName = subMsgArr[1];
    		submittedUser = subMsgArr[2];
    		trainingName = subMsgArr[3];
    		createDt = subMsgArr[4];
    		Date date = new Date();
    		//date.parse(createDt);
    	   		 
    		trainAppQueue.setCreatedDate(date);
    		// setting attr for approve queue 
    		userTrainRequest.setReqId(Integer.parseInt(reqId));
    		trainAppQueue.setUserTrainingRequest(userTrainRequest);
    		user.setUserName(submittedUser);
    		trainAppQueue.setSubmittedUser(user);
    		training.setTrainingName(trainingName);
    		trainAppQueue.setTraining(training);
    		traQueLst.add(trainAppQueue);
        }
		System.out.println("In parseMessage Exit >>>>>>>>> ");
		return traQueLst;
	}
	
	private String[] parseMessage(String msg){
		System.out.println("In parseMessage Entry >>>>>>>>> ");
		// String[] schoolbag = new String[4];
		
		String[] msgArr = new String[5];
		StringTokenizer st = new StringTokenizer(msg,",");
		int count = st.countTokens();
		System.out.println("Token Count:"+count);
		int i=0;
		while (st.hasMoreElements()) {
			msgArr[i] = (String)st.nextElement();
			// arrEle.add((String)st.nextElement());
			 System.out.println("Tokens:"+msgArr[i]);
			i++;
		}
		String[] subMsgArr = new String[10];
		 StringTokenizer st1 = null;
		 int k=0;
		for(int j=0;j<count;j++){
			 st1 = new StringTokenizer(msgArr[j],"=");
			 System.out.println("Tokens in each loop:"+msgArr[j]);
			 while (st1.hasMoreElements()) {
					subMsgArr[k] = (String)st1.nextElement();
					System.out.println("Each Sub Tokens is:"+subMsgArr[k]);
					
				}
			 k++;
		}
		System.out.println("Total Sub Tokens are: "+subMsgArr[0]+" "+subMsgArr[1]+" "+subMsgArr[2]+" "+subMsgArr[3]+" "+subMsgArr[4]);
		System.out.println("In parseMessage Exit >>>>>>>>> ");
		return subMsgArr;
	
	}
	
	public static void main(String args[]){
		MessageHelper msgh = new MessageHelper();
		List<UserTrainingApprovalQueue> traQueLst = new ArrayList<UserTrainingApprovalQueue>(); 
		String[] subMsgArr = new String[10];
		String msg = "TrainingRequestId-71,ApproverName-rajendra,Submitter-sabari vijayan,TrainingName-AWS Essentials,CreateDate-Sat Jun 06 17:58:59 IST 2015";
		// subMsgArr = msgh.parseMessage(msg);
		List<String> messageList = new ArrayList<String>();
		messageList.add(msg);
		traQueLst = msgh.prepareApproverQueueMessage(messageList);
		
		  for(UserTrainingApprovalQueue trainAppQue : traQueLst){
	    		System.out.println("Request ID >>>>>>>>>:"+trainAppQue.getUserTrainingRequest().getReqId());
	    		System.out.println("UserName>>>>>>>>>:"+trainAppQue.getSubmittedUser().getUserName());
	        }
		// msgh.parseMessage(msg);
		/*
		for(int j=0;j<subMsgArr.length;j++){
			System.out.println("Message"+j+":"+subMsgArr[j]+"");
		}*/
	}
	
}
