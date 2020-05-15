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
import models.Section;
import models.VMOSA;

class ServerBusinessPlanTest {

	
	@Test
	//test all function related to BusinessPlan
	void testBP() {
		
		//initialize storedBP
		BusinessPlan BP = new VMOSA();
		BP.year = 2020;
		BP.department ="CS";
		BP.isEditable=false;
		BP.name = "ha";
		
		BusinessPlan BP2 = new VMOSA();
		BP2.year = 2009;
		BP2.department ="CS";
		BP2.isEditable=true;
		BP2.name = "la";
		BP2.addSection(BP.root);
		BP2.root.content=("this is the vision");
		BP2.root.children.get(0).content=("this is the misson");
		BP2.addSection(BP.root.children.get(0));
		
		ArrayList <BusinessPlan> storedBP=new ArrayList<BusinessPlan>();
		storedBP.add(BP);
		storedBP.add(BP2);
		
		//initialize storedUser
		Person wynnie=new NormalUser("wynnie","wynnie","CS", true);
		
		ArrayList <Person> storedUser=new ArrayList<Person>();
		storedUser.add(wynnie);
		
		try {
				//get the server ready
			Registry registry = LocateRegistry.createRegistry(1899);
			MyRemoteImpl server = new MyRemoteImpl();
			
			server.setStoredBP(storedBP);
			server.setStoredUser(storedUser);
			
        	MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(server, 0);
			registry.rebind("MyRemote", stub);
			MyRemote serverInterface=(MyRemote) registry.lookup("MyRemote");
			MyRemoteClient client=new MyRemoteClient(serverInterface);
			
				//TEST FOR BP
			client.askForBP(2020); //see message in console, didn't login 
			client.askForLogin("wynnie","wynnie");
			
			
			client.askForBP(2021); // BP not exist
			assertEquals(null,client.getCurrentBP());
			client.askForBP(2020); // BP exists
			assertEquals(client.getCurrentBP().isEditable,server.findBP(2020).isEditable);
			assertEquals(client.getLoginPerson().department, client.getCurrentBP().department);
			System.out.println(client.getCurrentBP());
			
			//ask for new BP BYBPlan created or not
			client.newBP("BYBPlan"); 
			client.getCurrentBP().name = "pu";
			
			assertEquals(client.getLoginPerson().department,client.getCurrentBP().department);
			assertEquals(0,client.getCurrentBP().year);
			assertEquals("BYB Mission Statement", client.getCurrentBP().root.getName());
			Section testCur = client.getCurrentBP().root;//"BYB Mission Statement"
			assertEquals("BYB Objectives", testCur.children.get(0).getName());
			testCur = testCur.children.get(0);
			assertEquals("BYB Plan", testCur.children.get(0).getName());
			
			//test uploadBP(), see message in the console 
			client.uploadBP();//should fail, because year is invalid
			//when the BP already existed, but isEditable is false
			client.getCurrentBP().year=2020;
			client.getCurrentBP().department ="CS";
			client.uploadBP();
			//when the BP is new, add
			client.getCurrentBP().year=2021;
			client.uploadBP();
			//when the BP already existed and isEditable is true, replace with old version
			client.getCurrentBP().year=2009;
			client.uploadBP();
			
			BusinessPlan found=null;
			for (int i=0; i<storedBP.size();i++){
	    		if((storedBP.get(i).year==2009)){
	    			found=server.getStoredBP().get(i);
	    		}
			}
			assertEquals("CS",found.department);
			assertEquals(true,found.isEditable);
			
	
		}catch (Exception e) {
			e.printStackTrace();	//print fail
			fail("Fail");
		}
		
		}
	
}
