package views;

import org.controlsfx.control.Notifications;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
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
		Notifications  notification = Notifications.create()
				.title(" Message")
				.text(" You have made change. We will notify users who have subscribed this BP")
				.hideAfter(Duration.seconds(2))
				.position(Pos.TOP_RIGHT);
		notification.show();
		maincontroller.model.showTreeView();
		maincontroller.showOutlineTree(model.client.getCurrentBP().root);
		
    }

}
