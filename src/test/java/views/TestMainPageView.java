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
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.api.FxAssert.verifyThat;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.Main;
import models.BusinessPlan;
import models.MainViewModel;
import models.MainViewTransitionModel;
import models.MyRemote;
import models.MyRemoteClient;
import models.MyRemoteImpl;
import models.NormalUser;
import models.Person;
import models.VMOSA;

/////////////// This is a functional test for the whole program ///////////////

@ExtendWith(ApplicationExtension.class)
public class TestMainPageView {
	
	
	static MyRemoteImpl server;
	static MyRemoteClient client;

	//counter
	int clickClone = 0;
	int clickCopy = 0;
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

			//initialize storedUser
			Person wynnie=new NormalUser("wynnie","wynnie","CS", true);
			Person terry=new NormalUser("terry","terry","CS", false);
			
			BP2.root.addCom("great", wynnie);
			BP.root.addCom("nice", terry);
			
			terry.followBP(BP2);
			wynnie.followBP(BP2);

			ArrayList <BusinessPlan> storedBP=new ArrayList<BusinessPlan>();
			storedBP.add(BP);
			storedBP.add(BP2);
			
			ArrayList <Person> storedUser=new ArrayList<Person>();
			storedUser.add(wynnie);
			storedUser.add(terry);
			
			////////////// Set Server & Client ////////////
			Registry registry = LocateRegistry.createRegistry(9799);
			server = new MyRemoteImpl();
			MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(new MyRemoteImpl(), 9799);
			
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

			//set initial stage and view
			FXMLLoader loader0 = new FXMLLoader();
			loader0.setLocation(Main.class.getResource("../views/MainPageShell.fxml")); 
			
			BorderPane viewM = loader0.load();
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../views/login.fxml")); 
			
			BorderPane view = loader.load();

			MainViewModel model = new MainViewModel(client,view);

			LoginController cont = loader.getController();
			MainController contM = loader0.getController();
			
			MainViewTransitionModel vm =new MainViewTransitionModel(viewM,model); 
			contM.setModel(vm);
			
		    cont.setModel(model);
		    cont.setParent(viewM, contM);

			Scene s = new Scene(view);
			stage.setScene(s);
			stage.show();
			
		}catch(Exception e) {
			e.printStackTrace();	//print fail
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

	//step 1: login
	private void login(FxRobot robot, String username, String password)
	{
		enterText(robot, username, "#usernameInput");
		enterText(robot, password, "#passwordInput");
		robot.clickOn("#login");
	}	

	//step 2: create an new BP
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
	
	//step 3: select a BP
	private void selectBP(FxRobot robot, String name)
	{
		verifyThat("#MainBPList", NodeMatchers.isNotNull());
		robot.clickOn(name);
	}
	
	
	//step 4: clone a BP
	private void cloneBP(FxRobot robot, String name, int year, Boolean bool)
	{
		robot.clickOn("#cloneOnlist");
		clickClone+=1;
		enterText(robot, name, "#cloneName");
		enterYearText(robot, year, "#cloneYear");
		if(bool) {
			robot.clickOn("#cloneBP");
		}
		else {
			robot.clickOn("#cancelClone");
		}	
	}
	
	//step 5: copy a BP
	private void copyBP(FxRobot robot)
	{
		robot.clickOn("#copyOnlist");
		clickCopy+=1;
	}
	
	//step 6: select a section
	private void selectSection(FxRobot robot, String name)
	{
		verifyThat("#outlineTree", NodeMatchers.isNotNull());
		robot.clickOn(name);
	}
	
	//add new comment
	private void addCom(FxRobot robot, String com)
	{
		enterText(robot, com, "#writeCom");
		robot.clickOn("#addCom");	
	}
	
	//step 6: select a section
	private void deleteCom(FxRobot robot, String com)
	{
		verifyThat("#Comments", NodeMatchers.isNotNull());
		robot.clickOn(com);
		robot.clickOn("#deleteCom");
	}
	
	//edit a section
	private void editSection(FxRobot robot)
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
	
	//follow a BP
	private void subBP(FxRobot robot)
	{
		robot.clickOn("#subOnlist");
	}
	
	//add children sections
	private void addSection(FxRobot robot, String name)
	{
		selectSection(robot, name);
		robot.clickOn("#Add");
	}
	
	@Test
	public void testAll(FxRobot robot) {
		try {
			Thread.sleep(1000);
			
			//login
			login(robot,"wynnie","wynnie");
			robot.clickOn("#BPlist");
			
			//create new BPs: three different situations
			newBP(robot,"VMOSA","newBP",1999, true);
			robot.clickOn("#BPlist");
			
			robot.clickOn("#personalInfo");
			
			newBP(robot,"VMOSA","peiBP",1999, true);
			robot.clickOn("#BPlist");
			
			newBP(robot,"VMOSA","peiBP",1989, false);
			robot.clickOn("#BPlist");
			
			//clone a BP
			selectBP(robot, "Giao (2020)");
			cloneBP(robot, "New", 2000, true);
			
			//copy a BP
			selectBP(robot, "Hoaho (2009)");
			copyBP(robot);
			
			Assertions.assertThat(clickClone).isEqualTo(1);
			Assertions.assertThat(clickCopy).isEqualTo(1);
		
			//view whole BP
			robot.clickOn("#ViewBP");
			
			//view a section
			selectSection(robot, "Vision");
			robot.clickOn("#viewSection");
			
			//edit comments
			addCom(robot, "haha");
			deleteCom(robot, "wynnie: great");
			
			//edit a section
			selectSection(robot, "Mission");
			robot.clickOn("#Edit");
			editSection(robot);
		
			Assertions.assertThat(clickText).isEqualTo(2);
			Assertions.assertThat(clickSave).isEqualTo(1);
			Assertions.assertThat(clickReset).isEqualTo(1);
			
			//compare
			robot.clickOn("#Compare");
			robot.clickOn("Giao (2020)");
			robot.clickOn("#compare");
			robot.clickOn("#mainPage");
			robot.clickOn("#leaveYes");
			
			//see notifications
			robot.clickOn("#SubLists");
			robot.clickOn("#BPlist");
			selectBP(robot, "Giao (2020)");
			subBP(robot);
			robot.clickOn("Giao (2020)");
			robot.clickOn("#unsub");
			robot.clickOn("#BPlist");
			selectBP(robot, "Hoaho (2009)");
			copyBP(robot);
			selectSection(robot, "Vision");
			robot.clickOn("#Edit");
			editSection(robot);
			addSection(robot, "Mission");
			Thread.sleep(3000);
			robot.clickOn("#mainPage");
			robot.clickOn("#leaveYes");
			robot.clickOn("#SubLists");
			
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
