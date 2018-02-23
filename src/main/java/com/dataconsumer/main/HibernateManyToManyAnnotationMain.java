package com.dataconsumer.main;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.dataconsumer.model.Cart;
import com.dataconsumer.model.Items;
import com.dataconsumer.util.HibernateUtil;

public class HibernateManyToManyAnnotationMain {

	public static void main(String[] args) {
		Cart cart = new Cart();
		cart.setName("MyCart1");
		
		Items item1 = new Items("I10", 10, 1, cart);
		Items item2 = new Items("I20", 20, 2, cart);
		Set<Items> itemsSet = new HashSet<Items>();
		itemsSet.add(item1); itemsSet.add(item2);
		
		cart.setItems(itemsSet);
		cart.setTotal(10*1 + 20*2);
		
		cart.setItemsData(itemsSet);
		
		SessionFactory sessionFactory = null;
		try{
		sessionFactory = HibernateUtil.getSessionAnnotationFactory();
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		session.save(cart);
		System.out.println("Before committing transaction");
		tx.commit();
		sessionFactory.close();
		
		System.out.println("Cart ID="+cart.getId());
		System.out.println("Items ID="+item1.getId());
		System.out.println("Items2 ID="+item2.getId());
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(sessionFactory != null && !sessionFactory.isClosed()) sessionFactory.close();
		}
	}

}
