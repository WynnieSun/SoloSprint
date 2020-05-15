package views;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testfx.api.FxAssert.verifyThat;

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
import javafx.scene.control.ListView;
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

@ExtendWith(ApplicationExtension.class)

////////////////// Functional test & Unit test for SubscriptionsView /////////////////

public class TestNotificationSubscriptionView {
	
	static MyRemoteClient client;
	static MyRemoteImpl server;
	
	BusinessPlan BP; //currentBP
	static BusinessPlan notiBP;
	
	//counter
	int clickSectionView = 0;
	int clickSub = 0;
	int clickUnsub = 0;
	
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
			BP2.root.content=("this is the vision");
			
			notiBP = BP2;
			
			//initialize storedUser
			Person wynnie=new NormalUser("wynnie","wynnie","CS", true);
			Person terry=new NormalUser("terry","terry","CS", false);
			
			wynnie.followBP(BP2);
			terry.followBP(BP2);
			
			//add comment to root
			BP2.root.addCom("great", wynnie);
			BP.root.addCom("nice", terry);

			ArrayList <BusinessPlan> storedBP=new ArrayList<BusinessPlan>();
			storedBP.add(BP);
			storedBP.add(BP2);
			
			ArrayList <Person> storedUser=new ArrayList<Person>();
			storedUser.add(wynnie);
			storedUser.add(terry);
			
			////////////// Set Server & Client ////////////
			Registry registry = LocateRegistry.createRegistry(9299);
			server = new MyRemoteImpl();
			MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(new MyRemoteImpl(), 9299);
			
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
	
	//login
	private void login(FxRobot robot, String username, String password)
	{
		enterText(robot, username, "#usernameInput");
		enterText(robot, password, "#passwordInput");
		robot.clickOn("#login");
	}	
	
	private void newBP(FxRobot robot, String BPtype, String BPname, int BPyear)
	{
		robot.clickOn("#newBP");
		chooseType(robot, "#BPtypeBox", BPtype);
		enterText(robot, BPname, "#NameTextField");
		enterYearText(robot, BPyear, "#YearTextField");
		robot.clickOn("#createButton");
	
	}
	
	//unit test for SubscriptionList
	public void testSubList(FxRobot robot)
	{
		verifyThat("#followBPs", NodeMatchers.isNotNull());
		
		@SuppressWarnings("unchecked")
		ListView<BusinessPlan> BPList = (ListView<BusinessPlan>) robot.lookup("#followBPs")
				.queryAll().iterator().next();
		Assertions.assertThat(BPList).hasExactlyNumItems(client.getLoginPerson().followedBP.size());
		for(BusinessPlan bp: client.getLoginPerson().followedBP) {
			Assertions.assertThat(BPList).hasListCell(bp); 
		}
		
	}
	
	//unit test for NotificationsList
	public void testNotiList(FxRobot robot)
	{
		verifyThat("#notis", NodeMatchers.isNotNull());

		@SuppressWarnings("unchecked")
		ListView<String> notiList = (ListView<String>) robot.lookup("#notis")
		.queryAll().iterator().next();
		Assertions.assertThat(notiList).hasExactlyNumItems(client.getLoginPerson().notifications.size());
		for(String noti: client.getLoginPerson().notifications) {
			Assertions.assertThat(notiList).hasListCell(noti); 
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
	
	//follow a BP
	private void subBP(FxRobot robot)
	{
		robot.clickOn("#subOnlist");
		clickSub++;
	}

	//select a section
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
		robot.write(" haha!");
		robot.clickOn("#save");
	}
	
	//add children sections
	private void addSection(FxRobot robot, String name)
	{
		selectSection(robot, name);
		robot.clickOn("#Add");
	}
	
	//delete a section with children sections
	private void deleteSection(FxRobot robot, String name) {
		selectSection(robot, name);
		robot.clickOn("#Delete");
	}
	
	@Test
	public void testComments(FxRobot robot) {
		try {
			
			Thread.sleep(1000);
			
			//first round
			//login as wynnie
			//view subscriptions 
			//follow & unfollow a BP
			login(robot,"wynnie","wynnie");
			robot.clickOn("#SubLists");
			testSubList(robot);
			testNotiList(robot);
			
			robot.clickOn("#BPlist");
			selectBP(robot, "Giao (2020)");
			subBP(robot);
			testSubList(robot);
			testNotiList(robot);
			robot.clickOn("Giao (2020)");
			robot.clickOn("#unsub");
			clickUnsub++;
			testSubList(robot);
			testNotiList(robot);
			
			//second round
			//edit sections of Hoaho
			//view notifications changes
			robot.clickOn("#BPlist");
			selectBP(robot, "Hoaho (2009)");
			copyBP(robot);
			selectSection(robot, "Vision");
			editSection(robot);
			addSection(robot, "Mission");
			Thread.sleep(3000);
			robot.clickOn("#mainPage");
			robot.clickOn("#leaveYes");
			robot.clickOn("#SubLists");
			testSubList(robot);
			testNotiList(robot);

			//third round
   			//logout, then login as terry
			//see notifications changes created by others
			//edit a followed BP, see popup message window
			//unfollow that, edit it, see popup message window
   			robot.clickOn("#logout");
   			login(robot,"terry","terry");
			robot.clickOn("#SubLists");
			robot.clickOn("#BPlist");
			selectBP(robot, "Hoaho (2009)");
			copyBP(robot);
			selectSection(robot, "Vision");
			editSection(robot);
			
			Thread.sleep(3000);
			robot.clickOn("#mainPage");
			robot.clickOn("#leaveYes");
			robot.clickOn("#SubLists");
			testSubList(robot);
			testNotiList(robot);
			
			robot.clickOn("Hoaho (2009)");
			robot.clickOn("#unsub");
			clickUnsub++;
			
			robot.clickOn("#BPlist");
			selectBP(robot, "Hoaho (2009)");
			copyBP(robot);
			selectSection(robot, "Mission");
			addSection(robot, "Mission");
			Thread.sleep(3000);
			robot.clickOn("#mainPage");
			robot.clickOn("#leaveYes");
			robot.clickOn("#SubLists");
			testSubList(robot);
			testNotiList(robot);
			   			
   			//fourth round
   			//create a new BP, follow it
			//edit a section
			//view notifications
			newBP(robot,"CNTRAssessment","newBP",1999);
			robot.clickOn("#BPlist");
			selectBP(robot, "newBP (1999)");
			subBP(robot);
			robot.clickOn("#BPlist");
			selectBP(robot, "newBP (1999)");
			copyBP(robot);
			deleteSection(robot, "Program Mission Statement");
			robot.clickOn("#yesDe");
			
			Thread.sleep(3000);
			robot.clickOn("#mainPage");
			robot.clickOn("#leaveYes");
			robot.clickOn("#SubLists");
			testSubList(robot);
			testNotiList(robot);
			
			Assertions.assertThat(clickSub).isEqualTo(2);			
			Assertions.assertThat(clickUnsub).isEqualTo(2);
			
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
