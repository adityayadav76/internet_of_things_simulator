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
import java.net.URI;
import java.nio.ByteBuffer;

import javax.websocket.ContainerProvider;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.eclipse.jetty.util.component.LifeCycle;

public class WebSocketClient
{
    public static void main(String[] args)
    {
        URI uri = URI.create("ws://localhost:10090/events/");

        try
        {
            //WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        	WebSocketContainer container = (WebSocketContainer) org.eclipse.jetty.websocket.jsr356.JettyClientContainerProvider.getWebSocketContainer();//ContainerProvider.getWebSocketContainer();
            
            try
            {
                // Attempt Connect
                Session session = container.connectToServer(MyEventClientSocket.class,uri);
                final String sessionId = session.getId();
        		System.out.println("established session with id: " + sessionId);
                // add text based message handler
        		session.addMessageHandler(new MessageHandler.Whole<String>() {

        			@Override
        			public void onMessage(String msg) {
        				System.out.println(sessionId + ": text message: " + msg);
        			}
        		});

        		// add binary based message handler
        		session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

        			@Override
        			public void onMessage(ByteBuffer buffer) {
        				System.out.println(sessionId + ": binary message: "
        						+ new String(buffer.array()));
        			}
        		});
                // Send a message
                session.getBasicRemote().sendText("Hello");
                
                try {
                	Thread.sleep(5000);
                }catch (Exception e){}
                
                // Close session
                session.close();
            }
            finally
            {
                // Force lifecycle stop when done with container.
                // This is to free up threads and resources that the
                // JSR-356 container allocates. But unfortunately
                // the JSR-356 spec does not handle lifecycles (yet)
                if (container instanceof LifeCycle)
                {
                    ((LifeCycle)container).stop();
                }
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }
}