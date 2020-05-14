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

    @FXML
    void onClickUnsub(ActionEvent event) {
    	
    }
	
}
