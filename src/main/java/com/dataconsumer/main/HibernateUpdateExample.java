package com.dataconsumer.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.dataconsumer.model.Employee;
import com.dataconsumer.util.HibernateUtil;

public class HibernateUpdateExample {

	public static void main(String[] args) {

		// Prep Work
		SessionFactory sessionFactory = HibernateUtil.getSessionAnnotationFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Employee emp = (Employee) session.load(Employee.class, new Long(10));
		System.out.println("Employee object loaded. " + emp);
		tx.commit();	

		// update example
		emp.setName("Updated name");
		emp.getAddress().setCity("Bangalore");
		Transaction tx7 = session.beginTransaction();
		session.update(emp);
		emp.setName("Final updated name");
		System.out.println("13. Before committing update transaction");
		tx7.commit();
		System.out.println("14. After committing update transaction");

		// Close resources
		sessionFactory.close();

	}

}