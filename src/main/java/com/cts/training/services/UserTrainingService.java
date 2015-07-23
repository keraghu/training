package com.cts.training.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.cts.common.domain.Training;
import com.cts.common.domain.User;
import com.cts.common.domain.UserTrainingApprovalQueue;
import com.cts.common.domain.UserTrainingRequest;
import com.cts.training.dao.TrainingDao;
import com.cts.training.dao.TrainingDaoImpl;
import com.cts.training.services.helper.MessageHelper;
import com.cts.training.services.helper.MongoDBHelper;
import com.cts.training.services.helper.SendGridMailHelper;

@Path("/service")
public class UserTrainingService {
	MongoDBHelper mongoHelper=new MongoDBHelper();
	@GET
    @Path("/getList")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getCompletedTrainingList(@QueryParam("userName")  String userName,@QueryParam("pageNumber")  String pageNumber){
		TrainingDao trainingdao=new TrainingDaoImpl();
		List list=trainingdao.getUserCompletedTrainingList(userName,Integer.parseInt(pageNumber));
		//if(list.size()>0){
			Iterator it=list.iterator();
			JSONArray arr=new JSONArray();
			while(it.hasNext()){
				UserTrainingRequest utr=(UserTrainingRequest)it.next();
				JSONObject obj=new JSONObject();
				obj.put("TrainingName", utr.getTraining().getTrainingName());
				obj.put("TrainingStartDate", getDate(utr.getTraining().getStartDate()));
				obj.put("TrainingEndDate", getDate(utr.getTraining().getEndDate()));
				obj.put("TrainingMode", utr.getTraining().getMode());
				
				arr.add(obj);
			}
			JSONArray arr1=getUpcomingTrainingList(userName, pageNumber);
			
			JSONObject grp=new JSONObject();
			grp.put("CompletedList", arr);
			grp.put("UpcomingList", arr1);
		
		//}
		return grp;
	}
	
	private String getDate(Date d){
		
		 String pattern = "MM/dd/yyyy";
		 String res=null;
		 SimpleDateFormat format = new SimpleDateFormat(pattern);
		 res = format.format(d);
		return res;
	}
	
	
	public JSONArray getUpcomingTrainingList(  String userName, String pageNumber){
		TrainingDao trainingdao=new TrainingDaoImpl();
		List list=trainingdao.getUserUpcomingTrainingList(userName,Integer.parseInt(pageNumber));
		//if(list.size()>0){
			Iterator it=list.iterator();
			JSONArray arr=new JSONArray();
			while(it.hasNext()){
				Training t=(Training)it.next();
				JSONObject obj=new JSONObject();
				obj.put("TrainingName", t.getTrainingName());
				obj.put("TrainingStartDate", getDate(t.getStartDate()));
				obj.put("TrainingEndDate", getDate(t.getEndDate()));
				obj.put("TrainingMode", t.getMode());
				arr.add(obj);
			}
		
		//}
		return arr;
	}
	
	@GET
    @Path("/getManageTrainings")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getApprovedUpcomingTrainingList(@QueryParam("userName")  String userName){
		TrainingDao trainingdao=new TrainingDaoImpl();
		List list=trainingdao.getApprovedUpcomingTrainingList(userName);
		User user=trainingdao.getTrainingRequestApprover(userName);
		//if(list.size()>0){
			Iterator it=list.iterator();
			JSONArray arr=new JSONArray();
			while(it.hasNext()){
				UserTrainingRequest utr=(UserTrainingRequest)it.next();
				JSONObject obj=new JSONObject();
				obj.put("TrainingName", utr.getTraining().getTrainingName());
				obj.put("TrainingStartDate", getDate(utr.getTraining().getStartDate()));
				obj.put("TrainingEndDate", getDate(utr.getTraining().getEndDate()));
				obj.put("TrainingMode", utr.getTraining().getMode());
				obj.put("ApprovedDate", getDate(utr.getApprovedDate()));
				arr.add(obj);
			}
			JSONObject userJson=new JSONObject();
			userJson.put("UserName", user.getApprover().getUserName());
			userJson.put("Email", user.getApprover().getEmail());
			
			
			
			JSONObject grp=new JSONObject();
			grp.put("AprrovedIncompleteList", arr);
			grp.put("Approver", userJson);
			
		//}
		return grp;
	}
	
