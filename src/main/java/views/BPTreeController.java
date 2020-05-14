package views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import models.BPMainModel;
import models.Section;

public class BPTreeController {
	BPMainModel model;
    @FXML
    private Label BPNameLabel;

    @FXML
    private TreeView<String> BPTreeView;
    
    private void addNodes(Section p, TreeItem<String> t) {
		t.setExpanded(true);
		for (Section child : p.children) {
			TreeItem<String> node = new TreeItem<String>(child.showContent());
			t.getChildren().add(node);
			addNodes(child, node);
		}
	}
    
    public void setModel(BPMainModel newModel) {
    	model = newModel;
    	
    	Section root = model.client.getCurrentBP().getRoot();
    	String rootContent = root.showContent();
		TreeItem<String> rootItem = new TreeItem<String>(rootContent);
		addNodes(root, rootItem);
		BPTreeView.setShowRoot(true);
		BPTreeView.setRoot(rootItem);
		
		BPNameLabel.textProperty().set(model.client.getCurrentBP().name);
    	
    }

}
