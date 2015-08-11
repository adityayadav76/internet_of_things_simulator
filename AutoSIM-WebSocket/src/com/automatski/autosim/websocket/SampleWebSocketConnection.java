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
package com.automatski.autosim.websocket;


import java.net.URI;
import java.nio.ByteBuffer;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.websocket.jsr356.ClientContainer;
import org.eclipse.jetty.websocket.jsr356.ContainerManager;
import org.eclipse.jetty.websocket.jsr356.JettyClientContainerProvider;

import com.automatski.autosim.environments.IConnection;
import com.automatski.autosim.environments.IConnectionListener;
import com.automatski.autosim.websocket.config.AutoSIMConnectionConfig;

public class SampleWebSocketConnection implements IConnection {

	private AutoSIMConnectionConfig config = null;
	private IConnectionListener listener = null;

	private WebSocketContainer container = null;
	private Session session = null;
	private String sessionId = null;
	
	@Override
	public void connect() throws Exception {
		URI uri = URI.create(config.url);
		
		container = ContainerManager.getContainer();
		
		
        //container = (WebSocketContainer) JettyClientContainerProvider.getWebSocketContainer();//ContainerProvider.getWebSocketContainer();
        // Attempt Connect
        session = container.connectToServer(MyEventClientSocket.class,uri);
        String sessionId = session.getId();
        //System.out.println("established session with id: " + sessionId);
        session.addMessageHandler(new MessageHandler.Whole<String>() {

			@Override
			public void onMessage(String msg) {
				//System.out.println(sessionId + ": text message: " + msg);
				try {
					if (listener != null) listener.receive(sessionId + ": text message: " + msg);
				} catch (Exception e){
					System.out.println(e.getMessage());
				}
			}
		});

		// add binary based message handler
		session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

			@Override
			public void onMessage(ByteBuffer buffer) {
				//System.out.println(sessionId + ": binary message: "+ new String(buffer.array()));
				try {
					if (listener != null) listener.receive(sessionId + ": binary message: "+ new String(buffer.array()));
				} catch (Exception e){
					System.out.println(e.getMessage());
				}
			}
		});

	}

	@Override
	public void disconnect() throws Exception {
		//nothing to do
		session.close();
        if (container instanceof LifeCycle)
        {
            ((LifeCycle)container).stop();
        }
	}

	@Override
	public Object send(Object arg0) throws Exception {

		// add text based message handler
		// Send a message
		session.getBasicRemote().sendText("Hello");
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
