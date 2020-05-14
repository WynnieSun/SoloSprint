import static org.junit.jupiter.api.Assertions.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import models.BusinessPlan;
import models.MyRemote;
import models.MyRemoteClient;
import models.MyRemoteImpl;
import models.NormalUser;
import models.Person;
import models.VMOSA;

class UserTest {

	@Test
	//This test only test the Function that relate to User.
	void testPerson() {
		
		//initialize storedBP
		BusinessPlan BP = new VMOSA();
		BP.year = 2020;
		BP.department = "CS";
		BP.isEditable=false;
		
		ArrayList <BusinessPlan> storedBP=new ArrayList<BusinessPlan>();
		storedBP.add(BP);
		
		//initialize storedUser
		Person wynnie=new NormalUser("wynnie","wynnie","CS", true);
		
		ArrayList <Person> storedUser=new ArrayList<Person>();
		storedUser.add(wynnie);
		
		try {
				//get the server ready
			Registry registry = LocateRegistry.createRegistry(1299);
			MyRemoteImpl server = new MyRemoteImpl();
			
			server.setStoredBP(storedBP);
			server.setStoredUser(storedUser);
        	MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(server, 0);
			registry.rebind("MyRemote", stub);
			MyRemote serverInterface=(MyRemote) registry.lookup("MyRemote");
			MyRemoteClient client=new MyRemoteClient(serverInterface);
			
				//TEST FOR PERSON
			
			//Admin user
			//login
			client.askForLogin("wynnie","wynnie");

			//check loginPerson
			assertEquals(wynnie.toString(),client.getLoginPerson().toString());
			//add new Person (not Admin)
			client.addPerson("terry", "password", "CS", false);
			//check terry stored in the server side or not
			Person terry=new NormalUser("terry", "password", "CS", false);
			assertEquals(terry.toString(),server.findPerson("terry", "password", "MATH", false).toString());
			
			//check changeEditable
			//see message in the console
			client.changeEditable(2021,true); // file not exist
			client.changeEditable(2020,true); // file exist
			
			//notAdmin User
			//see message in the console
			client.logOut();
			client.askForLogin("terry","password");
			client.changeEditable(2020,true);
			
			client.addPerson("littleguangtou", "password", "CS", false);
			

		}catch (Exception e) {
			e.printStackTrace();	//print fail
			fail("Fail");
		}
		
		}
	

}
