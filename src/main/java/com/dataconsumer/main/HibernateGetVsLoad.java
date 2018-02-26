package com.dataconsumer.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.dataconsumer.model.Employee;
import com.dataconsumer.util.HibernateUtil;

public class HibernateGetVsLoad {

	public static void main(String[] args) {
		
		//Prep Work
		SessionFactory sessionFactory = HibernateUtil.getSessionAnnotationFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		//Get Example
		Employee emp = (Employee) session.get(Employee.class, new Long(2));
		System.out.println("Employee get called");
		System.out.println("Employee ID= "+emp.getId());
		System.out.println("Employee Get Details:: "+emp+"\n");
		
		//load Example
		Employee emp1 = (Employee) session.load(Employee.class, new Long(1));
		System.out.println("Employee load called");
		System.out.println("Employee ID= "+emp1.getId());
		System.out.println("Employee load Details:: "+emp1+"\n");
		
		//Get Example
		try{
		Employee empGet1 = (Employee) session.get(Employee.class, new Long(200));
		System.out.println("Employee get called");
		if(empGet1 != null){
		System.out.println("Employee GET ID= "+empGet1.getId());
		System.out.println("Employee Get Details:: "+empGet1+"\n");
		}
		}catch(Exception e){
			e.printStackTrace();
		}

		//load Example
		try{
		Employee empLoad1 = (Employee) session.load(Employee.class, new Long(100));
		System.out.println("Employee load called");
		System.out.println("Employee LOAD ID= "+empLoad1.getId());
		System.out.println("Employee load Details:: "+empLoad1+"\n");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Employee e1 = (Employee) session.get("com.journaldev.hibernate.model.Employee", new Long(2));

		Employee e2 = (Employee) session.load("com.journaldev.hibernate.model.Employee", new Long(1));

		Employee emp2 = new Employee();
		session.load(e1, new Long(1));
		
		//Close resources
		tx.commit();
		sessionFactory.close();
	}
}
