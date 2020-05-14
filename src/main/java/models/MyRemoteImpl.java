package models;

import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

// Concrete server class
public class MyRemoteImpl implements MyRemote {

	private Person loginPerson=null;
	private ArrayList <BusinessPlan> storedBP = new ArrayList<BusinessPlan>();
	private ArrayList <Person> storedUser = new ArrayList<Person>();
	private ArrayList <MyRemoteClient> clientList = new ArrayList<MyRemoteClient>();
	
	//helper attribute
	private ArrayList<String> diffsec = new ArrayList<String>();
  
    public MyRemoteImpl() {
    	
    }
    
    //register client here
    public void register(MyRemoteClient client) {
    	if (!(clientList.contains(client))) 
    	{
    		clientList.add(client);
    		System.out.println("Registered new client ");
    	}
    }

    public void unregister(MyRemoteClient client) {
    	if (clientList.remove(client)) {
    		System.out.println("Unregistered client ");
    	} else {
    		System.out.println("unregister: clientwasn't registered.");
    	}
    }

    //notify clients
    public void callback() {
    	System.out.println(clientList);
    	for (int i = 0; i < clientList.size(); i++){
    	      MyRemoteClient nextClient = clientList.get(i);
    	      // invoke the callback method
    	      System.out.println("Notify: "+ nextClient);
    	        nextClient.notifyChange("Number of registered clients="
    	           +  clientList.size());
    	    }
    	
    }
    
    //basic server methods 
	public Person getLoginPerson() {
		return loginPerson;
	}

	public void setLoginPerson(Person loginPerson) {
		this.loginPerson = loginPerson;
	}

	public ArrayList<Person> getStoredUser() {
		return storedUser;
	}

	public void setStoredUser(ArrayList<Person> storedUser) {
		this.storedUser = storedUser;
	}
	
	public ArrayList<BusinessPlan> getStoredBP() {
		return storedBP;
	}
	
	public void setStoredBP(ArrayList<BusinessPlan> storedBP) {
		this.storedBP = storedBP;
	}

	public String sayHello() {
        return "Hello User!";
    }
    
	public Person verifyLoginPerson(String username, String password) {
    	for(int i=0; i<storedUser.size();i++){
    		if ((storedUser.get(i).username.equals(username))&&(storedUser.get(i).password.equals(password))){
    			loginPerson=storedUser.get(i);
    			System.out.println("user found.");
    			
    			return loginPerson;
    		}
    	}
    	System.out.println("user not found.");
    	return null;
    }
    
    public ArrayList<MyRemoteClient> getClientList() {
		return clientList;
	}

	public void setClientList(ArrayList<MyRemoteClient> clientList) {
		this.clientList = clientList;
	}

	//helper class for checking the person exists or not in the test file
    public Person findPerson(String username, String password, String deparment, Boolean bol) {
    	for(int i=0; i<storedUser.size();i++){
    		if ((storedUser.get(i).username.equals(username))&&(storedUser.get(i).password.equals(password))){
    			Person personfound=storedUser.get(i);
    			System.out.println("user found.");
    			return personfound;
    		}
    	}
    	System.out.println("user not found.");
    	return null;
    }
    
    public void addPerson(String username, String password, String department, Boolean isAdmin) {
    	Person newperson=new NormalUser(username,password,department,isAdmin);
    	storedUser.add(newperson);
    	System.out.println("New User:"+username+" added.");
    }
    
    public void changeEditable(int year, boolean bool) {
    	BusinessPlan bpcur=null;
    	if(loginPerson==null) {
    		System.out.println("PLEASE LOGIN FIRST.");
    	}
    	else {
    		for (int i=0; i<storedBP.size();i++){
        		if((storedBP.get(i).department.equals(loginPerson.department))&&(storedBP.get(i).year==year)){
        			bpcur=storedBP.get(i);
        		}
    		}
    		if(bpcur!=null) {
    			bpcur.isEditable=bool;
    			System.out.println("BusinessPlan isEditable changed to: "+ bool);
    		}
    		else {
    			System.out.println("BusinessPlan not found.");
    		}
    	}
    	
    }
    
    public void logOut() {
    	loginPerson=null;
    	System.out.println("user logout from Server.");
    }
    
