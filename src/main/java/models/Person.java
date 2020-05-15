package models;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Person implements Serializable {
	

	private static final long serialVersionUID = -1952576292459976550L;
	public String username;
	public String password;
	public String department;
	public Boolean isAdmin;
	public ArrayList<BusinessPlan> followedBP = new ArrayList<BusinessPlan>();
	public ArrayList<String> notifications = new ArrayList<String>();

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
	
	public void followBP(BusinessPlan BP) {
		followedBP.add(BP);
		BP.addObserver(this);
	}
	
	public void unfollowBP(BusinessPlan BP) {
		followedBP.remove(BP);
		BP.deleteObserver(this);
		//remove BP's notifications  
		ArrayList<String> newNotis = new ArrayList<String>();
		
		for(String noti: notifications) {
			if(noti.contains(BP.toString())){
				
			}
			else {
				newNotis.add(noti);
			}
		}
		notifications = newNotis;
	}
	
	public abstract void updateMsg(String message);
	
}
