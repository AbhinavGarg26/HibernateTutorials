package com.dataconsumer.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="CART")
public class Cart {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cart_id")
	private long id;
	
	@Column(name="total")
	private double total;
	
	@Column(name="name")
	private String name;
	
	@OneToMany(mappedBy="cart")
	private Set<Items> items;
	
	@ManyToMany(targetEntity = Items.class, cascade = {CascadeType.ALL})
	@JoinTable(name = "CART_ITEMS", joinColumns = { @JoinColumn(name= "cart_id") },
				inverseJoinColumns = { @JoinColumn(name="item_id") })
	private Set<Items> itemsData;

	public Set<Items> getItemsData() {
		return itemsData;
	}

	public void setItemsData(Set<Items> itemsData) {
		this.itemsData = itemsData;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Items> getItems() {
		return items;
	}

	public void setItems(Set<Items> items) {
		this.items = items;
	}
	
	
}
