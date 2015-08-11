/*
 *  AutoSIM - Internet of Things Simulator
 *  Copyright (C) 2014, Aditya Yadav <aditya@automatski.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.automatski.autosim.xmpp;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import com.automatski.autosim.environments.IConnection;
import com.automatski.autosim.environments.IConnectionListener;
import com.automatski.autosim.xmpp.config.AutoSIMConnectionConfig;

public class SampleXMPPConnection implements IConnection {

	private AutoSIMConnectionConfig config = null;
	private IConnectionListener listener = null;
	private XMPPTCPConnectionConfiguration connConfig = null;
	private AbstractXMPPConnection connection = null;
	private ChatManager chatmanager = null;
	private Chat chat = null;
	
	@Override
	public void connect() throws Exception {
		// Create a connection to the jabber.org server on a specific port.
		connConfig = XMPPTCPConnectionConfiguration.builder()
		  .setUsernameAndPassword(config.username, config.password)
		  .setServiceName(config.servicename)
		  .setHost(config.host)
		  .setPort(config.port)
		  .build();
		
		connection = new XMPPTCPConnection(connConfig);
		// Connect to the server
		connection.connect();
		// Log into the server
		connection.login();

		String receiver = config.username;
		// Assume we've created an XMPPConnection name "connection"._
		chatmanager = ChatManager.getInstanceFor(connection);
		chat = chatmanager.createChat(config.username, new ChatMessageListener() {
			public void processMessage(Chat chat, Message message) {
				System.out.println("Received message: " + message);
				try {
					if (listener != null) listener.receive(message.getBody());
				} catch (Exception e){
					System.out.println(e.getMessage());
				}
			}
		});

	}

	@Override
	public void disconnect() throws Exception {
		chat.close();
		connection.disconnect();
		
	}

	@Override
	public Object send(Object arg0) throws Exception {
		String message = (String) arg0;
		chat.sendMessage(message);
		return null;
	}

	@Override
	public void setConfig(Object arg0) {
		config = (AutoSIMConnectionConfig) arg0;
		
	}

	@Override
	public void setListener(IConnectionListener listener) {
		this.listener = listener;
		
	}

}
