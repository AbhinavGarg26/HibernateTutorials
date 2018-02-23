package com.dataconsumer.main;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.dataconsumer.model.Customer;
import com.dataconsumer.model.Txn;
import com.dataconsumer.util.HibernateUtil;

public class HibernateOneToOneAnnotationMain {

	public static void main(String[] args) {
		
		Txn txn = buildDemoTransaction();
		
		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction tx = null;
		try{
		//Get Session
		sessionFactory = HibernateUtil.getSessionAnnotationFactory();
		session = sessionFactory.getCurrentSession();
		System.out.println("Session created using annotations configuration");
		//start transaction
		tx = session.beginTransaction();
		//Save the Model object
		session.save(txn);
		//Commit transaction
		tx.commit();
		System.out.println("Annotation Example. Transaction ID="+txn.getId());
		
		//Get Saved Trasaction Data
		printTransactionData(txn.getId(), sessionFactory);
		}catch(Exception e){
			System.out.println("Exception occured. "+e.getMessage());
			e.printStackTrace();
		}finally{
			if(sessionFactory != null && !sessionFactory.isClosed()){
				System.out.println("Closing SessionFactory");
				sessionFactory.close();
			}
		}
	}

	private static void printTransactionData(long id, SessionFactory sessionFactory) {
		Session session = null;
		Transaction tx = null;
		try{
			//Get Session
			sessionFactory = HibernateUtil.getSessionAnnotationFactory();
			session = sessionFactory.getCurrentSession();
			//start transaction
			tx = session.beginTransaction();
			//Save the Model object
			Txn txn = (Txn) session.get(Txn.class, id);
			//Commit transaction
			tx.commit();
			System.out.println("Annotation Example. Transaction Details=\n"+txn);
			
			}catch(Exception e){
				System.out.println("Exception occured. "+e.getMessage());
				e.printStackTrace();
			}
	}

	private static Txn buildDemoTransaction() {
		Txn txn = new Txn();
		txn.setDate(new Date());
		txn.setTotal(100);
		
		Customer cust = new Customer();
		cust.setAddress("San Jose, USA");
		cust.setEmail("pankaj@yahoo.com");
		cust.setName("Pankaj Kr");
		
		txn.setCustomer(cust);
		
		cust.setTxn(txn);
		return txn;
	}

}
