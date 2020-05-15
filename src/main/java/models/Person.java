package models;

import java.io.Serializable;
import java.util.ArrayList;

import org.controlsfx.control.Notifications;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;

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
		Boolean find = false;
		for(BusinessPlan bp: followedBP) {
			if(bp.toString().equals(BP.toString())) {
				find = true;
			}
		}
		
		if(!find) {
			followedBP.add(BP);
			BP.addObserver(this);
		}
	}
	
	public void unfollowBP(BusinessPlan BP) {
		
		followedBP.remove(BP);
		for(int i = 0; i<BP.observers.size();i++) {
			if(BP.observers.get(i).username.equals(username)) {
				BP.observers.get(i).followedBP = this.followedBP;
			}
		}
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
	
	public void showMsg() {
		
		Notifications  notification = Notifications.create()
				.title(" Message")
				.text(" The BP you followed has been changed by others")
				.hideAfter(Duration.seconds(2))
				.position(Pos.TOP_LEFT);
		
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				notification.show();
				
			}
		
			});
	}
	
	public abstract void updateMsg(String message);
	
}