	@GET
    @Path("/updateTrainings")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject UpdateTraining(@QueryParam("userName")  String userName,@QueryParam("trainingName")  String trainingName,@QueryParam("status")  String trainingStatus){
		TrainingDao trainingdao=new TrainingDaoImpl();
		System.out.println("trainingName="+trainingName);
		UserTrainingRequest utr=trainingdao.updateTraining(userName, trainingName, trainingStatus);
		mongoHelper.updateUserTrainingLog(userName, trainingName, trainingStatus);
		if(trainingStatus.equals("Completed"))
		 mongoHelper.insertUserTrainingAudit(utr, "Approved", "Completed");
		
		JSONObject obj=getCompletedTrainingList(userName,"1");
		return obj;
	}
	@GET
    @Path("/pendingTrainingCount")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getPendingCount(@QueryParam("userName")  String userName){
		TrainingDao trainingdao=new TrainingDaoImpl();
		long cnt=trainingdao.getPendingCount(userName);
		JSONObject grp=new JSONObject();
		grp.put("PendingRequestCount", cnt);
		
		return grp;
		
	}
	
	@GET
    @Path("/pendingTrainingList")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getPendingRequestList(@QueryParam("userName")  String userName){
		TrainingDao trainingdao=new TrainingDaoImpl();
		List list=trainingdao.getPendingRequestList(userName);
		Iterator it=list.iterator();
		JSONArray arr=new JSONArray();
		while(it.hasNext()){
			UserTrainingApprovalQueue utau=(UserTrainingApprovalQueue)it.next();
			JSONObject obj=new JSONObject();
			obj.put("AssociateName", utau.getSubmittedUser().getUserName());
			obj.put("TrainingName", utau.getTraining().getTrainingName());
			obj.put("TrainingDesc", utau.getTraining().getTrainingDesc());
			obj.put("TrainingDate", getDate(utau.getTraining().getStartDate()));
			obj.put("ReqId", utau.getUserTrainingRequest().getReqId());
			arr.add(obj);
		}
		
		return arr;
	}
	
	@GET
    @Path("/approveRequest")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray approveRejectTrainingRequest(@QueryParam("userName")  String userName,@QueryParam("reqId")  String reqId,@QueryParam("reqStatus")  String reqStatus){
		TrainingDao trainingdao=new TrainingDaoImpl();
		UserTrainingRequest utr=trainingdao.updateTrainingRequestStatus(Integer.parseInt(reqId), reqStatus);
		User u=trainingdao.getUser(userName);
		if(reqStatus.equals("Approved")){
			utr.setUser(u);
			mongoHelper.updateUserTrainingApproval(Integer.parseInt(reqId));
			mongoHelper.insertUserTrainingAudit(utr, "Pending", "Approved");
            SendGridMailHelper helper=new SendGridMailHelper();
            
            helper.sendMail(u.getEmail(), "Training Request Approval", "Training Request Id : "+reqId+" is Approved");
            }else{
            	mongoHelper.insertUserTrainingAudit(utr, "Pending", "Rejected");
                   SendGridMailHelper helper=new SendGridMailHelper();
                   helper.sendMail(u.getEmail(), "Training Request rejected", "Training Requset Id : "+reqId+" is Rejected");   
        }
		return getPendingRequestList(userName);
	}

