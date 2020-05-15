package views;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import models.BPMainModel;
import models.Comment;
import models.Person;
import models.Section;

public class SectionViewController {

	@FXML
	private Button Delete;

	@FXML
	private Button Add;

	@FXML
	private Label SectionName;

	@FXML
	private Text SectionContent;

	@FXML
	private ListView<Comment> Comments;
	
    @FXML
    private TextArea newComment;
    
	BPMainModel model;
	
	Section current;
	
	public ObservableList<Comment> commentsList = FXCollections.observableArrayList();
    
	public void setModel(BPMainModel newmodel, Section cur) {
		model = newmodel;
		current = cur;

		//show section info details
		SectionName.textProperty().set(cur.name);
		SectionContent.textProperty().set(cur.content);
		
		if(model.client.getCurrentBP().isEditable==false) {
	    	//can not use buttons to make changes
	    	Delete.setDisable(true);
	    	Add.setDisable(true);
	    }

		commentsList.clear();
		ArrayList<Comment> Dulplicate = cur.getComments();
		for (int i=0; i<Dulplicate.size();i++){
			commentsList.add(Dulplicate.get(i));
		}

		Comments.setItems(commentsList);
    	
	}

	@FXML
	void onClickAdd(ActionEvent event) {
		Person person = model.client.getLoginPerson();
		String content = newComment.getText();
		current.addCom(content, person);
		model.client.uploadBP();
		setModel(model, current);
	}

	@FXML
	void onClickDelete(ActionEvent event) {
		Comment clickedCom = Comments.getSelectionModel().getSelectedItem();
		if(clickedCom!=null) {
			current.deleteCom(clickedCom);
			model.client.uploadBP();
			setModel(model, current);
		}
	}

}
