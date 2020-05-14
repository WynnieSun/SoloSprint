package views;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testfx.api.FxAssert.verifyThat;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
import models.Comment;
import models.MyRemoteClient;
import models.MyRemoteImpl;
import models.NormalUser;
import models.Person;
import models.VMOSA;

@ExtendWith(ApplicationExtension.class)

////////////////// Functional test & Unit test for SectionView /////////////////

public class TestSectionView {
	
	static MyRemoteImpl server;
	static MyRemoteClient client;
	
	BusinessPlan BP; //currentBP
	
	//counter
	int clickSectionView = 0;
	
	@BeforeAll
	//Initialize server and client 
	static void Initialization()
	{		
		try
		{		
			Registry registry = LocateRegistry.createRegistry(1699);

			server = new MyRemoteImpl();
			
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
			
			//initialize storedUser
			Person wynnie=new NormalUser("wynnie","wynnie","CS", true);
			Person terry=new NormalUser("terry","terry","CS", false);
			
			//add comment to root
			BP2.root.addCom("great", wynnie);
			BP.root.addCom("nice", terry);

			ArrayList <BusinessPlan> storedBP=new ArrayList<BusinessPlan>();
			storedBP.add(BP);
			storedBP.add(BP2);
			
			ArrayList <Person> storedUser=new ArrayList<Person>();
			storedUser.add(wynnie);
			storedUser.add(terry);
			
			server.setStoredBP(storedBP);
			server.setStoredUser(storedUser);
			
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		client = new MyRemoteClient(server);
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
	//int to string
	private void enterYearText(FxRobot robot, int text, String target)
	{
		robot.clickOn(target);
		robot.write(Integer.toString(text));
	}
	
	private void login(FxRobot robot, String username, String password)
	{
		enterText(robot, username, "#usernameInput");
		enterText(robot, password, "#passwordInput");
		robot.clickOn("#login");
	}	
	
	//unit test for CommentsList
	public void testComList(FxRobot robot)
	{
		verifyThat("#Comments", NodeMatchers.isNotNull());
		
		@SuppressWarnings("unchecked")
		ListView<Comment> comList = (ListView<Comment>) robot.lookup("#Comments")
				.queryAll().iterator().next();
		Assertions.assertThat(comList).hasExactlyNumItems(client.getCurrentBP().getRoot().comments.size());
		System.out.println(client.getCurrentBP().getRoot().comments);
		for(Comment com: client.getCurrentBP().getRoot().comments) {
			Assertions.assertThat(comList).hasListCell(com); 
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
	
	private void cloneBP(FxRobot robot, String name, int year)
	{
		robot.clickOn("#cloneOnlist");
		enterText(robot, name, "#cloneName");
		enterYearText(robot, year, "#cloneYear");
		robot.clickOn("#cloneBP");
	}

	private void selectSection(FxRobot robot, String name)
	{
		verifyThat("#outlineTree", NodeMatchers.isNotNull());
		robot.clickOn(name);
	}
	
	//show sectionView
	private void showSection(FxRobot robot, String name)
	{
		selectSection(robot, name);
		robot.clickOn("#viewSection");
		clickSectionView++;
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
	
	@Test
	public void testComments(FxRobot robot) {
		try {
			
			Thread.sleep(1000);
			
			//first round
			//view a section of Hoaho, which is editable
			//add & delete a comment
			showSection(robot, "Vision");
			addCom(robot, "good");
			testComList(robot);
			deleteCom(robot, "wynnie: great");
			testComList(robot);
			showSection(robot, "Mission"); //a section without comment
			
			//second round
			//view a section of Giao, which is not editable
			//view comments only
			robot.clickOn("#mainPage");
			robot.clickOn("#leaveYes");
			robot.clickOn("#BPlist");
			selectBP(robot, "Giao (2020)");
			copyBP(robot);
			showSection(robot, "Vision");
			testComList(robot);
			robot.clickOn("#addCom");
			
			//third round
   			//logout, then login as terry
			//view a section of Hoaho, whose comments have been edited at the first round
			//add a comment
			robot.clickOn("#mainPage");
			robot.clickOn("#leaveYes");
   			robot.clickOn("#logout");
   			login(robot,"terry","terry");
   			
			robot.clickOn("#BPlist");
			selectBP(robot, "Hoaho (2009)");
			copyBP(robot);
			showSection(robot, "Vision");
			testComList(robot);
			addCom(robot, "nice job");
			testComList(robot);
   			
   			//fourth round
   			//clone a BP, which has been modified at the previous rounds
			//view a section
			//delete a comment
			robot.clickOn("#mainPage");
			robot.clickOn("#leaveYes");
			robot.clickOn("#BPlist");
			selectBP(robot, "Hoaho (2009)");
			cloneBP(robot, "newBP", 2000);
			
			selectBP(robot, "newBP (2000)");
			copyBP(robot);
			showSection(robot, "Vision");
			testComList(robot);
			deleteCom(robot, "terry: nice job");
			testComList(robot);
			
			Assertions.assertThat(clickSectionView).isEqualTo(5);
			
		    Assertions.assertThat(robot.lookup("#labelCom")
		            .queryAs(Label.class)).hasText("Comments");    
			
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
