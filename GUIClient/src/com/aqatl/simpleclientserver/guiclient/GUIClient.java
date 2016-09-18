package com.aqatl.simpleclientserver.guiclient;

import com.aqatl.simpleclientserver.guiclient.ui.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Created by Maciej on 18.09.2016.
 */
public class GUIClient extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(Controller.class.getResource("View.fxml"));
		Parent view = loader.load();

		loader.<Controller>getController().init(primaryStage);

		primaryStage.setTitle("GUIClient");
		primaryStage.setScene(new Scene(view));
		primaryStage.show();
	}


	public static void main(String[] args)
	{
		launch(args);
	}

}
