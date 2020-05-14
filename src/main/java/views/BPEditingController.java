package views;

import java.rmi.RemoteException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import models.BPMainModel;
import models.Section;

public class BPEditingController {

	BPMainModel model;
	Section current;

	public void setModel(BPMainModel newmodel, Section cur) {
		model = newmodel;
		current = cur;
		ContentTextArea.textProperty().set(cur.content);
		sectionName.textProperty().set(cur.name);

	}

	@FXML
	private Label sectionName;

	@FXML
	private TextArea ContentTextArea;

	@FXML
	private Button saveEdit;

	@FXML
	private Button cancelEdit;
	
	@FXML
    private Label savedMessge;

	@FXML
	void onClickCancel(ActionEvent event) {
		//reset the content (show original version)
		ContentTextArea.textProperty().set(current.content);
		savedMessge.setOpacity(0);
	}

	@FXML
	void onClickSave(ActionEvent event) throws RemoteException {
		//System.out.println(model.client.getCurrentBP());
		String changed = ContentTextArea.getText();
		current.setContent(changed);
		model.client.uploadBP();
		savedMessge.setOpacity(1);

	}


}
