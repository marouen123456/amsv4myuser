package com.sip.amsV3.entities;

import javax.persistence.*;
import javax.persistence.GenerationType;




@Entity
@Table(name="role")
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="role_id")
	private int id;
	
	@Column(name="role")
	private String role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Role(String role) {
		super();
		this.role = role;
	}
	
	public Role() {
		super();
	}

	@Override
	public String toString() {
		return  role;
	}
	
}
