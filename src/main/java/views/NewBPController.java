package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.MainViewModel;

public class NewBPController {
	
	MainViewModel model;
	ObservableList<String> TypeList=FXCollections.observableArrayList("VMOSA", "CNTRAssessment", "BYBPlan");
	
	public void setModel(MainViewModel newModel)
	 {
		model = newModel;	
		BPtypeBox.setItems(TypeList);
	 }
    @FXML
    private ComboBox<String> BPtypeBox;

    @FXML
    private TextField NewName;

    @FXML
    private TextField HappyNewYear;

    @FXML
    private Label ErrorMes;
    
    @FXML
    void onClickCancel(ActionEvent event) {
    	NewName.textProperty().set("");
    	HappyNewYear.textProperty().set("");
    	BPtypeBox.setValue("Choose a type");
    	ErrorMes.setOpacity(0);
    }

    @FXML
    void onClickCreate(ActionEvent event) {
    	String NewBPtype = BPtypeBox.getValue().toString();
    	String NewBPName = NewName.getText();
    	String NewBPYear = HappyNewYear.getText();
    	int NewBPYearInt=-1;
		
    	try {
    		NewBPYearInt = Integer.parseInt(NewBPYear);
		}
		catch(NumberFormatException e) {
			HappyNewYear.textProperty().set("Please type a number! ");
		}
		
    	if((NewBPtype!=null)&&(NewBPName!=null)&&(NewBPYearInt!=-1)) {
    		model.client.newBP(NewBPtype);
        	model.client.getCurrentBP().name=NewBPName;
        	model.client.getCurrentBP().year=NewBPYearInt;
        	String Message=model.client.addBP();
        	ErrorMes.setText(Message);
    		ErrorMes.setOpacity(1);

    	}

    }	

}

