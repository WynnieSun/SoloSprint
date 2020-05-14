package views;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.ViewTransitionModelInterface;

public class MainController {
	
	ViewTransitionModelInterface model;
	

	  
    public void setModel(ViewTransitionModelInterface newModel)
    {
      model=newModel;
    }
    
    @FXML
	private Button logout;
	
	@FXML
	private Button personalInfo;
	
	@FXML
	private Button savedBPs;
	
	@FXML
	private Button createBP;
	
    @FXML
    private Button subscriptions;
    
    @FXML
    void onClickCreateNewBP(ActionEvent event) {
    	model.showEmptyBPView();
    }

    @FXML
    void onClickPersonalInfo(ActionEvent event) {
    	model.showPersonInfo();
    }

    @FXML
    void onClickSavedBPs(ActionEvent event) {
    	model.showBPlistView(model);
    } 
    
    @FXML
    void onClickLogout(ActionEvent event) {
    	model.logout(); 
    	Stage stage = (Stage) logout.getScene().getWindow();
		stage.close();
    	model.showLoginPage(this);
    }
    
    @FXML
    void onClickSub(ActionEvent event) {

    }

}
