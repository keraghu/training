package com.cts.training.dao;

import java.util.Date;
import java.util.List;

import javassist.bytecode.Descriptor.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.cts.common.domain.Certification;
import com.cts.common.domain.Training;
import com.cts.common.domain.User;
import com.cts.common.domain.UserTrainingApprovalQueue;
import com.cts.common.domain.UserTrainingRequest;



public class HibernateSessionManager {
	 private static final SessionFactory sessionFactory = buildSessionFactory();

	 private static SessionFactory buildSessionFactory() {
	 try {
	 return new AnnotationConfiguration().
	 configure().buildSessionFactory();
	 } catch (Throwable ex) {
	 System.err.println("SessionFactory creation failed." + ex);
	 throw new ExceptionInInitializerError(ex);
	 }
	 }

	 public static SessionFactory getSessionFactory() {
	 return sessionFactory;
	 }

	 public static void shutdown() {
	 // Close caches and connection pools
	 getSessionFactory().close();
	 }
	 
	 
	 @SuppressWarnings("unchecked")
	 public static void main(String[] args) {
		 //insertcertandtraining();
		// insertusercert();
		// selectusercert();
		// insertcert();
		 //inserttraining();
		 getPendingCount("rajendra");
//		List l= getPendingRequestList("rajendra");
//		UserTrainingApprovalQueue utau=(UserTrainingApprovalQueue)l.get(0);
//		System.out.println(utau.getSubmittedUser().getUserName()+" "+utau.getTraining().getTrainingDesc()+" "+ utau.getUserTrainingRequest().getReqId());
	 }
	 public static void inserttrainingqueue(){
		 SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
		 session.beginTransaction();
	 }
	 
	 public static  List getPendingRequestList(String approverName){
		 SessionFactory sf = HibernateSessionManager.getSessionFactory();
		 Session session = sf.openSession();
		 session.beginTransaction();
		 String hql = "select utau FROM com.cts.common.domain.UserTrainingApprovalQueue as utau"
		     		+ "   inner join utau.approver as a  join fetch utau.submittedUser su  join fetch utau.training t where  a.userName=:userName ";
		 Query query = session.createQuery(hql);
			query.setParameter("userName", approverName);
			List list=	query.list();
			session.getTransaction().commit();
			session.close();
			
			return list;
	}
	 
		public static long getPendingCount(String approverName){
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
				System.out.println(cnt);
				session.getTransaction().commit();
				  session.close(); 
			 
			 return cnt;
		}
	 
	 public static void selectusercert(){
		 SessionFactory sf = HibernateSessionManager.getSessionFactory();
	     Session session = sf.openSession();
	     session.beginTransaction();
	     String hql = "select utr FROM com.cts.training.domain.UserTrainingRequest as utr"
		     		+ "   inner join utr.user as u where utr.trainingStatus=:status and u.userName=:name";
	     Query query = session.createQuery(hql);
	     query.setParameter("status", "Completed");
	     query.setParameter("name", "kanana");
	    // System.out.println(query.list().get(0));
//	     if(query.list().get(0)instanceof User){
//	    	 System.out.println("user object");
//	     }
	     UserTrainingRequest utr= (UserTrainingRequest)(query.list().get(0));
	     System.out.println(utr.getReqId()+" "+utr.getUser().getUserName());
	     
	 }
	 
	 public static void insertusercert(){
		 SessionFactory sf = HibernateSessionManager.getSessionFactory();
	     Session session = sf.openSession();
	     session.beginTransaction();
	     String hql = "FROM com.cts.common.domain.Training t where t.trainingName=:name";
	     Query query = session.createQuery(hql);
	     query.setParameter("name", "AWS Essentials");
	     Training t=(Training)(query.list().get(0));
	     
	     String uhql = "FROM com.cts.common.domain.User u where u.userName=:username";
	     Query uquery = session.createQuery(uhql);
	     uquery.setParameter("username", "divya");
	     
	     User u=(User)(uquery.list().get(0));
	     UserTrainingRequest utreq=new UserTrainingRequest();
	     utreq.setApprovedDate(new Date());
	     utreq.setReqStatus("Pending");
	     utreq.setTrainingStatus("Not Completed");
	     utreq.setTraining(t);
	     utreq.setUser(u);
	     session.save(utreq);
	     session.getTransaction().commit();
	     session.close();
	     System.out.println("insertion is over");
	 }
	 
	 public static void insertcert(){
		 SessionFactory sf = HibernateSessionManager.getSessionFactory();
	     Session session = sf.openSession();
	     session.beginTransaction();
	     
	     Certification c=new Certification();
	     c.setCertificationName("SAOPCP");
	     c.setCertificationDesc("Spring AOP Certified Professional");
	     c.setCost(0);
	     c.setMandatory(false);
	     c.setReimbursed(false);
	     
	     Certification c1=new Certification();
	     c1.setCertificationName("SDAOWCP");
	     c1.setCertificationDesc("Spring DAO Certified Professional");
	     c1.setCost(0);
	     c1.setMandatory(false);
	     c1.setReimbursed(false);
	     session.save(c1);
	     session.save(c);
	     session.getTransaction().commit();
	     session.close();
	 }
	 
	 public static void inserttraining(){
		 SessionFactory sf = HibernateSessionManager.getSessionFactory();
	     Session session = sf.openSession();
	     session.beginTransaction();
	     String hql = "FROM com.cts.common.domain.Certification c where c.certificationName=:name";
	     Query query = session.createQuery(hql);
	     query.setParameter("name", "SAOPCP");
	     Certification c=(Certification)(query.list().get(0));
	     
	     String hql2 = "FROM com.cts.common.domain.Certification c where c.certificationName=:name";
	     Query query2 = session.createQuery(hql2);
	     query2.setParameter("name", "SDAOWCP");
	     Certification c1=(Certification)(query2.list().get(0));
	     System.out.println("after saving of certification");
	     Training training=new Training();
	     training.setDuration("24hrs");
	     training.setEndDate(new Date());
	     training.setStartDate(new Date());
	     training.setMode("online");
	     training.setPreRequisite("Spring knowledge");
	     training.setTrainingName("Spring AOP");
	     training.setTrainingDesc("Basics of  Spring AOP");
	     training.setCertification(c);
	     
	     Training training1=new Training();
	     training1.setDuration("80hrs");
	     training1.setEndDate(new Date());
	     training1.setStartDate(new Date());
	     training1.setMode("classroom");
	     training1.setPreRequisite("cloud knowledge");
	     training1.setTrainingName("Spring DAO");
	     training1.setTrainingDesc("Basics of Spring DAO");
	     training1.setCertification(c1);
	     session.save(training);
	     session.save(training1);
	     System.out.println("after saving of training");
	     session.getTransaction().commit();
	     session.close();

	 }
}
