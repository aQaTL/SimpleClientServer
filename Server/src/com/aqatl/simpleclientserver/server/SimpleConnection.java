package com.aqatl.simpleclientserver.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Maciej on 10.09.2016.
 */
public class SimpleConnection implements Runnable, MessageListener
{
	private Socket socket;
	private MessageListener msgListener;
	private PrintWriter out;
	private String nickName;

	public SimpleConnection(Socket socket, MessageListener msgListener) throws IOException
	{
		this.socket = socket;
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
				msgListener.sendMessage(this, remoteIn.nextLine());
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
