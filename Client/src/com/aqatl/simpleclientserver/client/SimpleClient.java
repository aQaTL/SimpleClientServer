package com.aqatl.simpleclientserver.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Maciej on 10.09.2016.
 */
public class SimpleClient
{
	public static void main(String[] args) throws IOException
	{
		if (args.length < 1)
			return;

		try (Socket socket = new Socket(args[0], 8189))
		{
			Scanner remoteIn = new Scanner(socket.getInputStream());
			new Thread(() ->
			{
				while(remoteIn.hasNextLine())
				{
					System.out.println(remoteIn.nextLine());
				}
			}).start();

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			Scanner stdIn = new Scanner(System.in);
			while(stdIn.hasNextLine())
			{
				String msg = stdIn.nextLine();
				out.println(msg);
				if(msg.equals("/exit"))
				{
					System.exit(0);
				}
			}
		}
	}
}
