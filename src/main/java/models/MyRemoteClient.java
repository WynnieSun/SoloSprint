package models;

import java.rmi.RemoteException;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MyRemoteClient extends UnicastRemoteObject implements ClientInterface{

	private BusinessPlan currentBP=null;
	private Person loginPerson=null;
	private static MyRemote server;
	
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("static-access")
	public MyRemoteClient(MyRemote server) throws RemoteException {
		this.server=server;
	}

	@Override
	public String toString() {
		return loginPerson+"("+currentBP+")";
	}
	
	@Override
	public void notifyChange(Object observable, Object updateMsg) throws RemoteException {
		String returnMessage = "got message: " + updateMsg;
	    System.out.println(returnMessage);
		
	}

	public BusinessPlan getCurrentBP() {
		return currentBP;
	}

	public void setCurrentBP(BusinessPlan currentBP) {
		this.currentBP = currentBP;
	}

	public Person getLoginPerson() {
		return loginPerson;
	}

	public static void main(String[] args) {
		try {
			MyRemote remoteService = (MyRemote) Naming
					.lookup("//localhost:9999/MyRemote");
			MyRemoteClient client = new MyRemoteClient(server);
			remoteService.addObserver(client);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public void Hello() {
    	try {
    		String response = server.sayHello();
            System.out.println("Response: " + response );
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    } 
    
    public void askForLogin(String username, String password) {
		try {
			loginPerson=server.verifyLoginPerson(username,password);
		} catch (RemoteException e) {
			 System.err.println("Client exception: " + e.toString());
	         e.printStackTrace();
		}
		if(loginPerson!=null) {
			System.out.println("User: " + loginPerson.username+" logined.");
		}
		else {
			System.out.println("Wrong username password combination. ");
		}
	}

	public void logOut() {
    	loginPerson=null;
    	try {
			server.logOut();
			System.out.println("user logout from Client side.");
    	}catch (RemoteException e) {
			e.printStackTrace();
		}
    }
    
    public void changeEditable(int year,boolean bol) {
    	if(loginPerson.isAdmin==true) {
    		try {
        		server.changeEditable(year,bol);
        	}catch (RemoteException e) {
    			e.printStackTrace();
    		}
    	}
    	else {
    		System.out.println("Sorry, You're not a admin. You CAN'T change a BusinessPlan's isEditable.");
    	}
    }
    
    public void addPerson(String username, String password, String department, Boolean isAdmin) {
    	if(loginPerson.isAdmin==true) {
    		try {
    			server.addPerson(username,password,department,isAdmin);
        	}catch (RemoteException e) {
    			e.printStackTrace();
    		}
    	}
    	else {
    		System.out.println("Sorry, You're not a admin. You CAN'T add a person");
    	}
    }
    
    //only client's department
    public ArrayList<BusinessPlan> askForAllBP() {
    	try {
			ArrayList<BusinessPlan> copy=server.findDepAllBP();
			return copy;
		} catch (RemoteException e) {
			System.err.println("Client exception: " + e.toString());
	        e.printStackTrace();
		}
    	return null;
    }
    public void askForBP(int year){
    	try {
    		currentBP=server.findBP(year);
    	}catch (RemoteException e) {
			 System.err.println("Client exception: " + e.toString());
	         e.printStackTrace();
		}
    }
    
	public void newBP(String Type) {
		if(Type=="VMOSA") {
			BusinessPlan BP = new VMOSA();
			currentBP=BP;
			currentBP.department=loginPerson.department;
		}
		else if(Type=="BYBPlan"){
			BusinessPlan BP = new BYBPlan();
			currentBP=BP;
			currentBP.department=loginPerson.department;
		}
		else if(Type=="CNTRAssessment") {
			BusinessPlan BP = new CNTRAssessment();
			currentBP=BP;
			currentBP.department=loginPerson.department;
		}
		else {
			System.out.println("The Type of the BusinessPlan is not available to create.");
		}
	}
	
	//upload BP after create a new BP or revised the old one
    public String uploadBP() {
    	if(currentBP.year<1819||currentBP.year>3000) {
    		return "Please use appropriate year attribute.";
    	}
    	else {
    		try {
    			String Message=server.addBP(currentBP);	
    			return Message;
    		}catch (RemoteException e) {
    			e.printStackTrace();
    		}
    	}
    	return null;
    }
    
	//add new BP 
    public String addBP() {
    	if(currentBP.year<1819||currentBP.year>3000) {
    		return "Please use appropriate year attribute.";
    	}
    	else {
    		try {
    			String Message=server.addNewBP(currentBP);	
    			return Message;
    		}catch (RemoteException e) {
    			e.printStackTrace();
    		}
    	}
    	return null;
    }
    
    //compare two BPs
    public ArrayList<String> showDiff(BusinessPlan BP2) {
		try {
			ArrayList<String> diff = server.compare(currentBP, BP2);
			return diff;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return null;
    }

}
    
