package views;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testfx.api.FxAssert.verifyThat;

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
import org.testfx.matcher.base.NodeMatchers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

////////////////// Functional test & Unit test for CompareView /////////////////

public class TestCompareView {
	
	static MyRemoteImpl server;
	static MyRemoteClient client;
	
	BusinessPlan BP; //currentBP
	static BusinessPlan comBP; //Giao
	static BusinessPlan comBP2; //Hoaho
	
	//counter
	int clickMainPage = 0;
	int clickCompare = 0;
	int clickYes = 0;
	int clickNo = 0;
	
	@BeforeAll
	//Initialize server and client 
	static void Initialization()throws Exception
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
			
			comBP = BP;

			BusinessPlan BP2 = new VMOSA();
			BP2.name="Hoaho";
			BP2.year = 2009;
			BP2.department ="CS";
			BP2.isEditable=true;
			BP2.addSection(BP2.root);
			BP2.root.content=("this is the vision");

			comBP2 = BP2;
			
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
			Registry registry = LocateRegistry.createRegistry(9299);
			server = new MyRemoteImpl();
			MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(new MyRemoteImpl(), 9299);
			
			registry.bind("MyRemote", stub);
			System.err.println("Server ready");
			
			MyRemote remoteService = (MyRemote) Naming
					.lookup("//localhost:9299/MyRemote");
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
			loader.setLocation(Main.class.getResource("../views/BPMainView.fxml")); 
			
			BorderPane view = loader.load();
			
			BPMainModel model = new BPMainModel(client,view);

			BPMainController cont = loader.getController();
			
		    cont.setModel(model);

			Scene s = new Scene(view);

			stage.setScene(s);
			stage.show();
			
		}catch(Exception e) {
			e.printStackTrace();	
			fail("Fail");
		}
	}

	//general input text method
	private void enterText(FxRobot robot, String text, String target)
	{
		robot.clickOn(target);
		robot.write(text);
	}
	//comboBox
	private void chooseType(FxRobot robot, String target, String item) 
	{
		robot.clickOn(target);
		robot.clickOn(item);
	}
	//int to string
	private void enterYearText(FxRobot robot, int text, String target)
	{
		robot.clickOn(target);
		robot.write(Integer.toString(text));
	}

	//create an new BP
	private void newBP(FxRobot robot, String BPtype, String BPname, int BPyear, boolean create)
	{
		robot.clickOn("#newBP");
		chooseType(robot, "#BPtypeBox", BPtype);
		enterText(robot, BPname, "#NameTextField");
		enterYearText(robot, BPyear, "#YearTextField");
		if (create) {
			robot.clickOn("#createButton");
		}
		else {
			robot.clickOn("#cancelButton");
		}
		
	}
	
	//unit test for CompareBPList
	public void testBPList(FxRobot robot, BusinessPlan comBP)
	{
		verifyThat("#comBPList", NodeMatchers.isNotNull());
		
		@SuppressWarnings("unchecked")
		ListView<BusinessPlan> BPList = (ListView<BusinessPlan>) robot.lookup("#comBPList")
				.queryAll().iterator().next();
		Assertions.assertThat(BPList).hasExactlyNumItems(client.askForAllBP().size()-1);
		Assertions.assertThat(BPList).hasListCell(comBP); 
		
	}
	
	//unit test for DiffSectionList
	public void testDiffList(FxRobot robot, BusinessPlan comBP)
	{
		verifyThat("#diffList", NodeMatchers.isNotNull());

		@SuppressWarnings("unchecked")
		ListView<String> secList = (ListView<String>) robot.lookup("#diffList")
		.queryAll().iterator().next();
		System.out.println(client.showDiff(comBP).size());
		Assertions.assertThat(secList).hasExactlyNumItems(client.showDiff(comBP).size());
		for(String sec: client.showDiff(comBP)) {
			Assertions.assertThat(secList).hasListCell(sec); 
		}
		
	}
	
	//select a BP
	private void selectBP(FxRobot robot, String name)
	{
		verifyThat("#MainBPList", NodeMatchers.isNotNull());
		robot.clickOn(name);
	}
	
	//copy a BP
	private void copyBP(FxRobot robot)
	{
		robot.clickOn("#copyOnlist");
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

	private void selectSection(FxRobot robot, String name)
	{
		verifyThat("#outlineTree", NodeMatchers.isNotNull());
		robot.clickOn(name);
	}
	
	//edit a section
	private void editSection(FxRobot robot)
	{
		robot.clickOn("#Edit");
		robot.clickOn("#text");
		robot.write(" haha NO!");
		robot.clickOn("#save");
		robot.clickOn("#text");
		robot.write(" lalalala~");
		robot.clickOn("#reset");
	}
	
	//add children sections
	private void addSection(FxRobot robot, String name)
	{
		selectSection(robot, name);
		robot.clickOn("#Add");
	}
	
	//compare two BPs
	private void compare(FxRobot robot, String name)
	{
		robot.clickOn("#Compare");
		clickCompare++;
		robot.clickOn(name);
		robot.clickOn("#compare");
	}
	
	
	@Test
	public void testCompare(FxRobot robot) {
		try {
			
			Thread.sleep(1000);
			
			//first round
			//compare Hoaho (currentBP) with Giao
			compare(robot,"Giao (2020)");
			testBPList(robot, comBP);
			testDiffList(robot, comBP);
			
			//second round
			//edit a section, then compare
			//compare Hoaho (currentBP) with Giao
			selectSection(robot, "Vision");
			editSection(robot);
			compare(robot, "Giao (2020)");
			testBPList(robot, comBP);
			testDiffList(robot, comBP);
			
			//third round
			//create a new BP, pick it
			//compare newBP (currentBP) with Hoaho
			clickLeave(robot);
			Assertions.assertThat(clickMainPage).isEqualTo(2);
			Assertions.assertThat(clickYes).isEqualTo(1);
			Assertions.assertThat(clickNo).isEqualTo(1);
			
			newBP(robot,"BYBPlan","newBP",1999, true);
			robot.clickOn("#BPlist");
			
			selectBP(robot, "newBP (1999)");
			copyBP(robot);
			
			compare(robot,"Hoaho (2009)");
			testBPList(robot, comBP2);
			testDiffList(robot, comBP2);
			
			//fourth round
			//add children sections, then compare
			//compare newBP (currentBP) with Giao
			addSection(robot, "BYB Mission Statement");
			
			compare(robot,"Giao (2020)");
			testBPList(robot, comBP);
			testDiffList(robot, comBP);
			
		    Assertions.assertThat(robot.lookup("#labelDiff")
		            .queryAs(Label.class)).hasText("Different Sections"); 
			
			Assertions.assertThat(clickCompare).isEqualTo(4);
			
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
