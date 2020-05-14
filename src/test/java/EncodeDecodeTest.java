import static org.junit.jupiter.api.Assertions.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import models.BusinessPlan;
import models.MyRemote;
import models.MyRemoteImpl;
import models.NormalUser;
import models.Person;
import models.VMOSA;

class EncodeDecodeTest {

	@Test
	//this test will check encode and decode function to make sure
	//1. Every 2 minutes the server saves all the data in memory to file(s) on the disk.
	//2. When the server starts, it reads the files on the disk.
	//saved correctly
	
	//if the decoder works
	//then we can tell that when the server starts, if we add this two line of code below,
	//{
	//server.setStoredBP(server.XMLDecodeBP());
	//server.setStoredUser(server.XMLDecodeUser());
	//}
	//the server will decode all the data from the disk.
	
	void testServerEncodeDecode() {
		
		BusinessPlan BP = new VMOSA();
		BP.year = 2020;
		BP.department = "CS";
		BP.isEditable=false;
		
		//initialize storedBusinessPlan
		ArrayList <BusinessPlan> storedBP=new ArrayList<BusinessPlan>();
		storedBP.add(BP);
		
		//initialize storedUser
		Person wynnie=new NormalUser("wynnie","wynnie","CS", true);
		
		ArrayList <Person> storedUser=new ArrayList<Person>();
		storedUser.add(wynnie);
		
		try {
			//get the server ready
		Registry registry = LocateRegistry.createRegistry(1099);
		MyRemoteImpl server = new MyRemoteImpl();
		
		server.setStoredBP(storedBP);
		server.setStoredUser(storedUser);
    	
		MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(server, 0);
		registry.rebind("MyRemote", stub);
		//MyRemote serverInterface=(MyRemote) registry.lookup("MyRemote");
		//WTF??MyRemoteClient client=new MyRemoteClient(serverInterface);
		
		//test automatically run, start here
		
		//to check the server can store all data in every two minutes, just change 1000 to 120000.
		//And also change the time interval below to 240000.
		server.startEncode(1000);
		
		//test decoder and saved data
		server.XMLEncodeAllData();
		ArrayList <BusinessPlan> XMLBP=server.XMLDecodeBP();
		ArrayList <Person> XMLUser=server.XMLDecodeUser();
		
		//test for BP Arraylist
		assertEquals(server.getStoredBP().get(0).year,XMLBP.get(0).year);
		assertEquals(server.getStoredBP().get(0).department,XMLBP.get(0).department);
		assertEquals(server.getStoredBP().get(0).isEditable,XMLBP.get(0).isEditable);
		assertEquals(server.getStoredBP().get(0).root.getName(),XMLBP.get(0).root.getName());
		assertEquals(server.getStoredBP().get(0).root.getContent(),XMLBP.get(0).root.getContent());
		assertEquals(server.getStoredBP().get(0).root.getParent(),XMLBP.get(0).root.getParent());

		//test for User Arraylist
		assertEquals(server.getStoredUser().toString(),XMLUser.toString());
		
		//edit both Arraylists
		BusinessPlan BPauto = new VMOSA();
		BPauto.year = 2030;
		BPauto.department = "MATH";
		BPauto.isEditable=true;
		
		ArrayList <BusinessPlan> storedBPAuto=new ArrayList<BusinessPlan>();
		storedBPAuto.add(BPauto);
		server.setStoredBP(storedBPAuto);
		
		Person terry=new NormalUser("terry","password","MATH", false);
		
		ArrayList <Person> storedUserAuto=new ArrayList<Person>();
		storedUserAuto.add(terry);
		server.setStoredUser(storedUserAuto);
		
		//set time interval
		//change to 240000 to check the server can store all data in every two minutes
		Thread.sleep(3000);
				
		//test after program ends
		ArrayList <BusinessPlan> AUTOBP=server.XMLDecodeBP();
		ArrayList <Person> AUTOUSER=server.XMLDecodeUser();
		storedUser.add(wynnie);
		
		//test for BP Arraylist
		assertEquals(server.getStoredBP().get(0).year,AUTOBP.get(0).year);
		assertEquals(server.getStoredBP().get(0).department,AUTOBP.get(0).department);
		assertEquals(server.getStoredBP().get(0).isEditable,AUTOBP.get(0).isEditable);
		assertEquals(server.getStoredBP().get(0).root.getName(),AUTOBP.get(0).root.getName());
		assertEquals(server.getStoredBP().get(0).root.getContent(),AUTOBP.get(0).root.getContent());
		assertEquals(server.getStoredBP().get(0).root.getParent(),AUTOBP.get(0).root.getParent());
		
		//test for User Arraylist
		assertEquals(server.getStoredUser().toString(),AUTOUSER.toString());
		
		}catch (Exception e) {
			e.printStackTrace();	//print fail
			fail("Fail");
		}
	
		
		
	}

}
