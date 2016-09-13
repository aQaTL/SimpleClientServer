package com.aqatl.simpleclientserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

			while(true)
			{
				Socket clientConnection = serverSocket.accept();
				System.out.println("Connection #" + ++connectionsCount + " from " + clientConnection.getInetAddress());

				SimpleConnection connection = new SimpleConnection(clientConnection, this, mainMessageListener);
				connections.add(connection);
				new Thread(connection).start();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public List<String> getUsers()
	{
		return connections.stream()
				.map(SimpleConnection::getNickName)
				.collect(Collectors.toList());
	}

	public static void main(String[] args)
	{
		SimpleServer server = new SimpleServer();
	}
}

