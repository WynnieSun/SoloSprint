package views;
import static org.junit.jupiter.api.Assertions.fail;

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
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import main.Main;
import models.BPMainModel;
import models.BusinessPlan;
import models.MyRemote;
import models.MyRemoteClient;
import models.MyRemoteImpl;
import models.NormalUser;
import models.Person;
import models.Section;
import models.VMOSA;

@ExtendWith(ApplicationExtension.class)
public class TestBPMainView {
	
	static MyRemoteImpl server;
	static MyRemoteClient client;
	
	Scene scene;
	BusinessPlan BP;
	
	//counter
	int clickMainPage = 0;
	int clickYes = 0; 
	int clickNo = 0;
	int clickBP = 0;
	
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

			ArrayList <BusinessPlan> storedBP=new ArrayList<BusinessPlan>();
			storedBP.add(BP);
			storedBP.add(BP2);

			//initialize storedUser
			Person wynnie=new NormalUser("wynnie","wynnie","CS", true);
			Person terry=new NormalUser("terry","terry","CS", false);
			
			wynnie.followBP(BP2);

			ArrayList <Person> storedUser=new ArrayList<Person>();
			storedUser.add(wynnie);
			storedUser.add(terry);
			
			////////////// Set Server & Client ////////////
			Registry registry = LocateRegistry.createRegistry(9979);
			server = new MyRemoteImpl();
			MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(new MyRemoteImpl(), 9979);
			
			registry.bind("MyRemote", stub);
			System.err.println("Server ready");
			
			client = new MyRemoteClient(server);
		    
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
			loader.setLocation(Main.class.getResource("../views/BPMainView.fxml")); 
			
			BorderPane view = loader.load();
			
			BPMainModel model = new BPMainModel(client,view);

			BPMainController cont = loader.getController();
			
		    cont.setModel(model);

			Scene s = new Scene(view);
			scene = s;
			stage.setScene(s);
			stage.show();
			
		}catch(Exception e) {
			e.printStackTrace();	
			fail("Fail");
		}
	}

	
	public void clickTreeView(FxRobot robot)
	{
		@SuppressWarnings("unchecked")
		TreeView<Section> outlinetree = (TreeView<Section>) scene.lookup("#outlineTree");
		outlinetree.getRoot();
		System.out.println(outlinetree.getRoot());
		Assertions.assertThat(outlinetree.getRoot().getValue()).isEqualTo(BP.root);
		
		@SuppressWarnings("unchecked")
		TreeView<String> contenttree = (TreeView<String>) scene.lookup("#contentTree");
		contenttree.getRoot();
		System.out.println(contenttree.getRoot());
		Assertions.assertThat(contenttree.getRoot().getValue()).isEqualTo(BP.root.showContent());
	}

	
	private void clickViewBP(FxRobot robot)
	{
		robot.clickOn("#ViewBP");
		clickBP+=1;
	}
	
	private void clickLeave(FxRobot robot)
	{
		robot.clickOn("#mainPage");
		clickMainPage+=1;
		robot.clickOn("#leaveNo");
		clickNo+=1;
		robot.clickOn("#mainPage");
		clickMainPage+=1;
		robot.clickOn("#leaveYes");
		clickYes+=1;
	}

	@Test
	public void testAll(FxRobot robot) {
		try {
			Thread.sleep(1000);
			
			//test treeviews
			clickTreeView(robot);
			
			//test View BP button
			clickViewBP(robot);
			Assertions.assertThat(clickBP).isEqualTo(1);
			Thread.sleep(1000);
			
			//test go back to main page
			clickLeave(robot);
			Assertions.assertThat(clickMainPage).isEqualTo(2);
			Assertions.assertThat(clickYes).isEqualTo(1);
			Assertions.assertThat(clickYes).isEqualTo(1);
			
   			//logout
   			robot.clickOn("#logout");
			
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
