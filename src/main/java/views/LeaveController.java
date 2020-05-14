package views;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.BPMainModel;

public class LeaveController {
	
	BPMainModel model;  
	
	//previous working BPMain window
	Stage stageP;
	
	public void setModel(BPMainModel  newModel, Stage stage)
    {
		model = newModel;	
		stageP = stage;
		
    }
	
    @FXML
    private Button YesButton;

    @FXML
    private Button NoButton;

	@FXML
    void onClickNo(ActionEvent event) {
		Stage stage = (Stage) NoButton.getScene().getWindow();
		stage.close();
		
    }

    @FXML
    void onClickYes(ActionEvent event) {
		Stage stage = (Stage) YesButton.getScene().getWindow();
		stage.close();
		
		//close the previous working BPMain window
		stageP.close();
		
    	//show MainView window
    	model.showMainView();	
    }
	
	
}
