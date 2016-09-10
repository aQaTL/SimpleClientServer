package com.aqatl.simpleclientserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Maciej on 10.09.2016.
 */
public class SimpleServer
{
	private ArrayList<SimpleConnection> connections;
	private MessageListener mainMessageListener;

	public SimpleServer()
	{
		try (ServerSocket serverSocket = new ServerSocket(8189))
		{
			connections = new ArrayList<>();
			mainMessageListener = (source, message) -> connections.forEach(connection ->
			{
				if(source != connection)
					connection.sendMessage(source, message);
			});
			int connectionsCount = 0;

			Scanner nickNameScanner;
			while(true)
			{
				Socket clientConnection = serverSocket.accept();
				System.out.println("Connection #" + ++connectionsCount + " from " + clientConnection.getInetAddress());

				SimpleConnection connection = new SimpleConnection(clientConnection, mainMessageListener);
				connections.add(connection);
				new Thread(connection).start();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		SimpleServer server = new SimpleServer();
	}
}

