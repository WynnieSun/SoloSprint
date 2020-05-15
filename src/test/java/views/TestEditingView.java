package views;
import static org.junit.jupiter.api.Assertions.fail;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.Main;
import models.BPMainModel;
import models.BusinessPlan;
import models.MyRemote;
import models.MyRemoteClient;
import models.MyRemoteImpl;
import models.NormalUser;
import models.Person;
import models.VMOSA;

@ExtendWith(ApplicationExtension.class)
public class TestEditingView {
	
	
	static MyRemoteImpl server;
	static MyRemoteClient client;
	
	BusinessPlan BP;
	
	//counter
	int clickText = 0;
	int clickSave = 0;
	int clickReset = 0;
	
	@BeforeAll
	//Initialize server and client 
	static void Initialization() throws Exception
	{		
		try
		{		
			//initialize storedBP
			BusinessPlan BP = new VMOSA();
			BP.name="Giao";
			BP.year = 2020;
			BP.department ="CS";
			BP.isEditable=false;
			BP.addSection(BP.root);
			BP.root.content=("this is the vision");
			BP.root.children.get(0).content=("this is the misson");
			BP.addSection(BP.root.children.get(0));

			BusinessPlan BP2 = new VMOSA();
			BP2.name="Hoaho";
			BP2.year = 2009;
			BP2.department ="CS";
			BP2.isEditable=true;
			BP2.addSection(BP2.root);
			BP2.root.content = "Edit Me Please";

			ArrayList <BusinessPlan> storedBP=new ArrayList<BusinessPlan>();
			storedBP.add(BP);
			storedBP.add(BP2);

			//initialize storedUser
			Person wynnie=new NormalUser("wynnie","wynnie","CS", true);
			Person terry=new NormalUser("terry","terry","CS", false);

			ArrayList <Person> storedUser=new ArrayList<Person>();
			storedUser.add(wynnie);
			storedUser.add(terry);
			
			////////////// Set Server & Client ////////////
			Registry registry = LocateRegistry.createRegistry(9989);
			server = new MyRemoteImpl();
			MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(new MyRemoteImpl(), 9989);
			
			registry.bind("MyRemote", stub);
			System.err.println("Server ready");
			
			MyRemote remoteService = (MyRemote) Naming
					.lookup("//localhost:9989/MyRemote");
			client = new MyRemoteClient(server);
			remoteService.addObserver(client);
		    
		    //initialize stored data
			server.setStoredBP(storedBP);
			server.setStoredUser(storedUser);
			
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	@Start //Before
	private void start(Stage stage)
	{
		
		try {
			//set login user and current BP
			client.askForLogin("wynnie", "wynnie");
			client.askForBP(2009);
			BusinessPlan bp = client.getCurrentBP();
			BP = bp;

			//set initial stage and view
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../views/EditingView.fxml")); 
			
			BorderPane view = loader.load();
			
			BPMainModel model = new BPMainModel(client,view);

			BPEditingController cont = loader.getController();
			
		    cont.setModel(model,BP.root);

			Scene s = new Scene(view);
			stage.setScene(s);
			stage.show();
			
		}catch(Exception e) {
			e.printStackTrace();	
			fail("Fail");
		}
	}

	
	//general input text method
	private void enterText(FxRobot robot)
	{
		robot.clickOn("#text");
		clickText+=1;
		robot.write(" haha NO!");
		robot.clickOn("#save");
		clickSave+=1;
		
		robot.clickOn("#text");
		clickText+=1;
		robot.write(" lalalala~");
		robot.clickOn("#reset");
		clickReset+=1;
	}

	@Test
	public void testAll(FxRobot robot) {
		try {
			Thread.sleep(1000);

			enterText(robot);
			
			Assertions.assertThat(clickText).isEqualTo(2);
			Assertions.assertThat(clickSave).isEqualTo(1);
			Assertions.assertThat(clickReset).isEqualTo(1);
			
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
