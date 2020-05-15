package views;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.BusinessPlan;
import models.MainViewModel;
import models.ViewTransitionModelInterface;

public class SubscriptionViewController {

    @FXML
    private ListView<BusinessPlan> SubedBP;

    @FXML
    private ListView<String> Notifications;

    @FXML
    private Button Unsub;
    
	MainViewModel model; 

	public ObservableList<BusinessPlan> followedBPList= FXCollections.observableArrayList();
	public ObservableList<String> notis= FXCollections.observableArrayList();
	
    public void setModel(MainViewModel newModel)
    {
      model=newModel;
      
      followedBPList.clear();
      ArrayList<BusinessPlan> Dulplicate=model.client.getLoginPerson().followedBP;
		for (int i=0; i<Dulplicate.size();i++){
			followedBPList.add(Dulplicate.get(i));
		}
		
      SubedBP.setItems(followedBPList);
      
      notis.clear();
      ArrayList<String> Dulplicate2=model.client.getLoginPerson().notifications;
		for (int i=0; i<Dulplicate2.size();i++){
			notis.add(Dulplicate2.get(i));
		}
		
      Notifications.setItems(notis); 
    }
    

    @FXML
    void onClickUnsub(ActionEvent event) {
    	BusinessPlan clickedBP=SubedBP.getSelectionModel().getSelectedItem();
    	if(clickedBP!=null) {
    		model.client.getLoginPerson().unfollowBP(clickedBP);
    		System.out.println(model.client.getLoginPerson().followedBP);
    	}
    	setModel(model);
    }
	
}
