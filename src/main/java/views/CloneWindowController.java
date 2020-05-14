package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.BusinessPlan;
import models.MainViewModel;
import models.ViewTransitionModelInterface;

public class CloneWindowController {

	MainViewModel model; 
	ViewTransitionModelInterface supermodel;

	
	 public void setModel(MainViewModel newModel, ViewTransitionModelInterface vm)
	    {
	      model=newModel;	
	      supermodel=vm;
	      currentBPName.textProperty().set(newModel.client.getCurrentBP().toString());	      
	    }
	 
    @FXML
    private Label currentBPName;

    @FXML
    private TextField NewBPName;

    @FXML
    private TextField year;
    
    @FXML
    private Label Message;
    @FXML
    void onClickCancel(ActionEvent event) {
    	NewBPName.textProperty().set("");
    	year.textProperty().set("");
    	Message.setOpacity(0);

    }

    @FXML
    void onClickClone(ActionEvent event) {
    	String CloneBPName = NewBPName.getText();
    	String CloneBPyear = year.getText();
    	int CloneBPYearInt=-1;
    	
    	try {
    		CloneBPYearInt = Integer.parseInt(year.getText());
		}
		catch(NumberFormatException e) {
			year.textProperty().set("Please type a number! ");
		}
    	
    	if(CloneBPName!=null&&CloneBPyear!=null&&CloneBPYearInt!=-1) {
    		if(Integer.parseInt(year.getText())<1819) {
        		Message.textProperty().set("Please use appropriate year attribute.");
        		Message.setOpacity(1);	
        	}
        	else {
        		if(Integer.parseInt(year.getText())!=model.client.getCurrentBP().year) {
                	//deep copy
        			BusinessPlan oldBP = model.client.getCurrentBP();
                	model.client.newBP(oldBP.type);
                	model.client.getCurrentBP().root=oldBP.getRoot();
                	model.client.getCurrentBP().isEditable=oldBP.isEditable;
                	model.client.getCurrentBP().name=NewBPName.getText();
                	model.client.getCurrentBP().year=Integer.parseInt(year.getText());
                	model.client.uploadBP();
                	Message.setOpacity(1);
                	Stage stage = (Stage) Message.getScene().getWindow();
            		stage.close();
            		supermodel.showBPlistView(supermodel);
        		}
            	else {
            		Message.textProperty().set("Please copy with a different year");
            		Message.setOpacity(1);			
            	}
        	}
    	}
    	
    	
    	
    }

}
