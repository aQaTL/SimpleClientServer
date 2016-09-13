package com.aqatl.simpleclientserver.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Maciej on 10.09.2016.
 */
public class SimpleConnection implements Runnable, MessageListener
{
	private Socket socket;
	private SimpleServer server;
	private MessageListener msgListener;
	private PrintWriter out;
	private String nickName;

	public SimpleConnection(Socket socket, SimpleServer server, MessageListener msgListener) throws IOException
	{
		this.socket = socket;
		this.server = server;
		this.msgListener = msgListener;
		out = new PrintWriter(socket.getOutputStream(), true);
	}

	@Override
	public void run()
	{
		try
		{
			Scanner remoteIn = new Scanner(socket.getInputStream());
			askForNickName(remoteIn);
			while (remoteIn.hasNextLine())
			{
				String line = remoteIn.nextLine();

				if (line.startsWith("/"))
					parseCommand(line.substring(1).toLowerCase());
				else
					msgListener.sendMessage(this, line);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void parseCommand(String command) throws IOException
	{
		switch (command)
		{
			case "users":
				server.getUsers().forEach(out::println);
				break;
			case "exit":
				socket.close();
				break;

			default:
				out.println("Invalid command");
				break;
		}
	}

	private void askForNickName(Scanner in)
	{
		out.println("Your nickname: ");
		nickName = in.nextLine();
		out.println("Welcome " + nickName + " to our chat!");
	}

	@Override
	public void sendMessage(SimpleConnection source, String message)
	{
		out.println(source.getNickName() + " -> " + message);
	}

	public String getNickName()
	{
		return nickName;
	}

}
