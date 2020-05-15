package models;

import java.io.Serializable;

public class NormalUser extends Person implements Serializable 
{

	private static final long serialVersionUID = 3349885006404797374L;
	
	
	public NormalUser() {
		super();
	}
	
	public NormalUser(String username, String password, String department, Boolean isAdmin) {
		super(username, password, department, isAdmin);
		this.username = username;
		this.password = password;
		this.department = department;
		this.isAdmin = isAdmin;
	}
	
	public void updateMsg(String message) {
		notifications.add(message);

	} 

}
