package models;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import views.BPMainController;
import views.CloneWindowController;

public class MainViewModel {

	public MyRemoteClient client;
	public BorderPane mainview;

	public MainViewModel(MyRemoteClient wowclient, BorderPane view) {
		this.client=wowclient;
		this.mainview=view;
	}

	public void showCloneView(ViewTransitionModelInterface vm) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainViewModel.class
				.getResource("../views/CloneWindow.fxml"));
		try {
			BorderPane view = loader.load();
			CloneWindowController cont = loader.getController();
			cont.setModel(this,vm);

			Stage stage = new Stage();
			Scene s = new Scene(view);
			stage.setScene(s);
			stage.setTitle("Clone Page");
			stage.show();
		} catch (IOException e) {
			System.out.println("erroe");
			e.printStackTrace();
		}

	}

	//switch to BPMainView window
	public void showCopyView() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainViewModel.class
				.getResource("../views/BPMainView.fxml"));
		try {
			BorderPane view = loader.load();
			System.out.println(client.getCurrentBP());
			
			BPMainModel model = new BPMainModel(client, view);
			BPMainController cont = loader.getController();

			cont.setModel(model);
		
			Stage stage = new Stage();
			Scene s = new Scene(view);
			stage.setScene(s);
			stage.setTitle("BPViewer");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