	 @GET
	 @Path("/submitTraining")
	 @Produces(MediaType.APPLICATION_JSON) 
	public JSONObject submitTrainingRequest(@QueryParam("userName")  String userName,@QueryParam("trainingName")  String trainingName){
		 System.out.println("In submitTrainingRequest TrainingService Entry>>>>>>>");
		 List<UserTrainingApprovalQueue> queueList = new ArrayList<UserTrainingApprovalQueue>();
		 MessageHelper helper = new MessageHelper();
		 TrainingDao trainingDao = new TrainingDaoImpl();
		 UserTrainingRequest utr= trainingDao.submitTrainingRequest(userName, trainingName);
		 mongoHelper.insertUserTrainingLog(utr);
		 mongoHelper.insertUserTrainingAudit(utr, "", "Pending");
		 System.out.println("ReqId:>>>>>>>"+utr.getReqId());
			JSONObject userReq=new JSONObject();
			userReq.put("ReqId", utr.getReqId());
			userReq.put("TrainingName", utr.getTraining().getTrainingName());
			userReq.put("ReqStatus",  utr.getReqStatus());
			 JSONObject grp=new JSONObject();
			 grp.put("userTrainReq", userReq);
			
			 // sending Rabitt MQ Messages here
			// String message="Approver name: Rajendran,Training name:Java,Associate name: Sabari Vijayan";
			 String message= "TrainingRequestId="+utr.getReqId()+","+"ApproverName="
			                     +utr.getUser().getApprover().getUserName()+","+"Submitter="+userName+","
			                     +"TrainingName="+trainingName+"CreateDate="+new Date().toString();
			 System.out.println("Input message:>>>>>>>"+message);
	//		 String output = callPushMessagingService(message);
			 //System.out.println("output:>>>>>>>"+output);
			// System.out.println("Message Sent to MQServer Successfully:>>>>>>>");
				
			 try{
				// send approver queue in Rabit MQ
				pushMessage(message);
			    List<String> messageList = getPullMessages();
			    for(String msg : messageList){
			    		System.out.println("Input Message is >>>>>>>>>:"+msg);
			    }
			    queueList = helper.prepareApproverQueueMessage(messageList);
			    for(UserTrainingApprovalQueue trainAppQue : queueList){
		    		System.out.println("Request ID >>>>>>>>>:"+trainAppQue.getUserTrainingRequest().getReqId());
		        }
			 
			 }catch(Exception ex){
				 ex.printStackTrace();
			 }
			/* 
			 List<UserTrainingApprovalQueue> queueList = populateQueueData();*/
			
			 trainingDao.persistMessageInQueue(queueList);
			 
			// System.out.println("output:>>>>>>>"+output);
			// System.out.println("Message Sent to MQServer Successfully:>>>>>>>");
			 System.out.println("In submitTrainingRequest TrainingService Exit>>>>>>>");
		return grp;
	}
	 
	 
	 @GET
	 @Path("/getTrainingCourses")
	 //@Consumes(MediaType.APPLICATION_JSON)
	 @Produces(MediaType.APPLICATION_JSON) 
	public JSONObject getTrainingCourses(@QueryParam("userName")  String userName){
		 System.out.println(">>>>>>In getTrainingCourses Service Entry >>>>>>>>>>>>");
		 System.out.println(">>>>>>Username from url:"+userName);
		
		 TrainingDao trainingDao = new TrainingDaoImpl();
		 List<Training> trainingList = trainingDao.getTrainingCourses();
		User user=trainingDao.getTrainingRequestApprover(userName);
		
		 System.out.println(">>>>>>Approver UserName >>>>>>>>>>>>:"+user.getApprover().getUserName());
			Iterator it=trainingList.iterator();
			JSONArray arr=new JSONArray();
			while(it.hasNext()){
				Training training =(Training)it.next();
				JSONObject obj=new JSONObject();
				System.out.println("Certifications:"+training.getCertification().getCertificationName());
				obj.put("TrainingName", training.getTrainingName());
				obj.put("Duration", training.getDuration());
				obj.put("TrainingStartDate", getDate(training.getStartDate()));
				obj.put("TrainingEndDate", getDate(training.getEndDate()));
				obj.put("TrainingMode", training.getMode());
				obj.put("Prerequesties", training.getPreRequisite());
				obj.put("Certification", training.getCertification().getCertificationName());
				// To be populated the Certification
				//obj.put("ApprovedDate", getDate(utr.getApprovedDate()));
				arr.add(obj);
			}
		
			JSONObject userJson=new JSONObject();
			userJson.put("UserName", user.getApprover().getUserName());
			userJson.put("UserId", user.getUserId());
			userJson.put("Email", user.getApprover().getEmail());
			
			
			
			JSONObject grp=new JSONObject();
			grp.put("courseNameList", arr);
			grp.put("Approver", userJson);
			 System.out.println(">>>>>>In getTrainingCourses Service Exit >>>>>>>>>>>>");
			 
			 // Rabbit MQ Message Sending
			//  publishMessageToMQ();
			 
			 
		//}
		return grp;
	}
	 
	 private void pushMessage(String message) throws Exception {
		 
			//	String url = "http://localhost:8080/Messaging/service/pushMessage";
			
		 
				//String url = "http://jbossas-nagsopenshift.rhcloud.com/rest/service/pushMessage";
			 	String url = "http://messageservice.pivotalapps.ucp.local/rest/service/pushMessage";
				 final String USER_AGENT = "Mozilla/5.0";
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		 
				//add reuqest header
				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		 
				// String urlParameters = "sn=C02G8416DRJM&cn=hjhj&locale=jnkjkjk&caller=jkjk&num=12345";
				 String urlParameters = "key="+message;
				// Send post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();
		 
				int responseCode = con.getResponseCode();
				System.out.println("\nSending 'POST' request to URL : " + url);
				System.out.println("Post parameters : " + urlParameters);
				System.out.println("Response Code : " + responseCode);
		 
				BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
		 
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
		 
				//print result
				System.out.println(response.toString());
		 
			}
	 
	 private List<String> getPullMessages(){
			
		  RestTemplate restTemplate = new RestTemplate();
		//  String url="http://localhost:8080/Messaging/service/pullMessage";   
		//String url = "http://jbossas-nagsopenshift.rhcloud.com/rest/service/pullMessage";
		  String url = "http://messageservice.pivotalapps.ucp.local/rest/service/pullMessage";
		//  List<LinkedHashMap> messages=restTemplate.getForObject(url, List.class);
		  List<String> messages=restTemplate.getForObject(url, List.class);
		  System.out.println("Messages Size >>>>>:"+messages.size());
		  return messages;
	}

}
