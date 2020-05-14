package models;

import java.io.Serializable;

public abstract class Person implements Serializable {
	

	private static final long serialVersionUID = -1952576292459976550L;
	public String username;
	public String password;
	public String department;
	public Boolean isAdmin;

	public Person() {
		
	}
	
	public Person(String username, String password, String department, Boolean isAdmin) {
		this.username = username;
		this.password = password;
		this.department = department;
		this.isAdmin = isAdmin;
	}
	
	@Override
	public String toString() {
		return "Person [username=" + username + ", password=" + password + ", department=" + department + ", isAdmin="
				+ isAdmin + "]";
	}
	
	public abstract void update();
	
}
