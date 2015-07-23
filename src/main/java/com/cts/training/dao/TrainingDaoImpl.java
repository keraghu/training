package com.cts.training.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.cts.common.domain.Training;
import com.cts.common.domain.User;
import com.cts.common.domain.UserTrainingApprovalQueue;
import com.cts.common.domain.UserTrainingRequest;
import com.cts.training.dao.HibernateSessionManager;

public class TrainingDaoImpl implements TrainingDao {
	final static int PAGE_SIZE=10;

	public List getUserCompletedTrainingList(
			String userName,int pageNo) {
		SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
	     session.beginTransaction();
	     String hql = "select utr FROM com.cts.common.domain.UserTrainingRequest as utr"
	     		+ "   inner join utr.user as u where utr.trainingStatus=:status and u.userName=:name";
	     Query query = session.createQuery(hql);
	     query.setParameter("status", "Completed");
	     query.setParameter("name", userName);
	     query.setFirstResult((pageNo - 1) * PAGE_SIZE);
		 query.setMaxResults(PAGE_SIZE);
		return query.list();
	}
	
	public void persistMessageInQueue(List<UserTrainingApprovalQueue> queueList){
		 System.out.println("In persistMessageInQueue Entry>>>>>>>");
			SessionFactory sf = null;
			Session session = null;
			UserTrainingApprovalQueue taq = null;
			
		 try{
			   sf = HibernateSessionManager.getSessionFactory();
			   System.out.println("In try block>>>>>>>");
			 session = sf.openSession();
		     session.beginTransaction();
		     for (int i=0; i< queueList.size(); i++)
           {
		    	     System.out.println("In For loop Entry>>>>>>>");
		    	     taq = new UserTrainingApprovalQueue();
		    	     String trainingName =  queueList.get(i).getTraining().getTrainingName();
                   String userName = queueList.get(i).getSubmittedUser().getUserName();
                   int reqId = queueList.get(i).getUserTrainingRequest().getReqId();
                  // String apprName = queueList.get(i).getApprover().getUserName();
                   Date createDt =  queueList.get(i).getCreatedDate();
              	 User user=getUser(userName,session);
      			 System.out.println("User>>>>>>>:"+user.getUserId());
      			// Training training =getTraining(trainingName, session);
      			
      			 UserTrainingRequest utr = getTrainingRequest(reqId,session);
      			 System.out.println("Training Name>>>>>>>:"+utr.getTraining().getTrainingName());
      			 //taq.getUserTrainingRequest().setReqId(reqId);
      			 taq.setSubmittedUser(user);
      			 taq.setApprover(user.getApprover());
      			// taq.setTraining(training);
      			 taq.setTraining(utr.getTraining());
      			 taq.setUserTrainingRequest(utr);
      			 taq.setCreatedDate(createDt);
      			 session.saveOrUpdate(taq);
      			  System.out.println("Saved in Queue>>>>>>>");
           }
		    session.getTransaction().commit();
			}catch(HibernateException ex){
				System.out.println("In Exception block>>>>>>>>>>");
				 session.getTransaction().rollback();
				ex.printStackTrace();
		
			}finally{
				System.out.println("In finally block>>>>>>>>>>");
				  session.close();
			}
		 System.out.println("In persistMessageInQueue Exit>>>>>>>");
	}
	
	public List getUserUpcomingTrainingList(
			String userName,int pageNo) {
		SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
	     session.beginTransaction();
	     /* String hql = "select utr FROM com.cts.common.domain.UserTrainingRequest as utr"
  		+ "   inner join utr.user as u where utr.trainingStatus=:status and u.userName=:name";*/
	     String hql = "FROM com.cts.common.domain.Training t where t.startDate>current_date ";
	     Query query = session.createQuery(hql);
	     //  query.setParameter("status", "Not Completed");
	     // query.setParameter("name", userName);
	     query.setFirstResult((pageNo - 1) * PAGE_SIZE);
		 query.setMaxResults(PAGE_SIZE);
		return query.list();
		
	}
	
