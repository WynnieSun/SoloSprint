package views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.MainViewModel;


public class PersonalInfoController {

	MainViewModel model;
	
	 public void setModel(MainViewModel newModel)
	 {
	    model = newModel;
	    username.textProperty().set(model.client.getLoginPerson().username);
	    department.textProperty().set(model.client.getLoginPerson().department);
	    //change the textProperty of isAdmin to Yes/No
	    if(model.client.getLoginPerson().isAdmin==true) {
	    	isAdmin.textProperty().set("Yes");
	    }
	    else {
	    	isAdmin.textProperty().set("No");
	    }
	 }
	 @FXML
	 private Label username;

	 @FXML
	 private Label department;

	 @FXML
	 private Label isAdmin;

}