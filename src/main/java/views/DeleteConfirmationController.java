package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.BPMainModel;
import models.Section;

public class DeleteConfirmationController {
	
	BPMainModel model;  
	
	BPMainController maincontroller;
	
	Section clickedSection;
	
	public void setModel(BPMainModel  newModel, BPMainController cont,Section section)
    {
		model = newModel;	
		maincontroller = cont;
		clickedSection=section;
		
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
    	
		clickedSection.parent.deleteChild(clickedSection);
		model.client.uploadBP();
		maincontroller.model.showTreeView();
		maincontroller.showOutlineTree(model.client.getCurrentBP().root);
		
    }

}