	public List getApprovedUpcomingTrainingList(String userName){
		SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
	     session.beginTransaction();
	     String hql = "select utr FROM com.cts.common.domain.UserTrainingRequest as utr"
		     		+ "   inner join utr.user as u where utr.trainingStatus=:status and utr.reqStatus=:reqStatus and u.userName=:name";
	     Query query = session.createQuery(hql);
	     query.setParameter("status", "Not Completed");
	     query.setParameter("reqStatus", "Approved");
	     query.setParameter("name", userName);
	    // query.setFirstResult((pageNo - 1) * PAGE_SIZE);
		 //query.setMaxResults(PAGE_SIZE);
		return query.list();
	}
	
	public User getTrainingRequestApprover(String userName){
		System.out.println("In getTrainingRequestApprover() Entry in TrainingDaoImpl  ");
		User user = null;
		//int userId = Integer.parseInt(userID);
		 SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
		 session.beginTransaction();
		 String hql = "FROM com.cts.common.domain.User u where u.userName=:userName";
		 Query query = session.createQuery(hql);
		 query.setParameter("userName", userName);
		 System.out.println("List size:"+query.list().size());
		 if(query.list().size()>0)
			 user = ((User)(query.list().get(0)));
		 else
			 user=new User();
		 System.out.println("User id>>>>>>>>>>:"+user.getApprover().getUserId());
		 session.getTransaction().commit();
		  session.close();
		  return user;
	}
	
	public UserTrainingRequest getTrainingRequest(int reqId, Session session){
		  System.out.println("In getTrainingRequest Entry>>>>>>>");
		  String hql = "FROM com.cts.common.domain.UserTrainingRequest utr where utr.reqId=:reqId";
			Query query = session.createQuery(hql);
			query.setParameter("reqId", reqId);
			UserTrainingRequest utr = ((UserTrainingRequest)(query.list().get(0)));
			System.out.println("In getTrainingRequest  Exit>>>>>>>");
			return utr;
	}
	
	public User getUser(String userName){
		SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
		 session.beginTransaction();
		String hql = "FROM com.cts.common.domain.User u where u.userName=:userName";
		Query query = session.createQuery(hql);
		query.setParameter("userName", userName);
		User user = ((User)(query.list().get(0)));
		session.getTransaction().commit();
		  session.close();
		return user;
		
	}
	
	public User getUser(String userName,Session session){
		String hql = "FROM com.cts.common.domain.User u where u.userName=:userName";
		Query query = session.createQuery(hql);
		query.setParameter("userName", userName);
		User user = ((User)(query.list().get(0)));
		return user;
		
	}
	
	public Training getTraining(String trainingName,Session session){
		String hql = "FROM com.cts.common.domain.Training t where t.trainingName=:name";
		Query query = session.createQuery(hql);
		query.setParameter("name", trainingName);
		Training t = ((Training)(query.list().get(0)));
		return t;
		
	}
	
	public UserTrainingRequest updateTraining(String userName,String trainingName,String trainingStatus){
		 SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
		 session.beginTransaction();
		 User user=getUser(userName,session);
		 Training t =getTraining(trainingName, session);
		 String hql = "select utr FROM com.cts.common.domain.UserTrainingRequest as utr"
		     		+ "   inner join utr.user as u inner join utr.training t where  u.userName=:userName and t.trainingName=:tname";
			Query query = session.createQuery(hql);
			query.setParameter("userName", userName);
			query.setParameter("tname", trainingName);
			UserTrainingRequest utr=(UserTrainingRequest)(query.list().get(0));
			utr.setTrainingStatus(trainingStatus);
		   session.update(utr);
		   session.getTransaction().commit();
		  session.close();
		  return utr;
	}
	
