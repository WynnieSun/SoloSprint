package main;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.BusinessPlan;
import models.MainViewModel;
import models.MainViewTransitionModel;
import models.MyRemote;
import models.MyRemoteClient;
import models.MyRemoteImpl;
import models.NormalUser;
import models.Person;
import models.VMOSA;
import views.LoginController;
import views.MainController;

public class Main extends Application {
	
	static MyRemoteImpl server;
	static MyRemoteClient client;

	@Override
	public void start(Stage stage) throws Exception {
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

			//initialize storedUser
			Person wynnie=new NormalUser("wynnie","wynnie","CS", true);
			Person terry=new NormalUser("terry","terry","CS", false);
			
			wynnie.followBP(BP2);
			
			BP2.root.addCom("great", wynnie);
			BP.root.addCom("nice", terry);

			ArrayList <Person> storedUser=new ArrayList<Person>();
			storedUser.add(wynnie);
			storedUser.add(terry);
			
			ArrayList <BusinessPlan> storedBP=new ArrayList<BusinessPlan>();
			storedBP.add(BP);
			storedBP.add(BP2);

			////////////// Set Server & Client ////////////
			Registry registry = LocateRegistry.createRegistry(9999);
			server = new MyRemoteImpl();
			MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(new MyRemoteImpl(), 9999);
			
			registry.bind("MyRemote", stub);
			System.err.println("Server ready");
			
			MyRemote remoteService = (MyRemote) Naming
					.lookup("//localhost:9999/MyRemote");
			client = new MyRemoteClient(server);
			remoteService.addObserver(client);
		    
		    //initialize stored data
			server.setStoredBP(storedBP);
			server.setStoredUser(storedUser);
		
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}


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
		stage.setTitle("BPViewer");
		stage.show();
		
	}
	public static void main (String [] args) {
		launch(args);
		 
	}
}