package models;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.Main;
import views.BPListController;
import views.LoginController;
import views.MainController;
import views.NewBPController;
import views.PersonalInfoController;
import views.SubscriptionViewController;

public class MainViewTransitionModel implements ViewTransitionModelInterface {

	BorderPane mainview;
	MainViewModel model;
	
	public MainViewTransitionModel(BorderPane view,MainViewModel newModel)
	  {
	    mainview = view;
	    model = newModel;
	  }

	@Override
	public void showPersonInfo() {
		
		FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(MainViewTransitionModel.class
	        .getResource("../views/PersonalInfoView.fxml"));
	    try {
	      Pane view = loader.load();
	      mainview.setCenter(view);
	      PersonalInfoController cont = loader.getController();
	      cont.setModel(model);
	      
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
		
	}

	@Override
	public void showBPlistView(ViewTransitionModelInterface vm) {
		FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(MainViewTransitionModel.class
	        .getResource("../views/BPListView.fxml"));
	    try {
	      Pane view = loader.load();
	      mainview.setCenter(view);
	      BPListController cont = loader.getController();
	      cont.setModel(model,vm);
	      
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
		
	}
	
	@Override
	public void showSubView() {
		FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(MainViewTransitionModel.class
	        .getResource("../views/SubscriptionsView.fxml"));
	    try {
	      Pane view = loader.load();
	      mainview.setCenter(view);
	      SubscriptionViewController cont = loader.getController();
	      cont.setModel(model);
	      
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
		
	}

	@Override
	public void showEmptyBPView() {
		FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(MainViewTransitionModel.class
	        .getResource("../views/EmptyBPView.fxml"));
	    try {
	      Pane view = loader.load();
	      mainview.setCenter(view);
	      NewBPController cont = loader.getController();
	      cont.setModel(model);
	      
	      
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
		
	}

	@Override
	public void logout() {
		model.client.logOut();	
		
	}

	@Override
	public void showLoginPage(MainController cont) 
	{
		try {
			FXMLLoader loader0 = new FXMLLoader();
			loader0.setLocation(Main.class.getResource("../views/MainPageShell.fxml")); 

			BorderPane viewM = loader0.load();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../views/login.fxml")); 

			BorderPane view = loader.load();

			MainViewModel modelM = new MainViewModel(model.client,view);

			LoginController contL = loader.getController();
			MainController contM = loader0.getController();

			MainViewTransitionModel vm =new MainViewTransitionModel(viewM,modelM); 
			contM.setModel(vm);

			contL.setModel(modelM);
			contL.setParent(viewM, contM);

			Stage stage = new Stage();
			Scene s = new Scene(view);
			stage.setScene(s);
			stage.setTitle("BPViewer");
			stage.show();
 
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	
}