	public List getPendingRequestList(String approverName){
		 SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
		 session.beginTransaction();
		 String hql = "select utau FROM com.cts.common.domain.UserTrainingApprovalQueue as utau"
		     		+ "   inner join utau.approver as a inner join utau.userTrainingRequest utr join fetch utau.submittedUser su  join fetch utau.training t "
		     		+ "where  a.userName=:userName and utr.reqStatus=:status";
		 Query query = session.createQuery(hql);
			query.setParameter("userName", approverName);
			query.setParameter("status", "Pending");
			List list=	query.list();
			session.getTransaction().commit();
			  session.close();
			return list;
	}
	
	public long getPendingCount(String approverName){
		SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
		 session.beginTransaction();
		 String hql = "select count(utau) FROM com.cts.common.domain.UserTrainingApprovalQueue as utau"
		     		+ "   inner join utau.approver as a inner join utau.userTrainingRequest utr where  a.userName=:userName and utr.reqStatus=:status";
		 Query query = session.createQuery(hql);
			query.setParameter("userName", approverName);
			query.setParameter("status", "Pending");
			List list=	query.list();
			long cnt=(Long)(list.get(0));
			session.getTransaction().commit();
			  session.close(); 
		 
		 return cnt;
	}
	
	public UserTrainingRequest updateTrainingRequestStatus(int reqId,String reqStatus){
		 SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
		 session.beginTransaction();
		String hql = "FROM com.cts.common.domain.UserTrainingRequest utr where utr.reqId=:reqId";
		Query query = session.createQuery(hql);
		query.setParameter("reqId", reqId);
		UserTrainingRequest utr=(UserTrainingRequest)(query.list().get(0));
		System.out.println("to fetch training name "+utr.getTraining().getTrainingName());
		utr.setReqStatus(reqStatus);
		utr.setApprovedDate(new Date());
		session.save(utr);
		 session.getTransaction().commit();
		  session.close();
		  return utr;
	}
	
	
	public UserTrainingRequest submitTrainingRequest(String userName, String trainingName){
		 System.out.println("In submitTrainingRequest TrainingDaoImpl Entry>>>>>>>");
		UserTrainingRequest usrTrainReq = new UserTrainingRequest();
		SessionFactory sf = null;
		Session session = null;
		//usrTrainReq.set
		//Training training = getTrainingDetails(trainingName);
		try{
		   sf = HibernateSessionManager.getSessionFactory();
		   System.out.println("In try block>>>>>>>");
		 session = sf.openSession();
	     session.beginTransaction();
		 User user=getUser(userName,session);
		// User approver=getUser(user.get,session);
		 System.out.println("User>>>>>>>:"+user.getUserId());
		 Training training =getTraining(trainingName, session);
		 System.out.println("Training Name>>>>>>>:"+training.getTrainingName());
		 usrTrainReq.setUser(user);
		 usrTrainReq.setTraining(training);
		 usrTrainReq.setApprover(user.getApprover());
	     session.saveOrUpdate(usrTrainReq);
	     session.getTransaction().commit();
		}catch(HibernateException ex){
			System.out.println("In Exception block>>>>>>>>>>");
			 session.getTransaction().rollback();
			ex.printStackTrace();
	
		}finally{
			System.out.println("In finally block>>>>>>>>>>");
			  session.close();
		}
	   //  session.getTransaction().commit();
	   //  session.close();
	     System.out.println("In submitTrainingRequest TrainingDaoImpl Exit>>>>>>>");
	     //HibernateSessionManager.shutdown();
	     return usrTrainReq;
	}
	
	public List<Training> getTrainingCourses(){
		System.out.println("In getTrainingCourses() Entry ");
		List<Training> list =null;
		 SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
		 session.beginTransaction();
		 String hql = "FROM com.cts.common.domain.Training";
		 Query query = session.createQuery(hql);
		 list = query.list();   
		 session.getTransaction().commit();
	     session.close();
	     System.out.println("Is session closed>>>>>>>>:"+session.isOpen());
	    //HibernateSessionManager.shutdown();
		 return list;
	}
	

}
