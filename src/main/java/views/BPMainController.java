package views;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.ArrayList;

import org.controlsfx.control.Notifications;

import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.BPMainModel;
import models.BYBPlan;
import models.BusinessPlan;
import models.CNTRAssessment;
import models.Section;
import models.VMOSA;


public class BPMainController {


	@FXML
	private Label BPname;

	@FXML
	private Label BPyear;

	@FXML
	private Label BPdep;

	@FXML
	private Label BPtype;

	@FXML
	private Label BPedi;

	@FXML
	private TreeView<Section> outlineTree;

	@FXML
    private TreeView<String> contentTree;

	@FXML
	private Button MainPage;
	
	@FXML
    private Button ViewBP;

    @FXML
    private Button ViewSection;
	
	@FXML
	private Button Edit;

	@FXML
	private Button Delete;

	@FXML
	private Button Add;
	
    @FXML
    private Button Compare;

	BPMainModel model;
	
	Stage currentStage;
	
    //add tree items to the outline tree
	private void addNodes(Section p, TreeItem<Section> t) {
		t.setExpanded(true);
		for (Section child : p.children) {
			TreeItem<Section> node = new TreeItem<Section>(child);
			t.getChildren().add(node);
			addNodes(child, node);
		}
	}
	
	//add tree items to the content tree
	private void addStringNodes(Section p, TreeItem<String> t) {
		t.setExpanded(true);
		for (Section child : p.children) {
			TreeItem<String> node = new TreeItem<String>(child.showContent());
			t.getChildren().add(node);
			addStringNodes(child, node);
		}
	}
	
	public void showOutlineTree(Section root){
		TreeItem<Section> rootItem = new TreeItem<Section>(root);
		addNodes(root, rootItem);
		outlineTree.setShowRoot(true);
		outlineTree.setRoot(rootItem);
	}
	
	private void showContentTree(Section root) {
		String rootContent = root.showContent();
		TreeItem<String> rootItem2 = new TreeItem<String>(rootContent);
		addStringNodes(root, rootItem2);
		contentTree.setShowRoot(true);
		contentTree.setRoot(rootItem2);
	}
	
	//helper function for addSection
	private Section newAddBP(String Type) {
		if(Type.equals("VMOSA")) {
			BusinessPlan BP = new VMOSA();
			return BP.root;
		}
		else if(Type.equals("BYBPlan")){
			BusinessPlan BP = new BYBPlan();
			return BP.root;
		}
		else if(Type.equals("CNTRAssessment")) {
			BusinessPlan BP = new CNTRAssessment();
			return BP.root;
		}
		else {
			return null;
		}
	}
	
	public void setModel(BPMainModel newmodel) {
		model = newmodel;
		Section root = model.client.getCurrentBP().getRoot();
		
		//show BP info details
		BPname.textProperty().set(model.client.getCurrentBP().name);
		BPyear.textProperty().set(Integer.toString(model.client.getCurrentBP().year));
		BPdep.textProperty().set(model.client.getCurrentBP().department);
		BPtype.textProperty().set(model.client.getCurrentBP().type);
		if(model.client.getCurrentBP().isEditable==true) {
	    	BPedi.textProperty().set("Yes");
	    }
	    else {
	    	BPedi.textProperty().set("No");
	    	//can not use buttons to make changes
	    	Edit.setDisable(true);
	    	Delete.setDisable(true);
	    	Add.setDisable(true);
	    }
		
		//set tree views
		showOutlineTree(root);
		showContentTree(root);
    	
	}
	
    @FXML
    void onClickBack(ActionEvent event) {
    	//save the current working BPMain window
    	Stage stage = (Stage) MainPage.getScene().getWindow();
        currentStage = stage;
    	
    	//show Leave Confirmation window
    	model.showLeaveConfirm(stage);

    }
    
    //helper function of adding children
    private Section findCorrespondingChild(Section selectedroot, Section searchingroot) {
    	if(searchingroot.children.size()!=0) {
    		if(selectedroot.name.equals(searchingroot.name)) {
        		return searchingroot.children.get(0);
        	}
        	else {
        		return findCorrespondingChild(selectedroot, searchingroot.children.get(0));
        	}
    	}
    	else {
    		return null;
    	}
    }
    
    @FXML
    void onClickAdd(ActionEvent event) throws IOException {
    	TreeItem<Section> SelectedItem=outlineTree.getSelectionModel().getSelectedItem();
		Section root=model.client.getCurrentBP().root;
		//since root can't be deleted or added.
		ArrayList<String> lastSection=new ArrayList<String>();
		lastSection.add("Action Plan");
		lastSection.add("BYB Plan");
		lastSection.add("Program Goals and Student Learning Objective");
		if(SelectedItem!=null&&(lastSection.contains(SelectedItem.getValue().name)!=true)) {
			Section newChildroot=newAddBP(model.client.getCurrentBP().type);
			Section child=findCorrespondingChild(SelectedItem.getValue(),newChildroot);
			
			SelectedItem.getValue().addChild(child);
			model.client.uploadBP();
			Notifications  notification = Notifications.create()
					.title(" Message")
					.text(" You have made change. We will notify users who have subscribed this BP")
					.hideAfter(Duration.seconds(2))
					.position(Pos.TOP_RIGHT);
			notification.show();
			model.showTreeView();
			showOutlineTree(root);}
    }

    @FXML
    void onClickDelete(ActionEvent event) {
    	TreeItem<Section> SelectedItem=outlineTree.getSelectionModel().getSelectedItem();
    	Section root=model.client.getCurrentBP().root;
    	//since root can't be deleted or added.
    	if(SelectedItem!=null&&SelectedItem.getValue()!=root) {
    		Stage stage = (Stage) MainPage.getScene().getWindow();
            currentStage = stage;
        	
        	//show Leave Confirmation window
        	model.showDeleteConfirm(this,SelectedItem.getValue());
        }
    }

    @FXML
    void onClickEdit(ActionEvent event) {
    	TreeItem<Section> SelectedItem=outlineTree.getSelectionModel().getSelectedItem();
    	if(SelectedItem!=null) {
    		model.showEditView(SelectedItem.getValue());
        }
    	
    }
    
    @FXML
    void onClickView(ActionEvent event) {
    	model.showTreeView();
    }
    
    //Task 1
    @FXML
    void onClickCompare(ActionEvent event) {
    	model.showCompView();
    }
    
    //Task 2
    @FXML
    void onClickSection(ActionEvent event) {
    	TreeItem<Section> SelectedItem=outlineTree.getSelectionModel().getSelectedItem();
    	if(SelectedItem!=null) {
    		model.showSection(SelectedItem.getValue());
    	}

    }

}