    //called by client askForAllBP function
    public ArrayList<BusinessPlan> findDepAllBP(){
    	ArrayList<BusinessPlan> DepAllBP= new ArrayList<BusinessPlan>();
    	if(loginPerson==null) {
    		System.out.println("PLEASE LOGIN FIRST.");
    		return null;
    	}
    	for (int i=0; i<storedBP.size();i++){
    		if((storedBP.get(i).department.equals(loginPerson.department))){
    			DepAllBP.add(storedBP.get(i));
    			}
    	}
    	System.out.println("All BP: " + DepAllBP);
    	return DepAllBP;
    }
    
    //called by client askForBP function
    public BusinessPlan findBP(int year) {
    	if(loginPerson==null) {
    		System.out.println("PLEASE LOGIN FIRST.");
    		return null;
    	}
    	for (int i=0; i<storedBP.size();i++){
    		if((storedBP.get(i).department.equals(loginPerson.department))&&(storedBP.get(i).year==year)){
    			System.out.println("BusinessPlan found.");
    			return storedBP.get(i);
    		}
    	}
    	System.out.println("BusinessPlan not found.");
    	return null;
    }
    
    //called by client uploadBP function
    //edit a old BP
    public String addBP(BusinessPlan BP) {
    	String Message="";
    	if(loginPerson==null) {
    		Message="PLEASE LOGIN FIRST.";
    		System.out.println(Message);
    		return Message;
    	}
    	System.out.println(storedBP);
    	for (int i=0; i<storedBP.size();i++){
    		BusinessPlan current=storedBP.get(i);

    		if((current.department.equals(BP.department))&&(current.year==BP.year)){
    			System.out.println("Business Plan already exists.");
    			if(current.isEditable==false) {
    				Message="This BusinessPlan is not Editable";
        			System.out.println(Message);
        			//callback();
        			return Message;
    			}
    			storedBP.remove(current);
    			storedBP.add(BP);
    			Message="Replaced Old Version BP with New One.";
    			System.out.println(Message);
    			//callback();
    			return Message;
    		}
    	}
    	storedBP.add(BP);
    	System.out.println("Business does not exist.");
    	Message="Added new BP to Server";
    	System.out.println(Message);
    	//callback();
    	return Message;
    }
    
    @Override
    //called by client addBP()
    //create or clone an newBP
	public String addNewBP(BusinessPlan BP) throws RemoteException {
    	String Message="";
    	if(loginPerson==null) {
    		Message="PLEASE LOGIN FIRST.";
    		System.out.println(Message);
    		return Message;
    	}
    	for (int i=0; i<storedBP.size();i++){
    		BusinessPlan current=storedBP.get(i);
    		if((current.department.equals(BP.department))&&(current.year==BP.year)){
    			Message=("Business Plan already exists.");
    			//callback();
    			System.out.println(Message);
    			return Message;
    		}
    	}
    	storedBP.add(BP);
    	System.out.println("Business does not exist.");
    	Message="Added new BP to Server";
    	//callback();
    	System.out.println(Message);
    	return Message;
	}
    
    //helper function for compare, tree traversal
    public void storeDiffSection(Section p, BusinessPlan BP) {
		for (Section child : p.children) {
			diffsec.add(BP.toString() + "--" + child.showContent());
			storeDiffSection(child, BP);
		}
	}
    
    //compare two BPs
    //called by client showDiff method
    public ArrayList<String> compare(BusinessPlan BP1, BusinessPlan BP2){
    	//store different sections
    	ArrayList<String> diff = new ArrayList<String> ();

    	//compare roots first    	
    	if((BP1.getRoot().showContent()).equals(BP2.getRoot().showContent())){
    		//do nothing if same
    	}
    	else {
    		diff.add(BP1.toString() + "--" + BP1.getRoot().showContent());
    		diff.add(BP2.toString() + "--" + BP2.getRoot().showContent());
    	}
    	
    	//compare children
    	diffsec.clear();
    	compareSections(BP1.getRoot(), BP2.getRoot(), BP1, BP2);
    	for (String child : diffsec) {
    		diff.add(child);
    	}
    	
		return diff;
    	
    }
    
