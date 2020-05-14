package views;

import javafx.scene.control.TextField;

import java.rmi.RemoteException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.MainViewModel;

public class LoginController {

	MainViewModel model;
	MainController parent;
	BorderPane view;
	
	
    public void setModel(MainViewModel newModel)
    {
      model=newModel;
    }
    
    public void setParent(BorderPane viewM, MainController pt)
    {
    	view = viewM;
    	parent = pt;
    }
    
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private TextField passwordField;
    
    @FXML 
    private TextField serverField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Text error;
        
    @FXML
    void onClickLogin(ActionEvent event) throws RemoteException
    {    	
    	String username = usernameField.getText();
    	String password = passwordField.getText();
    	model.client.askForLogin(username, password);

    	if(model.client.getLoginPerson()!=null)
    	{
        	error.setOpacity(0);
        	Stage stage0 = (Stage) loginButton.getScene().getWindow();
    		stage0.close();
        	
        	Scene s = new Scene(view);
        	Stage stage = new Stage();
    		stage.setScene(s);
    		stage.setTitle("BPViewer");
    		stage.show();
        	parent.model.showPersonInfo();
    	}
    	else
    	{
    		error.setOpacity(1);
    	}
    }

 
}
