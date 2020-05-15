package views;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.BPMainModel;
import models.BusinessPlan;

public class CompareBPController {

	BPMainModel model; 
	
	public ObservableList<BusinessPlan> BPList= FXCollections.observableArrayList();
	public ObservableList<String> diffList= FXCollections.observableArrayList();
	
	public void setModel(BPMainModel newModel)
	{
		model=newModel;
		
		ArrayList<BusinessPlan> Dulplicate=model.client.askForAllBP();
		for (int i=0; i<Dulplicate.size();i++){
			if((Dulplicate.get(i).name.equals(model.client.getCurrentBP().name))
					&& (Dulplicate.get(i).year == model.client.getCurrentBP().year)) {
				//do nothing
			}
			else {
				BPList.add(Dulplicate.get(i));
			}
		}

		BPCompList.setItems(BPList);  
	}

	@FXML
	private Button compareBP;

	@FXML
	private ListView<BusinessPlan> BPCompList;

    @FXML
    private Label BP2;

    @FXML
    private ListView<String> DiffList;
	
	@FXML
	void onCllickCompareBP(ActionEvent event) {
		BusinessPlan clickedBP=BPCompList.getSelectionModel().getSelectedItem();
    	if(clickedBP!=null) {
    		
    		diffList.clear();
    		ArrayList<String> diff = model.client.showDiff(clickedBP);
    		for (int i=0; i<diff.size();i++){
    				diffList.add(diff.get(i));
    			}
    		}

    		DiffList.setItems(diffList); 
        

	}
	
}