    //compare children sections, tree traversal
    public void compareSections(Section sec1, Section sec2, BusinessPlan BP1, BusinessPlan BP2) {
    	int size;
    	int diffsize = sec1.getChildren().size() - sec2.getChildren().size();

    	//add redundant sections as differences
    	//same size: no redundant sections
    	if(diffsize == 0) {
    		size = sec1.getChildren().size();
    	}
    	//sec2 is bigger
    	else if(diffsize < 0) {
    		size = sec1.getChildren().size();
    		for(int i = diffsize; i<0; i++) {
    			int len = sec2.getChildren().size();
    			diffsec.add(BP2.toString() + "--" + sec2.getChildren().get(diffsize+len).showContent());
    			storeDiffSection(sec2.getChildren().get(diffsize+len),BP2);
    		}
    	}
    	//sec1 is bigger
    	else {
    		size = sec2.getChildren().size();
    		for(int i = diffsize; i>0; i--) {
    			int len = sec2.getChildren().size();
    			diffsec.add(BP1.toString() + "--" + sec1.getChildren().get(diffsize+len-1).showContent());
    			storeDiffSection(sec1.getChildren().get(diffsize+len-1),BP1);
    		}
    	}
    	
    	//compare sections within the smaller size
    	for (int i = 0; i<size; i++) {
    		if((sec1.getChildren().get(i).showContent()).equals(sec2.getChildren().get(i).showContent())) {
    			//do nothing if same
    		}
    		else {
    			diffsec.add(BP1.toString() + "--" + sec1.getChildren().get(i).showContent());
    			diffsec.add(BP2.toString() + "--" + sec2.getChildren().get(i).showContent());
    		}
    		compareSections(sec1.getChildren().get(i),sec2.getChildren().get(i), BP1, BP2);
		}
    	
    }
    
    //save all data to the disk every two minutes 
  	//timeInterval should be set to 1000*120 when call the function
  	public void startEncode(long timeInterval) {
          Runnable runnable = new Runnable() {
          	public void run() {
          		while (true) {
          			// ------- code for task to run	      
          			XMLEncodeAllData();
          			System.out.println("Server has automatically Encode all Data.");
          			// ------- ends here	      
          			try {
          				Thread.sleep(timeInterval);
          				} 
          			catch (InterruptedException e) {
          				e.printStackTrace();
          				}	      
          			}	    
          		}	  
          	};	  	  
          	Thread thread = new Thread(runnable);
          	thread.start();
  	}
  	
	//Encoder
	public void XMLEncodeAllData() {
		String BusinessPlan_File="Server_BusinessPlan.xml";
		String User_File="Server_User.xml";
		XMLEncodeBP(BusinessPlan_File);
		XMLEncodeUser(User_File);
	}
	
    //helperEncodeFunction
	public void XMLEncodeBP(String filename) {
		XMLEncoder encoder=null;
		try{
			encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));
			}
		catch(FileNotFoundException fileNotFound){
				System.out.println("ERROR: While Creating or Opening the File"+filename);
			}
			encoder.writeObject(this.storedBP);
			encoder.close();
	}
	
	public void XMLEncodeUser(String filename) {
		XMLEncoder encoder=null;
		try{
			encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));
			}
		catch(FileNotFoundException fileNotFound){
				System.out.println("ERROR: While Creating or Opening the File"+filename);
			}
			encoder.writeObject(this.storedUser);
			encoder.close();
	}
	
	//Decoder
	//Call two decoder functions to read all data from the disk
	//When the server starts, we can call this two function first
	//Since we set the filename of encoder's and decoder's files are the same, 
	//The server will always store and read from the same files.
	@SuppressWarnings("unchecked")
	public ArrayList <BusinessPlan> XMLDecodeBP() {
		String BusinessPlan_File="Server_BusinessPlan.xml";
		XMLDecoder decoder=null;
		try {
			decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(BusinessPlan_File)));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File "+BusinessPlan_File+" not found");
		}
		return (ArrayList<BusinessPlan>)decoder.readObject();
	}

	@SuppressWarnings("unchecked")
	public ArrayList <Person> XMLDecodeUser() {
		String User_File="Server_User.xml";
		XMLDecoder decoder=null;
		try {
			decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(User_File)));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File "+User_File+" not found");
		}
		return (ArrayList <Person>)decoder.readObject();
	}

	public static void main(String args[]) {
		try {
			MyRemoteImpl obj = new MyRemoteImpl();
			MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(obj, 0);
			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("MyRemote", stub);

			System.err.println("Server ready");

		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

}