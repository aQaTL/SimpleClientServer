package com.aqatl.simpleclientserver.guiclient.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Scanner;

/**
 * Created by Maciej on 18.09.2016.
 */
public class Controller
{
	@FXML
	private TextArea mainTextArea;
	@FXML
	private TextField inputField;
	@FXML
	private ListView<String> usersList;

	private PrintWriter outStream;
	private String username;

	public void init(Stage stage)
	{
		try (Socket socket = new Socket(getIPAddress(), 8189))
		{
			Scanner inStream = new Scanner(socket.getInputStream());
			outStream = new PrintWriter(socket.getOutputStream(), true);

			stage.setOnCloseRequest(event -> outStream.println("/exit"));

			new Thread(() ->
			{
				while (inStream.hasNextLine())
				{
					mainTextArea.appendText(inStream.nextLine() + "\n");
				}
			}).start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			if(e instanceof UnknownHostException)
				getIPAddress();
		}
	}

	@FXML
	private void sendMessage(KeyEvent keyEvent)
	{
		if(keyEvent.getCode() == KeyCode.ENTER)
		{
			String msg = inputField.getText();
			outStream.println(msg);
			mainTextArea.appendText(msg + "\n"); //TODO with username

			if(msg.equals("/exit"))
				System.exit(0);
			else
				inputField.setText("");
		}
	}

	private void updateUsers()
	{
		//W którejś książce było coś o przesyłaniu obiektów,
		//może da się zrefactorować getUsers z logiki servera tak, aby tutaj
		//docierało do mnie to w postaci List<String>
	}

	private String getIPAddress()
	{
		TextInputDialog inputDialog = new TextInputDialog();
		inputDialog.setTitle("GUIClient");
		inputDialog.setHeaderText("Connect to server");
		inputDialog.setContentText("Enter server IP address");

		Optional<String> input = inputDialog.showAndWait();
		if(input.isPresent())
			return input.get();
		else
		{
			System.exit(0);
			return null; //Stupid compiler
		}
	}
}
