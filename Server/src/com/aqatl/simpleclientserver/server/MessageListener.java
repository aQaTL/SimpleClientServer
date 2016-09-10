package com.aqatl.simpleclientserver.server;

/**
 * Created by Maciej on 10.09.2016.
 */
public interface MessageListener
{
	void sendMessage(SimpleConnection source, String message);
}
