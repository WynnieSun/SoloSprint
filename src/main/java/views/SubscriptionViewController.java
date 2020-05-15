package views;

import java.rmi.RemoteException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import models.BusinessPlan;
import models.MainViewModel;

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
      
      //model.client.askForLogin(model.client.getLoginPerson().username, model.client.getLoginPerson().username);
      
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
    void onClickUnsub(ActionEvent event) throws RemoteException {
    	BusinessPlan clickedBP=SubedBP.getSelectionModel().getSelectedItem();
    	if(clickedBP!=null) {
    		model.client.askForBP(clickedBP.year);
    		model.client.getLoginPerson().unfollowBP(clickedBP);
//    		model.client.getCurrentBP().deleteObserver(model.client.getLoginPerson());
//    		System.out.println(model.client.getLoginPerson());
//    		System.out.println(model.client.getCurrentBP().observers);
    		//model.client.updateObserver();
    		//model.client.askForLogin(model.client.getLoginPerson().username, model.client.getLoginPerson().username);
    		//System.out.println(model.client.getLoginPerson().followedBP);
    	}
    	setModel(model);
    }
	
}
