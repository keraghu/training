package com.cts.training.services.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.cts.common.domain.UserTrainingRequest;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;

public class MongoDBHelper {
	
	public MongoClient getMongoCleint() throws Exception{
		
		//String textUri = "mongodb://openshift:openshift5@ds047812.mongolab.com:47812/login4mongo";
		  String textUri = "mongodb://192.168.40.175/login4mongo";
		  MongoClientURI uri = new MongoClientURI(textUri);
		  MongoClient m = new MongoClient(uri);
		  m.setWriteConcern(WriteConcern.NONE);
		  return m;
	}
	
	public void insertUserTrainingAudit(UserTrainingRequest utr,String fromstatus,String tostatus){
		System.out.println("entering into insertUserTrainingLog");
		try {
			DB db = getMongoCleint().getDB("login4mongo");
			DBCollection table = db.getCollection("useraudit");
			BasicDBObject document = new BasicDBObject();
			populateAuditData(document, utr,fromstatus,tostatus);
			table.insert(document,WriteConcern.NONE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("exiting  insertUserTrainingLog");
		
	}
	public void populateAuditData(BasicDBObject document,UserTrainingRequest utr,String fromstatus,String tostatus){
		System.out.println("entering into populateAuditData");
		document.put("requestid", utr.getReqId());
		document.put("trainingname", utr.getTraining().getTrainingName());
		document.put("associatename", utr.getUser().getUserName());
		document.put("fromstatus ", fromstatus);
		document.put("updateddate", new Date());
		document.put("tostatus ", tostatus);
		
	}
	
	public void insertUserTrainingLog(UserTrainingRequest utr){
		System.out.println("entering into insertUserTrainingLog");
		try {
			DB db = getMongoCleint().getDB("login4mongo");
			DBCollection table = db.getCollection("user");
			BasicDBObject document = new BasicDBObject();
			populateData(document, utr);
			table.insert(document,WriteConcern.NONE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("exiting  insertUserTrainingLog");
		
	}
	
	public void populateData(BasicDBObject document,UserTrainingRequest utr){
		System.out.println("entering into populateData");
		document.put("requestid", utr.getReqId());
		document.put("trainingname", utr.getTraining().getTrainingName());
		document.put("associatename", utr.getUser().getUserName());
		document.put("approvername", utr.getApprover().getUserName());
		document.put("raiseddate", new Date());
		document.put("approveddate", "");
		document.put("trainingcost", utr.getTraining().getCost());
		document.put("completeddate", "");
		document.put("trainingstatus",utr.getTrainingStatus());
		document.put("statusupdateddate", new Date());
	}
	
	public void updateUserTrainingApproval(int reqId){
		System.out.println("entering into updateUserTrainingApproval");
		try {
			DB db = getMongoCleint().getDB("login4mongo");
			DBCollection table = db.getCollection("user");
			BasicDBObject query = new BasicDBObject();
			query.put("requestid", reqId);
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.put("approveddate", new Date());
			BasicDBObject updateObj = new BasicDBObject();
			updateObj.put("$set", newDocument);
		 
			table.update(query, updateObj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("exiting updateUserTrainingApproval");
	}
	
	public void updateUserTrainingLog(String userName,String trainingName,String trainingStatus){
		System.out.println(userName+"entering into updateUserTrainingLog"+trainingName);
		try {
			DB db = getMongoCleint().getDB("login4mongo");
			DBCollection table = db.getCollection("user");
			BasicDBObject document = new BasicDBObject();
			BasicDBObject query = new BasicDBObject();
			query.put("trainingname", trainingName);
			query.put("associatename", userName);
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.put("trainingstatus", trainingStatus);
			newDocument.put("completeddate", new Date());
			newDocument.put("statusupdateddate", new Date());
			BasicDBObject updateObj = new BasicDBObject();
			updateObj.put("$set", newDocument);
		 
			table.update(query, updateObj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("exiting updateUserTrainingLog");
		
	}
	
	
	
	public String getISOFormatDate(Date d){
		//DateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSZZ");
		//df.setTimeZone(TimeZone.getTimeZone("UTC"));
		//df.format(TimeDateUtils.getCurrentDate());
		
		  TimeZone tz = TimeZone.getTimeZone("UTC");
		    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		    df.setTimeZone(tz);
		    String nowAsISO = df.format(d);
		    return nowAsISO;
	}



}
