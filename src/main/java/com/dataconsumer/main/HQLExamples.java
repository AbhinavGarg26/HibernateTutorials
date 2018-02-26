package com.dataconsumer.main;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
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
		
		
		try {
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
		SQLQuery querySql = session.createSQLQuery("select id, name, salary from Employee");
		List<Object[]> rows = querySql.list();
		for(Object[] row : rows){
			Employee empSql = new Employee();
			empSql.setId(Long.parseLong(row[0].toString()));
			empSql.setName(row[1].toString());
			empSql.setSalary(Double.parseDouble(row[2].toString()));
			System.out.println(empSql);
		}
		
		//Get All Employees - addScalar example
		querySql = session.createSQLQuery("select id, name, salary from Employee")
				.addScalar("id", new LongType())
				.addScalar("name", new StringType())
				.addScalar("salary", new DoubleType());
		rows = querySql.list();
		for(Object[] row : rows){
			Employee empScaler = new Employee();
			empScaler.setId(Long.parseLong(row[0].toString()));
			empScaler.setName(row[1].toString());
			empScaler.setSalary(Double.parseDouble(row[2].toString()));
			System.out.println(empScaler);
		}
		
		//Multiple Tables
		query = session.createSQLQuery("select e.id, name, salary,address_line1, city,zipcode from Employee e, Address a where a.emp_id=e.id");
			rows = query.list();
			for(Object[] row : rows){
				Employee empMultipleTable = new Employee();
				empMultipleTable.setId(Long.parseLong(row[0].toString()));
				empMultipleTable.setName(row[1].toString());
				empMultipleTable.setSalary(Double.parseDouble(row[2].toString()));
				Address address = new Address();
				address.setAddressLine1(row[3].toString());
				address.setCity(row[4].toString());
				address.setZipcode(row[5].toString());
				empMultipleTable.setAddress(address);
				System.out.println(empMultipleTable);
		}
		
		//Join example with addEntity and addJoin
			query = session.createSQLQuery("select {e.*}, {a.*} from Employee e join Address a ON e.id=a.emp_id")
					.addEntity("e",Employee.class)
					.addJoin("a","e.address");
			rows = query.list();
			for (Object[] row : rows) {
			    for(Object obj : row) {
			    	System.out.print(obj + "::");
			    }
			    System.out.println("\n");
			}
			//Above join returns both Employee and Address Objects in the array
			for (Object[] row : rows) {
				Employee e = (Employee) row[0];
				System.out.println("Employee Info::"+e);
				Address a = (Address) row[1];
				System.out.println("Address Info::"+a);
			}
			
		//With Parameters
			query = session
					.createSQLQuery("select id, name, salary from Employee where id = ?");
			List<Object[]> empData = query.setLong(0, 1L).list();
			for (Object[] row : empData) {
				Employee empParam = new Employee();
				empParam.setId(Long.parseLong(row[0].toString()));
				empParam.setName(row[1].toString());
				empParam.setSalary(Double.parseDouble(row[2].toString()));
				System.out.println(empParam);
			}

			query = session
					.createSQLQuery("select id, name, salary from Employee where id = :id");
			empData = query.setLong("id", 2L).list();
			for (Object[] row : empData) {
				Employee empParam2 = new Employee();
				empParam2.setId(Long.parseLong(row[0].toString()));
				empParam2.setName(row[1].toString());
				empParam2.setSalary(Double.parseDouble(row[2].toString()));
				System.out.println(empParam2);
			}
			
		//rolling back to save the test data
		tx.rollback();
		
		//closing hibernate resources
		sessionFactory.close();
		
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception occured: " + e.getMessage());
		}
		
	}

}
