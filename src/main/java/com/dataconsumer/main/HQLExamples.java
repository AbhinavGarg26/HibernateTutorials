package com.dataconsumer.main;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import com.dataconsumer.model.Address;
import com.dataconsumer.model.Employee;
import com.dataconsumer.util.HibernateUtil;

public class HQLExamples {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		//Prep work
		SessionFactory sessionFactory = HibernateUtil.getSessionAnnotationFactory();
		Session session = sessionFactory.getCurrentSession();
		
		//HQL example - Get All Employees
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Address");
		List<Address> addList = query.list();
		for(Address add : addList){
			System.out.println("List of Address::" + add.getCity());
		}
		
		query = session.createQuery("from Employee");
		List<Employee> empList = query.list();
		for(Employee emp : empList){
			System.out.println("List of Employees::"+emp.getId()+","+emp.getAddress().getCity());
		}
		
		//HQL example - Get Employee with id
		query = session.createQuery("from Employee where id= :id");
		query.setLong("id", 3);
		Employee emp = (Employee) query.uniqueResult();
		System.out.println("Employee Name="+emp.getName()+", City="+emp.getAddress().getCity());
		
		//HQL pagination example
		query = session.createQuery("from Employee");
		query.setFirstResult(0); //starts with 0
		query.setFetchSize(2);
		empList = query.list();
		for(Employee emp4 : empList){
			System.out.println("Paginated Employees::"+emp4.getId()+","+emp4.getAddress().getCity());
		}
		
		//HQL Update Employee
		query = session.createQuery("update Employee set name= :name where id= :id");
		query.setParameter("name", "Pankaj Kumar");
		query.setLong("id", 1);
		int result = query.executeUpdate();
		System.out.println("Employee Update Status="+result);

		//HQL Delete Employee, we need to take care of foreign key constraints too
		query = session.createQuery("delete from Address where id= :id");
		query.setLong("id", 3);
		result = query.executeUpdate();
		System.out.println("Address Delete Status="+result);
		
		query = session.createQuery("delete from Employee where id= :id");
		query.setLong("id", 3);
		result = query.executeUpdate();
		System.out.println("Employee Delete Status="+result);
		
		//HQL Aggregate function examples
		query = session.createQuery("select sum(salary) from Employee");
		double sumSalary = (Double) query.uniqueResult();
		System.out.println("Sum of all Zipcode= "+sumSalary);
		
		//HQL join examples
		query = session.createQuery("select e.name, a.city from Employee e "
				+ "INNER JOIN e.address a");
		List<Object[]> list = query.list();
		for(Object[] arr : list){
			System.out.println(Arrays.toString(arr));
		}
		
		//HQL group by and like example
		query = session.createQuery("select e.name, sum(e.salary), count(e)"
				+ " from Employee e where e.name like '%i%' group by e.name");
		List<Object[]> groupList = query.list();
		for(Object[] arr : groupList){
			System.out.println(Arrays.toString(arr));
		}
		
		//HQL order by example
		query = session.createQuery("from Employee e order by e.id desc");
		empList = query.list();
		for(Employee emp3 : empList){
			System.out.println("ID Desc Order Employee::"+emp3.getId()+","+emp3.getAddress().getCity());
		}
		
		
		// Use of SQL 
		query = session.createSQLQuery("select emp_id, emp_name, emp_salary from Employee");
		List<Object[]> rows = query.list();
		for(Object[] row : rows){
			Employee empSql = new Employee();
			empSql.setId(Long.parseLong(row[0].toString()));
			empSql.setName(row[1].toString());
			empSql.setSalary(Double.parseDouble(row[2].toString()));
			System.out.println(empSql);
		}
		
		//Get All Employees - addScalar example
		query = session.createSQLQuery("select emp_id, emp_name, emp_salary from Employee")
				.addScalar("emp_id", new LongType())
				.addScalar("emp_name", new StringType())
				.addScalar("emp_salary", new DoubleType());
		rows = query.list();
		for(Object[] row : rows){
			Employee empScaler = new Employee();
			empScaler.setId(Long.parseLong(row[0].toString()));
			empScaler.setName(row[1].toString());
			empScaler.setSalary(Double.parseDouble(row[2].toString()));
			System.out.println(empScaler);
		}
		
		
		//rolling back to save the test data
		tx.rollback();
		
		//closing hibernate resources
		sessionFactory.close();
	}

}
